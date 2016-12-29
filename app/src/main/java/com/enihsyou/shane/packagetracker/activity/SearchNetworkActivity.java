package com.enihsyou.shane.packagetracker.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.async_tasks.FetchCourierTask;
import com.enihsyou.shane.packagetracker.async_tasks.FetchNetworkTask;
import com.enihsyou.shane.packagetracker.enums.DialogType;
import com.enihsyou.shane.packagetracker.model.Place;

import java.util.ArrayList;

import static com.enihsyou.shane.packagetracker.model.Places.PROVINCES;

public class SearchNetworkActivity extends NeedLocationActivity {
    private static final String TAG = "SearchNetworkActivity";

    private TextInputEditText mStreetText;
    private Button mNetworkButton;
    private Button mCourierButton;
    private ListView mListView;
    private ExpandableListView mDeatailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_search);

        mFab = (FloatingActionButton) findViewById(R.id.fab_location);
        mProvinceSendButton = (Button) findViewById(R.id.btn_province_send);
        mCitySendButton = (Button) findViewById(R.id.btn_city_send);
        mAreaSendButton = (Button) findViewById(R.id.btn_area_send);
        mStreetText = (TextInputEditText) findViewById(R.id.ipt_street);
        mNetworkButton = (Button) findViewById(R.id.btn_search_network);
        mCourierButton = (Button) findViewById(R.id.btn_search_courier);
        /*设置按钮监听器*/
        mProvinceSendClick =
            new OnAddressButtonClickListener(
                this, DialogType.SEND_PROVINCE,
                "选择寄件省份", PROVINCES);
        mCitySendClick =
            new OnAddressButtonClickListener(
                this, DialogType.SEND_CITY,
                "选择寄件城市", new ArrayList<Place>());
        mAreaSendClick =
            new OnAddressButtonClickListener(
                this, DialogType.SEND_AREA,
                "选择寄件县区", new ArrayList<Place>());
        mProvinceSendButton.setOnClickListener(mProvinceSendClick);
        mCitySendButton.setOnClickListener(mCitySendClick);
        mAreaSendButton.setOnClickListener(mAreaSendClick);
        /*预设置按钮*/
        mCitySendButton.setEnabled(false);
        mAreaSendButton.setEnabled(false);
        /*FAB设置点击强制更新位置信息*/
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "onClick: 点击FAB，更新位置信息");
                requestUpdateLocation(true);
            }
        });
        /*处理 查询价格按钮*/
        mNetworkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sendChoose == null) return;
                String location = sendChoose.getFullName();
                String locationCode = sendChoose.getCode();
                String street = mStreetText.getText().toString();
                Log.d(TAG, String.format("onClick: 搜索网点: 从 %s %s %s",
                    location, locationCode, street));

                new FetchNetworkTask(SearchNetworkActivity.this)
                    .execute(location, locationCode, street);
            }
        });
        mCourierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sendChoose == null) return;
                String location = sendChoose.getFullName();
                String locationCode = sendChoose.getCode();
                String street = mStreetText.getText().toString();
                Log.d(TAG, String.format("onClick: 搜索快递员: 从 %s %s %s",
                    location, locationCode, street));
                new FetchCourierTask(SearchNetworkActivity.this)
                    .execute(location, locationCode, street);
            }
        });
        /*启动的时候 获取地点信息*/
        requestUpdateLocation(false);
    }
}
