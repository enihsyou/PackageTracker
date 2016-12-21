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
                }else{
                    Toast.makeText(TrackDetailActivity.this, "没有找到电话号码", Toast.LENGTH_SHORT).show();
                }
            }

            private void makePhoneCall() {
                Intent makePhoneCall = new Intent(Intent.ACTION_DIAL);
                makePhoneCall.setData(Uri.parse("tel:" + phoneNumber));
                Log.d(TAG, "makePhoneCall: Dial "+ phoneNumber);
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
            String packageNumber = intent.getStringExtra("package_number");
            doMySearch(packageNumber);
        }
    }

    private void doMySearch(String query) {
        List<PackageTrafficSearchResult> packages = Packages.getPackages(this);
        for (PackageTrafficSearchResult aPackage : packages) {
            // if (aPackage.getNumber().startsWith(query)) {}
            if (aPackage.getNumber().equals(query)) {
                mTraffic = aPackage;
                LinearLayout linearLayout = (LinearLayout) LayoutInflater
                        .from(this)
                        .inflate(R.layout.traffic_header_card, mScrollView, false);
                mScrollView.addView(Kuaidi100Fetcher.generateCard(aPackage, linearLayout));

                setPhoneNumber();
            }
        }
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
