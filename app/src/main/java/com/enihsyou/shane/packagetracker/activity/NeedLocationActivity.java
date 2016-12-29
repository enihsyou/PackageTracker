package com.enihsyou.shane.packagetracker.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.async_task.FetchLocationTask;
import com.enihsyou.shane.packagetracker.helper.LocationGetter;
import com.enihsyou.shane.packagetracker.listener.OnAddressButtonClickListener;
import com.enihsyou.shane.packagetracker.model.*;

import java.util.List;
import java.util.Locale;

import static com.enihsyou.shane.packagetracker.model.Places.*;

public abstract class NeedLocationActivity extends AppCompatActivity {
    public static final int REQUEST_LOCATION_SERVICE = 254;
    private static final String TAG = "NeedLocationActivity";
    FloatingActionButton mFab;
    OnAddressButtonClickListener mProvinceSendClick;
    OnAddressButtonClickListener mCitySendClick;
    OnAddressButtonClickListener mAreaSendClick;
    Button mProvinceSendButton;
    Button mCitySendButton;
    Button mAreaSendButton;
    Area sendChoose;
    Location mLocation;
    int provinceSendIndex;
    int citySendIndex;
    int areaSendIndex;
    LocationGetter mLocationGetter;
    CurrentLocationResult mCurrentLocation;

    static Area calcChooseArea(int first, int second, int third) {
        PROVINCES.get(first).populate();
        PROVINCES.get(first).nexts.get(second).populate();
        return (Area) PROVINCES.get(first).nexts.get(second).nexts.get(third);
    }

    /**
     * 遍历省份列表，找到匹配的对象序号
     *
     * @param compare 要对比的字符串
     *
     * @return 匹配的省份列表中的序号
     */
    private static int trySetProvinceButton(String compare) {
        for (int i = 0; i < PROVINCES.size(); i++) {
            Province province = PROVINCES.get(i);
            if (compare.contains(province.getName())) {
                Log.d(TAG, "trySetProvinceButton: province " + i);
                return i;
            }
        }
        return -1;
    }

    /**
     * 遍历城市列表，找到匹配的县区序号
     *
     * @param parentSelected 匹配的省份列表
     * @param compare        对比的字符串
     *
     * @return 匹配的列表中的序号
     */
    private static int trySetCityButton(int parentSelected, String compare) {
        if (parentSelected < 0) return -1;
        Province parent = getProvince(parentSelected); //匹配的省份
        parent.populate(); //扩展下级列表
        for (Place city : parent.nexts) {
            if (compare.contains(city.getName())) {
                Log.d(TAG, "trySetCityButton: city " + parent.nexts.indexOf(city));
                return parent.nexts.indexOf(city);
            }
        }
        return -1;
    }

    /**
     * 遍历县区列表，找到匹配的序号
     *
     * @param parent1 省份序号
     * @param parent2 城市序号
     * @param compare 对比字符串
     *
     * @return 匹配的县区序号
     */
    private static int trySetAreaButton(int parent1, int parent2, String compare) {
        if (parent1 < 0 || parent2 < 0) return -1;
        City city = getCity(parent1, parent2);
        city.populate();//扩展下级列表
        for (Place place : city.nexts) {
            if (compare.contains(place.getName())) {
                Log.d(TAG, "trySetAreaButton: area " + city.nexts.indexOf(place));
                return city.nexts.indexOf(place);
            }
        }
        return -1;
    }

    void requestUpdateLocation(boolean update) {
        if (resolveLocationPermission(this)) {
            getLocation(update);
            if (mLocation == null) {
                Log.i(TAG, "requestUpdateLocation: 没有完成定位");
                Toast.makeText(this, "未获得位置信息", Toast.LENGTH_SHORT).show();
                mLocationGetter.requestUpdate(update);
                return;
            }
            new FetchLocationTask(this).execute(mLocation.getLatitude(), mLocation.getLongitude());
        } else {
            Log.d(TAG, "requestUpdateLocation: 没有定位能力");
        }
    }

    /**
     * @return 是否有获取位置信息的能力
     */
    private static boolean resolveLocationPermission(Activity context) {
        /*检查应用权限*/
        int GpsPermissionCheck =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int NetworkPermissionCheck =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        Log.d(TAG, String.format(Locale.getDefault(), "resolveLocationPermission: 当前定位权限 GPS: %b Network %b", GpsPermissionCheck, NetworkPermissionCheck));

        if (GpsPermissionCheck != PackageManager.PERMISSION_GRANTED
            || NetworkPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "resolveLocationPermission: 需要向用户请求定位权限");
            /*如果需要向用户解释请求权限的原因*/
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Log.d(TAG, "resolveLocationPermission: 需要向解释请求定位权限的原因");
                showPermissionAlert(context);
            }

