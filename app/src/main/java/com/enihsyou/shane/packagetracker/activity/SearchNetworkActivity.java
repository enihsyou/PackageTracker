package com.enihsyou.shane.packagetracker.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.adapter.NetworkListViewAdapter;
import com.enihsyou.shane.packagetracker.async_task.FetchCourierTask;
import com.enihsyou.shane.packagetracker.async_task.FetchNetworkTask;
import com.enihsyou.shane.packagetracker.dialog.ChooseAreaDialog;
import com.enihsyou.shane.packagetracker.enums.DialogType;
import com.enihsyou.shane.packagetracker.listener.OnAddressButtonClickListener;
import com.enihsyou.shane.packagetracker.model.*;

import java.util.ArrayList;

import static com.enihsyou.shane.packagetracker.model.Places.PROVINCES;

public class SearchNetworkActivity extends NeedLocationActivity implements
    ChooseAreaDialog.OnChooseListener {
    private static final String TAG = "SearchNetworkActivity";

    private TextInputEditText mStreetText;
    private Button mNetworkButton;
    private Button mCourierButton;

    private ListView mListView;
    private ExpandableListView mDetailView;
    private NetworkListViewAdapter mListViewAdapter;

    public NetworkListViewAdapter getListViewAdapter() {
        return mListViewAdapter;
    }

    public void setListViewAdapter(NetworkListViewAdapter listViewAdapter) {
        mListViewAdapter = listViewAdapter;
    }

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
        mListView = (ListView) findViewById(R.id.entry_list);
        mDetailView = (ExpandableListView) findViewById(R.id.sub_list);
        mListViewAdapter =
            new NetworkListViewAdapter(this, R.layout.network_card, new ArrayList<NetworkSearchResult.NetworkNetList>());
        mListView.setAdapter(mListViewAdapter);

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
                    .execute(location, street, "0", "10");
            }
        });
        mCourierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sendChoose == null) return;
                String location = sendChoose.getFullName();
                String street = mStreetText.getText().toString();
                if (street.isEmpty() && mCurrentLocation != null) {
                    String area = calcStreetPart();
                    if (area != null) { street = area; } else street = location;
                }
                Log.d(TAG, String.format("onClick: 搜索快递员: 在 %s %s",
                    location, street));

                new FetchCourierTask(SearchNetworkActivity.this)
                    .execute(location, street);
            }
        });
        /*启动的时候 获取地点信息*/
        requestUpdateLocation(false);
    }

    private String calcStreetPart() {
        String street = null;
        try {
            String area1 = mCurrentLocation.getResults().get(0).getFormattedAddress();
            String area2 = mCurrentLocation.getResults().get(1).getFormattedAddress();
            street = area1.substring(area2.length());
        } catch (Exception e) {
            Log.e(TAG, "onClick: 处理地区字符串时错误", e);

        }
        return street;
    }

    public ExpandableListView getDetailView() {
        return mDetailView;
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
            default:
                Log.wtf(TAG, "WTF " + type);
        }
        setSendButtons(sendChoose);
    }

    @Override
    void setSendButtons(Area sendChoose) {
        super.setSendButtons(sendChoose);
        String street = calcStreetPart();
        if (street != null) { mStreetText.setText(street); }
    }

    @Override
    public void OnDialogPositiveClick(DialogFragment dialog) {
        Log.d(TAG, "OnDialogPositiveClick: ok");
    }

    @Override
    public void OnDialogNegativeClick(DialogFragment dialog) {
        Log.d(TAG, "OnDialogNegativeClick: cancel");
    }
}
