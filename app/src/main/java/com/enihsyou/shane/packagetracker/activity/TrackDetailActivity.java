package com.enihsyou.shane.packagetracker.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.model.PackageEachTraffic;
import com.enihsyou.shane.packagetracker.model.PackageTrafficSearchResult;
import com.enihsyou.shane.packagetracker.model.Packages;

import java.util.List;

public class TrackDetailActivity extends AppCompatActivity {
    private static final String TAG = "TrackDetailActivity";
    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private NestedScrollView mScrollView;
    private PackageTrafficSearchResult mTraffic;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mScrollView = (NestedScrollView) findViewById(R.id.detail_card_container);

        setSupportActionBar(mToolbar);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneNumber != null) {
                    makePhoneCall();
                } else {
                    Toast.makeText(TrackDetailActivity.this, "没有找到电话号码", Toast.LENGTH_SHORT).show();
                }
            }

            private void makePhoneCall() {
                Intent makePhoneCall = new Intent(Intent.ACTION_DIAL);
                makePhoneCall.setData(Uri.parse("tel:" + phoneNumber));
                Log.d(TAG, "makePhoneCall: Dial " + phoneNumber);
                startActivity(makePhoneCall);
            }
        });
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) supportActionBar.setDisplayHomeAsUpEnabled(true);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        } else {
            String packageNumber = intent.getStringExtra(MainActivity.PACKAGE_NUMBER);
            doMySearch(packageNumber);
        }
    }

    private void doMySearch(String query) {
        List<PackageTrafficSearchResult> packages = Packages.getPackages(this, null);
        for (PackageTrafficSearchResult aPackage : packages) {
            /*匹配成功*/
            if (aPackage.getNumber().equals(query)) {
                mTraffic = aPackage;
                LinearLayout linearLayout = (LinearLayout) LayoutInflater
                    .from(this)
                    .inflate(R.layout.traffic_header_card, mScrollView, false);
                mScrollView.addView(Kuaidi100Fetcher.generateCard(aPackage, linearLayout));

                mFab.show();
                setPhoneNumber();
                return;
            }
        }
        /*匹配失败*/
        LinearLayout linearLayout = (LinearLayout) LayoutInflater
            .from(this)
            .inflate(R.layout.traffic_not_found, mScrollView, false);
        mScrollView.addView(linearLayout);
        mFab.hide();
        mToolbar.collapseActionView();
        Button button = (Button) linearLayout.findViewById(R.id.add_new);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNew = new Intent(getApplication(), AddNewPackageActivity.class);
                startActivity(addNew);
            }
        });
    }

    private void setPhoneNumber() {
        for (PackageEachTraffic traffic : mTraffic.getTraffics()) {
            String phone = traffic.getPhone();
            if (phone != null) {
                phoneNumber = phone;
                break;
            }
        }
    }
}