            ActivityCompat.requestPermissions(context, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQUEST_LOCATION_SERVICE);
        }
        return LocationGetter.isPositionable(context);
    }

    void getLocation(boolean update) {
        mLocationGetter = new LocationGetter(this);
        mLocationGetter.requestUpdate(update);
        mLocation = mLocationGetter.getCurrent();
        if (mLocation == null) return;
        Log.d(TAG, String.format(Locale.getDefault(), "getLocation: 获得当前 经度: %f 纬度: %f", mLocation.getLongitude(), mLocation.getLatitude()));
    }

    private static void showPermissionAlert(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("没有定位权限");
        alertDialog.setMessage("我们需要定位权限来获取你的当前位置，按确定跳转到设置页面。");
        alertDialog.setPositiveButton(context.getResources().getString(R.string.go_setting), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton(context.getResources().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void updateLocationSelection(CurrentLocationResult current) {
        mCurrentLocation = current;
        List<CurrentLocationResult.AddressComponents> addresses = null;
        /*检测当前地点是否有区级地址*/
        for (CurrentLocationResult.Results results : current.getResults()) {
            List<String> types = results.getTypes();
            if (types.contains("sublocality_level_1")) {
                addresses = results.getAddresses();
                break;
            }
        }
        if (addresses == null) throw new NullPointerException("没有找到地点");
        String third = "", second = "", first = "";
        /*获取各级地址名字*/
        for (CurrentLocationResult.AddressComponents components : addresses) {
            List<String> types = components.getTypes();
            if (types.contains("sublocality_level_1")) {
                third = components.getShortName();
                continue;
            }
            if (types.contains("locality")) {
                second = components.getShortName();
                continue;
            }
            if (types.contains("administrative_area_level_1")) {
                first = components.getShortName();
                break;
            }
        }
        Log.d(TAG, String.format("updateLocationSelection: 解析地址结果 %s %s %s", first, second, third));
        /*设置下拉框*/
        int indexFirst = -1, indexSecond = -1, indexThird = -1;
        indexFirst = trySetProvinceButton(first);
        if (indexFirst >= 0) indexSecond = trySetCityButton(indexFirst, second);
        if (indexSecond >= 0) indexThird = trySetAreaButton(indexFirst, indexSecond, third);
        if (indexThird < 0) {
            Log.d(TAG, String.format("updateLocationSelection: 失败 定位 设置下拉框 %s %s %s",
                first, second, third));
        } else {
            try {
                setProvinceSendButton(indexFirst, getProvince(indexFirst));
                setCitySendButton(indexSecond, getCity(indexFirst, indexSecond));
                setAreaSendButton(indexThird, getArea(indexFirst, indexSecond, indexThird));
                setSendButtons(calcChooseArea(indexFirst, indexSecond, indexThird));
                Log.d(TAG, String.format("updateLocationSelection: 成功 定位 设置下拉框 %s-%s-%s",
                    first, second, third));
                Toast.makeText(this, String.format("定位到%s-%s-%s", first, second, third), Toast.LENGTH_SHORT).show();
            } catch (IndexOutOfBoundsException e) {
                Log.e(TAG, "updateLocationSelection: confirmed bug", e);
            }
        }
    }



    void setAreaSendButton(int which, Area place) {
        sendChoose = calcChooseArea(provinceSendIndex, citySendIndex, which);
        areaSendIndex = which;
        if (sendChoose != place) { throw new IllegalArgumentException("area send not equal"); }
    }

    void setCitySendButton(int which, City place) {
        sendChoose = calcChooseArea(provinceSendIndex, which, 0);
        citySendIndex = which;
        mAreaSendClick.setList(getCity(provinceSendIndex, citySendIndex).nexts);
        if (place.isDirectControlled()) {
            setProvinceSendButton(which, getProvince(provinceSendIndex));
        }
        setAreaSendButton(0, (Area) place.nexts.get(0));
    }

    void setProvinceSendButton(int which, Province place) {
        if (which == provinceSendIndex) return;
        sendChoose = calcChooseArea(which, 0, 0);
        provinceSendIndex = which;
        mCitySendClick.setList(getProvince(provinceSendIndex).nexts);
        if (place.isDirectControlled()) {
            setCitySendButton(which, (City) place.nexts.get(which));
        } else {
            setCitySendButton(0, (City) place.nexts.get(0));
        }
    }

    /**
     * 设置寄送方的三个按钮
     * @param sendChoose 发送的位置
     */
    @SuppressWarnings("Duplicates")
    void setSendButtons(Area sendChoose) {
        if (sendChoose != null && !sendChoose.getName().isEmpty()) {
            mProvinceSendButton.setText(sendChoose.getFirst());
            mCitySendButton.setText(sendChoose.getSecond());
            mAreaSendButton.setText(sendChoose.getName());
            if (sendChoose.isDirectControlled()) {
                mCitySendButton.setEnabled(false);
            } else {
                mCitySendButton.setEnabled(true);
            }
            mAreaSendButton.setEnabled(true);
        }
        mProvinceSendClick.setPosition(provinceSendIndex);
        mCitySendClick.setPosition(citySendIndex);
        mAreaSendClick.setPosition(areaSendIndex);
    }
}
