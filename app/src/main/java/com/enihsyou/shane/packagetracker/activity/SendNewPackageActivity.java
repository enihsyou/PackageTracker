package com.enihsyou.shane.packagetracker.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TimingLogger;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.async_tasks.FetchLocationTask;
import com.enihsyou.shane.packagetracker.async_tasks.FetchPriceTask;
import com.enihsyou.shane.packagetracker.async_tasks.FetchTimeTask;
import com.enihsyou.shane.packagetracker.dialog.ChooseAreaDialog;
import com.enihsyou.shane.packagetracker.helper.LocationGetter;
import com.enihsyou.shane.packagetracker.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SendNewPackageActivity extends AppCompatActivity implements
    ChooseAreaDialog.ChooseListener {
    public static final Province[] PROVINCES = {
        new Province("北京", "110000", null, true),
        new Province("天津", "120000", null, true),
        new Province("上海", "310000", null, true),
        new Province("重庆", "500000", null, true),
        new Province("广东", "440000", null),
        new Province("浙江", "330000", null),
        new Province("江苏", "320000", null),
        new Province("山东", "370000", null),
        new Province("河南", "410000", null),
        new Province("河北", "130000", null),
        new Province("福建", "350000", null),
        new Province("湖北", "420000", null),
        new Province("陕西", "610000", null),
        new Province("辽宁", "210000", null),
        new Province("四川", "510000", null),
        new Province("安徽", "340000", null),
        new Province("湖南", "430000", null),
        new Province("山西", "140000", null),
        new Province("黑龙江", "230000", null),
        new Province("广西", "450000", null),
        new Province("内蒙古", "150000", null),
        new Province("吉林", "220000", null),
        new Province("云南", "530000", null),
        new Province("江西", "360000", null),
        new Province("贵州", "520000", null),
        new Province("甘肃", "620000", null),
        new Province("新疆", "650000", null),
        new Province("海南", "460000", null),
        new Province("宁夏", "640000", null),
        new Province("青海", "630000", null),
        new Province("西藏", "540000", null),
        new Province("台湾", "710000", null),
        new Province("香港", "810000", null),
        new Province("澳门", "820000", null)
    };
    private static final String TAG = "SendNewPackageActivity";
    private static final int REQUEST_LOCATION_SERVICE = 254;
    private static TimingLogger mTimings;
    private Location mLocation;
    private FloatingActionButton mFab;
    private Button mPriceButton;
    private Button mTimeButton;
    private Button mProvinceSendButton;
    private Button mProvinceReceiveButton;
    private Button mCitySendButton;
    private Button mCityReceiveButton;
    private Button mAreaSendButton;
    private Button mAreaReceiveButton;
    private EditText mWeight;
    private LocationGetter mLocationGetter;
    private Area sendChoose;
    private Area receiveChoose;
    private DialogFragment mDialogFragment;
    private ButtonListener mProvinceSendClick;
    private ButtonListener mProvinceReceiveClick;
    private ButtonListener mCitySendClick;
    private ButtonListener mCityReceiveClick;
    private ButtonListener mAreaSendClick;
    private ButtonListener mAreaReceiveClick;

    private int provinceSendIndex;
    private int provinceReceiveIndex;
    private int citySendIndex;
    private int cityReceiveIndex;
    private int areaSendIndex;
    private int areaReceiveIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate: 启动发送新快递");
        setContentView(R.layout.activity_send_new_package);
        mTimings = new TimingLogger(TAG, "计时器");
        mFab = (FloatingActionButton) findViewById(R.id.fab_send_new);
        mPriceButton = (Button) findViewById(R.id.button_search_price);
        mTimeButton = (Button) findViewById(R.id.button_search_time);
        mProvinceSendButton = (Button) findViewById(R.id.btn_province_send);
        mProvinceReceiveButton = (Button) findViewById(R.id.btn_province_receive);
        mCitySendButton = (Button) findViewById(R.id.btn_city_send);
        mCityReceiveButton = (Button) findViewById(R.id.btn_city_receive);
        mAreaSendButton = (Button) findViewById(R.id.btn_area_send);
        mAreaReceiveButton = (Button) findViewById(R.id.btn_area_receive);
        mWeight = (EditText) findViewById(R.id.package_weight_input);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "onClick: 点击FAB，更新位置信息");
                requestUpdateLocation(true);
            }
        });
        mProvinceSendClick =
            new ButtonListener(DialogType.SEND_PROVINCE, mProvinceSendButton, "选择寄件省份", new ArrayList<Place>(Arrays.asList(PROVINCES)));
        mProvinceSendButton.setOnClickListener(mProvinceSendClick);
        mProvinceReceiveClick =
            new ButtonListener(DialogType.RECEIVE_PROVINCE, mProvinceReceiveButton, "选择收件省份", new ArrayList<Place>(Arrays.asList(PROVINCES)));
        mProvinceReceiveButton.setOnClickListener(mProvinceReceiveClick);
        mCitySendClick =
            new ButtonListener(DialogType.SEND_CITY, mCitySendButton, "选择寄件城市", new ArrayList<Place>());
        mCitySendButton.setOnClickListener(
            mCitySendClick);
        mCityReceiveClick =
            new ButtonListener(DialogType.RECEIVE_CITY, mCityReceiveButton, "选择收件城市", new ArrayList<Place>());
        mCityReceiveButton.setOnClickListener(
            mCityReceiveClick);
        mAreaSendClick =
            new ButtonListener(DialogType.SEND_AREA, mAreaSendButton, "选择寄件地区", new ArrayList<Place>());
        mAreaSendButton.setOnClickListener(
            mAreaSendClick);
        mAreaReceiveClick =
            new ButtonListener(DialogType.RECEIVE_AREA, mAreaReceiveButton, "选择收件地区", new ArrayList<Place>());
        mAreaReceiveButton.setOnClickListener(
            mAreaReceiveClick);
        mCitySendButton.setEnabled(false);
        mCityReceiveButton.setEnabled(false);
        mAreaSendButton.setEnabled(false);
        mAreaReceiveButton.setEnabled(false);
        /*处理 查询价格按钮*/
        mPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Area itemFrom = sendChoose;
                Area itemTo = receiveChoose;
                String locationSend = itemFrom.getFullName();
                String locationSendCode = itemFrom.getCode();
                String locationReceive = itemTo.getFullName();
                String locationReceiveCode = itemTo.getCode();
                String weight = mWeight.getText().toString();
                weight = weight.isEmpty() ? "1" : weight;
                Log.d(TAG, String.format("onClick: 搜索价格: 从 %s %s，到 %s %s，重量 %s",
                    locationSend, locationSendCode, locationReceive, locationReceiveCode, weight));

                new FetchPriceTask(SendNewPackageActivity.this)
                    .execute(locationSendCode, locationReceiveCode, "", weight);
            }
        });
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Area itemFrom = sendChoose;
                Area itemTo = receiveChoose;
                String locationSend = itemFrom.getFullName();
                String locationSendCode = itemFrom.getCode();
                String locationReceive = itemTo.getFullName();
                String locationReceiveCode = itemTo.getCode();
                Log.d(TAG, String.format("onClick: 搜索时效: 从 %s %s，到 %s %s",
                    locationSend, locationSendCode, locationReceive, locationReceiveCode));

                new FetchTimeTask(SendNewPackageActivity.this)
                    .execute(locationSendCode, locationReceiveCode);
            }
        });
        /*启动的时候 获取地点信息*/
        requestUpdateLocation(false);
    }

    public void requestUpdateLocation(boolean update) {
        if (resolveLocationPermission()) {
            getLocation(update);
            new FetchLocationTask(this).execute(mLocation.getLatitude(), mLocation.getLongitude());
        } else {
            Log.d(TAG, "requestUpdateLocation: 没有定位能力");
        }
    }

    /**
     * @return 是否有获取位置信息的能力
     */
    private boolean resolveLocationPermission() {
        /*检查应用权限*/
        int GpsPermissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int NetworkPermissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        Log.d(TAG, String.format(Locale.getDefault(), "resolveLocationPermission: 当前定位权限 GPS: %b Network %b", GpsPermissionCheck, NetworkPermissionCheck));

        if (GpsPermissionCheck != PackageManager.PERMISSION_GRANTED
            || NetworkPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "resolveLocationPermission: 需要向用户请求定位权限");
            /*如果需要向用户解释请求权限的原因*/
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Log.d(TAG, "resolveLocationPermission: 需要向解释请求定位权限的原因");
                showPermissionAlert(this);
            }

            ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQUEST_LOCATION_SERVICE);
        }
        return LocationGetter.isPositionable(this);
    }

    private void getLocation(boolean update) {
        mLocationGetter = new LocationGetter(this, update);
        mLocation = mLocationGetter.getCurrent();
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
                setProvinceSendButton(indexFirst, PROVINCES[indexFirst]);
                setCitySendButton(indexSecond, (City) PROVINCES[indexFirst].nexts.get(indexSecond));
                setAreaSendButton(indexThird, (Area) PROVINCES[indexFirst].nexts.get(indexSecond).nexts.get(indexThird));
                setButtons(calcChooseArea(indexFirst, indexSecond, indexThird));
                Log.d(TAG, String.format("updateLocationSelection: 成功 定位 设置下拉框 %s-%s-%s",
                    first, second, third));
                Toast.makeText(this, String.format("定位到%s-%s-%s", first, second, third), Toast.LENGTH_SHORT).show();
            } catch (IndexOutOfBoundsException e) {
                Log.e(TAG, "updateLocationSelection: confirmed bug", e);
            }
        }
    }

    /**
     * 遍历省份列表，找到匹配的对象序号
     *
     * @param compare 要对比的字符串
     *
     * @return 匹配的省份列表中的序号
     */
    private static int trySetProvinceButton(String compare) {
        for (int i = 0; i < PROVINCES.length; i++) {
            Province province = PROVINCES[i];
            if (compare.contains(province.getName())) {
                Log.d(TAG, "trySetProvinceButton: province " + i);
                return i;
            }
        }
        return -1;
    }

    /**
     * 遍历城市列表，找到匹配的地区序号
     *
     * @param parentSelected 匹配的省份列表
     * @param compare        对比的字符串
     *
     * @return 匹配的列表中的序号
     */
    private static int trySetCityButton(int parentSelected, String compare) {
        if (parentSelected < 0) return -1;
        Province parent = PROVINCES[parentSelected]; //匹配的省份
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
     * 遍历地区列表，找到匹配的序号
     *
     * @param parent1 省份序号
     * @param parent2 城市序号
     * @param compare 对比字符串
     *
     * @return 匹配的地区序号
     */
    private static int trySetAreaButton(int parent1, int parent2, String compare) {
        if (parent1 < 0 || parent2 < 0) return -1;
        City city = (City) PROVINCES[parent1].nexts.get(parent2);
        city.populate();//扩展下级列表
        for (Place place : city.nexts) {
            if (compare.contains(place.getName())) {
                Log.d(TAG, "trySetAreaButton: area " + city.nexts.indexOf(place));
                return city.nexts.indexOf(place);
            }
        }
        return -1;
    }

    private void setButtons(Area sendChoose) {
        setButtons(sendChoose, receiveChoose);
    }

    private static Area calcChooseArea(int first, int second, int third) {
        PROVINCES[first].populate();
        PROVINCES[first].nexts.get(second).populate();
        return (Area) PROVINCES[first].nexts.get(second).nexts.get(third);
    }

    @SuppressWarnings("Duplicates")
    private void setButtons(Area sendChoose, Area receiveChoose) {
        if (sendChoose != null && !sendChoose.getName().isEmpty()) {
            mProvinceSendButton.setText(sendChoose.getFirst());
            mCitySendButton.setText(sendChoose.getSecond());
            mAreaSendButton.setText(sendChoose.getName());
            if (sendChoose.isDirectControlled()) {
                mCitySendButton.setEnabled(false);
            } else {
                mCitySendButton.setEnabled(true);
            }
        }
        if (receiveChoose != null && !receiveChoose.getName().isEmpty()) {
            mProvinceReceiveButton.setText(receiveChoose.getFirst());
            mCityReceiveButton.setText(receiveChoose.getSecond());
            mAreaReceiveButton.setText(receiveChoose.getName());
            if (receiveChoose.isDirectControlled()) {
                mCityReceiveButton.setEnabled(false);
            } else {
                mCityReceiveButton.setEnabled(true);
            }
        }
        mAreaSendButton.setEnabled(true);
        mAreaReceiveButton.setEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_SERVICE: {
                try {
                    getLocation(true);
                    new FetchLocationTask(this).execute(mLocation.getLatitude(), mLocation.getLongitude());
                } catch (Exception e) {
                    Log.e(TAG, "onRequestPermissionsResult: ", e);
                }
            }
        }
    }

    @Override
    public void OnItemClick(DialogFragment dialog, DialogType type, int which, Place place) {
        Log.d(TAG, "OnItemClick: " + place);
        // Province provinceItem;
        // City cityItem;
        // Area areaItem;
        switch (type) {
            case SEND_PROVINCE:
                setProvinceSendButton(which, (Province) place);
                break;
            case SEND_CITY:
                setCitySendButton(which, (City) place);
                break;
            case SEND_AREA:
                setAreaSendButton(which, (Area) place);
                break;
            case RECEIVE_PROVINCE:
                setProvinceReceiveButton(which, (Province) place);
                break;
            case RECEIVE_CITY:
                setCityReceiveButton(which, (City) place);
                break;
            case RECEIVE_AREA:
                setAreaReceiveButton(which, (Area) place);
                break;
            default:
                Log.wtf(TAG, "WTF " + type);
        }
        setButtons();
    }

    @Override
    public void OnDialogPositiveClick(DialogFragment dialog) {
        Log.d(TAG, "OnDialogPositiveClick: ok");
    }

    @Override
    public void OnDialogNegativeClick(DialogFragment dialog) {
        Log.d(TAG, "OnDialogNegativeClick: cancel");
    }

    private void setAreaReceiveButton(int which, Area place) {
        Log.d(TAG,
            "setAreaReceiveButton() called with: which = [" + which + "], place = [" + place + "]");
        receiveChoose = calcChooseArea(provinceReceiveIndex, cityReceiveIndex, which);
        areaReceiveIndex = which;
        if (receiveChoose != place) {
            throw new IllegalArgumentException("area receive not equal :"+receiveChoose+ "  "+place);
        }
    }

    private void setCityReceiveButton(int which, City place) {
        Log.d(TAG,
            "setCityReceiveButton() called with: which = [" + which + "], place = [" + place + "]");
        receiveChoose = calcChooseArea(provinceReceiveIndex, which, 0);
        cityReceiveIndex = which;
        mAreaReceiveClick.setList(PROVINCES[provinceReceiveIndex].nexts.get(which).nexts);
        if (place.isDirectControlled()) {
            setProvinceReceiveButton(which, PROVINCES[which]);
        }
        setAreaReceiveButton(0, (Area) place.nexts.get(0));
    }

    private void setProvinceReceiveButton(int which, Province place) {
        Log.d(TAG,
            "setProvinceReceiveButton() called with: which = [" + which + "], place = [" + place
                + "]");
        if (which == provinceReceiveIndex) return;
        receiveChoose = calcChooseArea(which, 0, 0);
        provinceReceiveIndex = which;
        mCityReceiveClick.setList(PROVINCES[which].nexts);
        if (place.isDirectControlled()) {
            setCityReceiveButton(which, (City) place.nexts.get(which));
        } else {
            setCityReceiveButton(0, (City) place.nexts.get(0));
        }
    }

    private void setAreaSendButton(int which, Area place) {
        Log.d(TAG,
            "setAreaSendButton() called with: which = [" + which + "], place = [" + place + "]");
        sendChoose = calcChooseArea(provinceSendIndex, citySendIndex, which);
        areaSendIndex = which;
        if (sendChoose != place) { throw new IllegalArgumentException("area send not equal"); }
    }

    private void setCitySendButton(int which, City place) {
        Log.d(TAG,
            "setCitySendButton() called with: which = [" + which + "], place = [" + place + "]");
        sendChoose = calcChooseArea(provinceSendIndex, which, 0);
        citySendIndex = which;
        mAreaSendClick.setList(PROVINCES[provinceSendIndex].nexts.get(which).nexts);
        if (place.isDirectControlled()) {
            setProvinceSendButton(which, PROVINCES[which]);
        }
        setAreaSendButton(0, (Area) place.nexts.get(0));
    }

    private void setProvinceSendButton(int which, Province place) {
        Log.d(TAG, "setProvinceSendButton() called with: which = [" + which + "], place = [" + place
            + "]");
        if (which == provinceSendIndex) return;
        sendChoose = calcChooseArea(which, 0, 0);
        provinceSendIndex = which;
        mCitySendClick.setList(PROVINCES[which].nexts);
        if (place.isDirectControlled()) {
            setCitySendButton(which, (City) place.nexts.get(which));
        } else {
            setCitySendButton(0, (City) place.nexts.get(0));
        }
    }

    private void setButtons() {
        setButtons(sendChoose, receiveChoose);
    }

    public enum DialogType {
        SEND_PROVINCE,
        RECEIVE_PROVINCE,
        SEND_CITY,
        RECEIVE_CITY,
        SEND_AREA,
        RECEIVE_AREA
    }

    private class ButtonListener implements View.OnClickListener {
        private FragmentManager mManager;
        private String mTitle;
        private ArrayList<Place> mList;
        private DialogType mType;
        private Button mButton;

        public ButtonListener(DialogType type, Button button, String title, ArrayList<Place> list) {
            mType = type;
            mButton = button;
            mManager = getSupportFragmentManager();
            mTitle = title;
            mList = list;
        }

        public void setList(ArrayList<Place> list) {
            mList = list;
        }

        @Override
        public void onClick(View v) {
            mDialogFragment = ChooseAreaDialog.newInstance(mType, mTitle, mList);
            mDialogFragment.show(mManager, String.format("选择框 %s", mTitle));
        }
    }
}
