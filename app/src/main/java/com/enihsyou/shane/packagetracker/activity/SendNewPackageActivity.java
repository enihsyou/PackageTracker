package com.enihsyou.shane.packagetracker.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.async_tasks.FetchLocationTask;
import com.enihsyou.shane.packagetracker.async_tasks.FetchPriceTask;
import com.enihsyou.shane.packagetracker.async_tasks.FetchTimeTask;
import com.enihsyou.shane.packagetracker.dialog.ChooseAreaDialog;
import com.enihsyou.shane.packagetracker.enums.DialogType;
import com.enihsyou.shane.packagetracker.model.Area;
import com.enihsyou.shane.packagetracker.model.City;
import com.enihsyou.shane.packagetracker.model.Place;
import com.enihsyou.shane.packagetracker.model.Province;

import java.util.ArrayList;

import static com.enihsyou.shane.packagetracker.model.Places.*;

public class SendNewPackageActivity extends NeedLocationActivity implements
    ChooseAreaDialog.OnChooseListener {
    private static final String TAG = "SendNewPackageActivity";

    private Button mPriceButton;
    private Button mTimeButton;
    private Button mProvinceReceiveButton;
    private Button mCityReceiveButton;
    private Button mAreaReceiveButton;
    private EditText mWeight;
    private Area receiveChoose;
    private GridLayout mGridLayout;

    private OnAddressButtonClickListener mCityReceiveClick;
    private OnAddressButtonClickListener mProvinceReceiveClick;
    private OnAddressButtonClickListener mAreaReceiveClick;

    private int provinceReceiveIndex;
    private int cityReceiveIndex;
    private int areaReceiveIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate: 启动发送新快递");
        setContentView(R.layout.activity_send_new_package);
        mFab = (FloatingActionButton) findViewById(R.id.fab_location);
        mPriceButton = (Button) findViewById(R.id.button_search_price);
        mTimeButton = (Button) findViewById(R.id.button_search_time);
        mProvinceSendButton = (Button) findViewById(R.id.btn_province_send);
        mProvinceReceiveButton = (Button) findViewById(R.id.btn_province_receive);
        mCitySendButton = (Button) findViewById(R.id.btn_city_send);
        mCityReceiveButton = (Button) findViewById(R.id.btn_city_receive);
        mAreaSendButton = (Button) findViewById(R.id.btn_area_send);
        mAreaReceiveButton = (Button) findViewById(R.id.btn_area_receive);
        mWeight = (EditText) findViewById(R.id.package_weight_input);
        mGridLayout = (GridLayout) findViewById(R.id.list_container);
        /*FAB设置点击强制更新位置信息*/
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "onClick: 点击FAB，更新位置信息");
                requestUpdateLocation(true);
            }
        });
        /*设置按钮监听器*/
        mProvinceSendClick =
            new OnAddressButtonClickListener(
                this, DialogType.SEND_PROVINCE,
                "选择寄件省份", PROVINCES);
        mProvinceReceiveClick =
            new OnAddressButtonClickListener(
                this, DialogType.RECEIVE_PROVINCE,
                "选择收件省份", PROVINCES);
        mCitySendClick =
            new OnAddressButtonClickListener(
                this, DialogType.SEND_CITY,
                "选择寄件城市", new ArrayList<Place>());
        mCityReceiveClick =
            new OnAddressButtonClickListener(
                this, DialogType.RECEIVE_CITY,
                "选择收件城市", new ArrayList<Place>());
        mAreaSendClick =
            new OnAddressButtonClickListener(
                this, DialogType.SEND_AREA,
                "选择寄件县区", new ArrayList<Place>());
        mAreaReceiveClick =
            new OnAddressButtonClickListener(
                this, DialogType.RECEIVE_AREA,
                "选择收件县区", new ArrayList<Place>());
        mProvinceSendButton.setOnClickListener(mProvinceSendClick);
        mProvinceReceiveButton.setOnClickListener(mProvinceReceiveClick);
        mCitySendButton.setOnClickListener(mCitySendClick);
        mCityReceiveButton.setOnClickListener(mCityReceiveClick);
        mAreaSendButton.setOnClickListener(mAreaSendClick);
        mAreaReceiveButton.setOnClickListener(mAreaReceiveClick);
        /*预设置按钮*/
        mCitySendButton.setEnabled(false);
        mCityReceiveButton.setEnabled(false);
        mAreaSendButton.setEnabled(false);
        mAreaReceiveButton.setEnabled(false);
        /*处理 查询价格按钮*/
        mPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sendChoose == null || receiveChoose == null) return;
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
                if (sendChoose == null || receiveChoose == null) return;
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

    @SuppressWarnings("Duplicates")
    private void setReceiveButtons(Area receiveChoose) {
        if (receiveChoose != null && !receiveChoose.getName().isEmpty()) {
            mProvinceReceiveButton.setText(receiveChoose.getFirst());
            mCityReceiveButton.setText(receiveChoose.getSecond());
            mAreaReceiveButton.setText(receiveChoose.getName());
            if (receiveChoose.isDirectControlled()) {
                mCityReceiveButton.setEnabled(false);
            } else {
                mCityReceiveButton.setEnabled(true);
            }
            mAreaReceiveButton.setEnabled(true);

            mProvinceReceiveClick.setPosition(provinceReceiveIndex);
            mCityReceiveClick.setPosition(cityReceiveIndex);
            mAreaReceiveClick.setPosition(areaReceiveIndex);
        }
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

    // @Override
    // public void onConfigurationChanged(Configuration newConfig) {
    //     super.onConfigurationChanged(newConfig);
    //     switch (newConfig.orientation) {
    //         case Configuration.ORIENTATION_LANDSCAPE:
    //             Toast.makeText(this, "横屏", Toast.LENGTH_LONG).show();
    //             Log.d(TAG, "onConfigurationChanged: 横屏");
    //             break;
    //         case Configuration.ORIENTATION_PORTRAIT:
    //             Toast.makeText(this, "竖屏", Toast.LENGTH_LONG).show();
    //             Log.d(TAG, "onConfigurationChanged: 竖屏");
    //             break;
    //         default:
    //             Log.d(TAG, "onConfigurationChanged: 触发" );
    //     }
    // }

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
            throw new IllegalArgumentException(
                "area receive not equal :" + receiveChoose + "  " + place);
        }
    }

    private void setCityReceiveButton(int which, City place) {
        Log.d(TAG,
            "setCityReceiveButton() called with: which = [" + which + "], place = [" + place + "]");
        receiveChoose = calcChooseArea(provinceReceiveIndex, which, 0);
        cityReceiveIndex = which;
        mAreaReceiveClick.setList(getCity(provinceReceiveIndex, cityReceiveIndex).nexts);
        if (place.isDirectControlled()) {
            setProvinceReceiveButton(which, getProvince(cityReceiveIndex));
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
        mCityReceiveClick.setList(getProvince(provinceReceiveIndex).nexts);
        if (place.isDirectControlled()) {
            setCityReceiveButton(which, (City) place.nexts.get(which));
        } else {
            setCityReceiveButton(0, (City) place.nexts.get(0));
        }
    }

    private void setButtons() {
        setSendButtons(sendChoose);
        setReceiveButtons(receiveChoose);
    }

    public GridLayout getGridLayout() {
        return mGridLayout;
    }
}
