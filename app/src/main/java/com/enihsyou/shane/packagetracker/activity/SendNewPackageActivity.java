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
import com.enihsyou.shane.packagetracker.model.City;
import com.enihsyou.shane.packagetracker.model.Place;
import com.enihsyou.shane.packagetracker.model.Province;
import com.enihsyou.shane.packagetracker.network.FetchLocationTask;

import java.io.IOException;
import java.util.Locale;

public class SendNewPackageActivity extends AppCompatActivity {
    private static final String TAG = "SendNewPackageActivity";
    private static final int REQUEST_LOCATION_SERVICE = 254;
    private Location mLocation;
    private FloatingActionButton mFab;
    private Button mPriceButton;
    private Button mTimeButton;
    private Spinner mProvinceSendSpinner;
    private Spinner mProvinceReceiveSpinner;
    private Spinner mCitySendSpinner;
    private Spinner mCityReceiveSpinner;
    private Spinner mAreaSendSpinner;
    private Spinner mAreaRecieveSpinner;
    private EditText mWeight;
    private LocationGetter mLocationGetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_new_package);

        mFab = (FloatingActionButton) findViewById(R.id.fab_send_new);
        mPriceButton = (Button) findViewById(R.id.button_search_price);
        mTimeButton = (Button) findViewById(R.id.button_search_time);
        mProvinceSendSpinner = (Spinner) findViewById(R.id.province_spinner_send);
        mProvinceReceiveSpinner = (Spinner) findViewById(R.id.province_spinner_receive);
        mCitySendSpinner = (Spinner) findViewById(R.id.city_spinner_send);
        mCityReceiveSpinner = (Spinner) findViewById(R.id.city_spinner_receive);
        mAreaSendSpinner = (Spinner) findViewById(R.id.area_spinner_send);
        mAreaRecieveSpinner = (Spinner) findViewById(R.id.area_spinner_receive);
        mWeight = (EditText) findViewById(R.id.package_weight_input);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 点击FAB，更新位置信息");
                requestUpdateLocation();
            }
        });
        Province[] provinces = new Province[]{
            new Province("北京", "110000", null),
            new Province("天津", "120000", null),
            new Province("上海", "310000", null),
            new Province("重庆", "500000", null),
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
        ArrayAdapter provinceArrayAdapter =
            new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, provinces);
        mProvinceSendSpinner.setAdapter(provinceArrayAdapter);
        mProvinceReceiveSpinner.setAdapter(provinceArrayAdapter);

        mProvinceSendSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Province selectedItem = (Province) parent.getSelectedItem();
                selectedItem.populate();
                Place[] cities = new City[selectedItem.nexts.size()];
                cities = selectedItem.nexts.toArray(cities);
                ArrayAdapter cityArrayAdapter =
                    new ArrayAdapter<>(SendNewPackageActivity.this, android.R.layout.simple_spinner_dropdown_item, cities);
                mCitySendSpinner.setAdapter(cityArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mProvinceReceiveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Province selectedItem = (Province) parent.getSelectedItem();
                selectedItem.populate();
                Place[] cities = selectedItem.nexts.toArray(new Place[selectedItem.nexts.size()]);
                ArrayAdapter cityArrayAdapter =
                    new ArrayAdapter<>(SendNewPackageActivity.this, android.R.layout.simple_spinner_dropdown_item, cities);
                mCityReceiveSpinner.setAdapter(cityArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mCitySendSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                City selectedItem = (City) parent.getSelectedItem();
                try {
                    selectedItem.populate();
                } catch (IOException e) {// TODO: 2016/12/24 错误
                    e.printStackTrace();
                }
                Place[] areas = selectedItem.nexts.toArray(new Place[selectedItem.nexts.size()]);
                ArrayAdapter cityArrayAdapter =
                    new ArrayAdapter<>(SendNewPackageActivity.this, android.R.layout.simple_spinner_dropdown_item, areas);
                mAreaSendSpinner.setAdapter(cityArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mCityReceiveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                City selectedItem = (City) parent.getSelectedItem();
                try {
                    selectedItem.populate();
                } catch (IOException e) {// TODO: 2016/12/24 错误
                    e.printStackTrace();
                }
                Place[] areas = new Place[selectedItem.nexts.size()];
                areas = selectedItem.nexts.toArray(areas);
                ArrayAdapter cityArrayAdapter =
                    new ArrayAdapter<>(SendNewPackageActivity.this, android.R.layout.simple_spinner_dropdown_item, areas);
                mAreaRecieveSpinner.setAdapter(cityArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void requestUpdateLocation() {
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
        Log.d(TAG, String.format(Locale.getDefault(), "resolveLocationPermission: 权限 GPS: %b Network %b", GpsPermissionCheck, NetworkPermissionCheck));

        if (GpsPermissionCheck != PackageManager.PERMISSION_GRANTED
            || NetworkPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            /*如果需要向用户解释请求权限的原因*/
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Log.d(TAG, "resolveLocationPermission: 向用户请求定位权限");

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
