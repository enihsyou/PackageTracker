package com.enihsyou.shane.packagetracker.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.helper.LocationGetter;
import com.enihsyou.shane.packagetracker.model.*;
import com.enihsyou.shane.packagetracker.network.FetchLocationTask;
import com.enihsyou.shane.packagetracker.network.FetchPriceTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SendNewPackageActivity extends AppCompatActivity {
    private static final String TAG = "SendNewPackageActivity";
    private static final int REQUEST_LOCATION_SERVICE = 254;
    public boolean isReady;
    private Location mLocation;
    private FloatingActionButton mFab;
    private Button mPriceButton;
    private Button mTimeButton;
    private Spinner mProvinceSendSpinner;
    private Spinner mProvinceReceiveSpinner;
    private Spinner mCitySendSpinner;
    private Spinner mCityReceiveSpinner;
    private Spinner mAreaSendSpinner;
    private Spinner mAreaReceiveSpinner;
    private EditText mWeight;
    private LocationGetter mLocationGetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate: 启动发送新快递");
        setContentView(R.layout.activity_send_new_package);

        mFab = (FloatingActionButton) findViewById(R.id.fab_send_new);
        mPriceButton = (Button) findViewById(R.id.button_search_price);
        mTimeButton = (Button) findViewById(R.id.button_search_time);
        mProvinceSendSpinner = (Spinner) findViewById(R.id.province_spinner_send);
        mProvinceReceiveSpinner = (Spinner) findViewById(R.id.province_spinner_receive);
        mCitySendSpinner = (Spinner) findViewById(R.id.city_spinner_send);
        mCityReceiveSpinner = (Spinner) findViewById(R.id.city_spinner_receive);
        mAreaSendSpinner = (Spinner) findViewById(R.id.area_spinner_send);
        mAreaReceiveSpinner = (Spinner) findViewById(R.id.area_spinner_receive);
        mWeight = (EditText) findViewById(R.id.package_weight_input);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "onClick: 点击FAB，更新位置信息");
                requestUpdateLocation();
            }
        });
        /*设置下拉框们···*/
        final Province[] provinces = new Province[]{
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
        ArrayAdapter<? extends Place> provinceArrayAdapter =
            new ArrayAdapter<>(this, R.layout.spinner_textview, provinces);
        ArrayAdapter<? extends Place> defaultNoneAdapter =
            new ArrayAdapter<>(this, R.layout.spinner_textview,
                new Place[]{new Province("请选择", "00", null)});
        provinceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        defaultNoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProvinceSendSpinner.setAdapter(provinceArrayAdapter);
        mProvinceReceiveSpinner.setAdapter(provinceArrayAdapter);
        mCitySendSpinner.setAdapter(defaultNoneAdapter);
        mCityReceiveSpinner.setAdapter(defaultNoneAdapter);
        mAreaSendSpinner.setAdapter(defaultNoneAdapter);
        mAreaReceiveSpinner.setAdapter(defaultNoneAdapter);
        mProvinceSendSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressWarnings("Duplicates")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Province selectedItem = (Province) parent.getSelectedItem();
                if (selectedItem.isDirectControlled()) {
                    mCitySendSpinner.setAdapter(mProvinceSendSpinner.getAdapter());
                    mCitySendSpinner.setSelection(position);
                    return;
                }
                selectedItem.populate();
                ArrayList<? extends Place> cities = selectedItem.nexts;
                ArrayAdapter cityArrayAdapter =
                    new ArrayAdapter<>(SendNewPackageActivity.this, R.layout.spinner_textview, cities);
                cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mCitySendSpinner.setAdapter(cityArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected: province send");
            }
        });
        mProvinceReceiveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressWarnings("Duplicates")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Province selectedItem = (Province) parent.getSelectedItem();
                if (selectedItem.isDirectControlled()) {
                    mCityReceiveSpinner.setAdapter(mProvinceReceiveSpinner.getAdapter());
                    mCityReceiveSpinner.setSelection(position);
                    return;
                }
                selectedItem.populate();
                ArrayList<? extends Place> cities = selectedItem.nexts;
                ArrayAdapter cityArrayAdapter =
                    new ArrayAdapter<>(SendNewPackageActivity.this, R.layout.spinner_textview, cities);
                cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mCityReceiveSpinner.setAdapter(cityArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected: province receive");
            }
        });
        mCitySendSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressWarnings("Duplicates")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Place selectedItem;
                try { //当前省份是直辖市
                    Province item = (Province) parent.getSelectedItem();
                    mProvinceSendSpinner.setSelection(position);
                    selectedItem = new City(item.getName(), item.getCode(), item.nexts);
                } catch (ClassCastException e) {
                    City item = (City) parent.getSelectedItem();
                    selectedItem = item;
                }
                try {
                    selectedItem.populate();
                } catch (IOException e) {
                    e.printStackTrace();// FIXME: 2016/12/27
                }
                ArrayList<? extends Place> areas = selectedItem.nexts;
                int originPosition = mAreaSendSpinner.getSelectedItemPosition();
                ArrayAdapter<? extends Place> cityArrayAdapter =
                    new ArrayAdapter<>(SendNewPackageActivity.this, R.layout.spinner_textview, areas);
                cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mAreaSendSpinner.setAdapter(cityArrayAdapter);
                mAreaSendSpinner.setSelection(originPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected: city send");
            }
        });
        mCityReceiveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressWarnings("Duplicates")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Place selectedItem;
                try { //当前省份是直辖市
                    Province item = (Province) parent.getSelectedItem();
                    mProvinceReceiveSpinner.setSelection(position);
                    selectedItem = item;
                } catch (ClassCastException e) {
                    City item = (City) parent.getSelectedItem();
                    selectedItem = item;
                }
                try {
                    selectedItem.populate();
                } catch (IOException e) {
                    e.printStackTrace();// FIXME: 2016/12/27
                }
                ArrayList<? extends Place> areas = selectedItem.nexts;
                int originPosition = mAreaReceiveSpinner.getSelectedItemPosition();
                ArrayAdapter<? extends Place> cityArrayAdapter =
                    new ArrayAdapter<>(SendNewPackageActivity.this, R.layout.spinner_textview, areas);
                cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mAreaReceiveSpinner.setAdapter(cityArrayAdapter);
                mAreaReceiveSpinner.setSelection(originPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected: city receive");
            }
        });
        /*启动的时候 获取地点信息*/
        requestUpdateLocation();
        /*处理 查询价格按钮*/
        mWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Area item = (Area) mAreaSendSpinner.getSelectedItem();
                String locationSend = item.getFullName();
                String locationSendCode = item.getCode();
                String locationReceive = item.getFullName();
                String locationReceiveCode = item.getCode();
                String weight = mWeight.getText().toString();
                Log.d(TAG, String.format("onClick: 搜索价格: 从 %s %s，到 %s %s，重量 %s ",
                    locationSend, locationSendCode, locationReceive, locationReceiveCode, weight));

                new FetchPriceTask(SendNewPackageActivity.this);
            }
        });
    }

    public void requestUpdateLocation() {
        if (resolveLocationPermission()) {
            getLocation();
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

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("没有定位权限");
                alertDialog.setMessage("我们需要定位权限来获取你的当前位置，按确定跳转到设置页面。");
                // alertDialog.setTitle("没有有效的定位手段");
                // alertDialog.setMessage("请启用GPS或者网络定位");
                alertDialog.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        SendNewPackageActivity.this.startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton(getResources().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }

            ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQUEST_LOCATION_SERVICE);
        }
        return LocationGetter.isPositionable(this);
    }

    private void getLocation() {
        mLocationGetter = new LocationGetter(this);
        mLocation = mLocationGetter.getCurrent();
        Log.d(TAG, String.format(Locale.getDefault(), "getLocation: 获得当前 经度: %f 纬度: %f", mLocation.getLongitude(), mLocation.getLatitude()));
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
        boolean isFirstSucceed = false, isSecondSucceed = false, isThirdSucceed = false;
        isFirstSucceed = setSpinner(mProvinceSendSpinner, first);
        if (isFirstSucceed) isSecondSucceed = setSpinner(mCitySendSpinner, second);
        if (isSecondSucceed) isThirdSucceed = setSpinner(mAreaSendSpinner, third);
        if (!isThirdSucceed) {
            Log.d(TAG, String.format("updateLocationSelection: 失败 定位 设置下拉框 %s %s %s···",
                first, second, third));
        } else {
            Log.d(TAG, String.format("updateLocationSelection: 成功 定位 设置下拉框 %s %s %s···",
                first, second, third));
            Toast.makeText(this, String.format("定位到%s", ((Area) mAreaSendSpinner.getSelectedItem()).getFullName()), Toast.LENGTH_SHORT).show();
        }
    }

    private <T extends Place> boolean setSpinner(Spinner spinner, String compare) {
        ArrayAdapter<? extends Place> provinceAdapter =
            (ArrayAdapter<? extends Place>) spinner.getAdapter();
        for (int i = 0; i < provinceAdapter.getCount(); i++) {
            T item = (T) provinceAdapter.getItem(i);
            if (compare.contains(item.getName())) {
                spinner.setSelection(i);
                if (spinner.getOnItemSelectedListener() != null) {
                    spinner.getOnItemSelectedListener().onItemSelected(spinner, spinner, i, spinner.getItemIdAtPosition(i));
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_SERVICE: {
                try {
                    getLocation();
                    new FetchLocationTask(this).execute(mLocation.getLatitude(), mLocation.getLongitude());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "onRequestPermissionsResult: e", e);
                }
            }
        }
    }
}
