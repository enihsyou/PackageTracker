package com.enihsyou.shane.packagetracker;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.enihsyou.shane.packagetracker.model.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.model.PackageTrafficSearchResult;
import com.enihsyou.shane.packagetracker.model.Packages;

import java.util.List;

public class PackageDetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private NestedScrollView mScrollView;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) supportActionBar.setDisplayHomeAsUpEnabled(true);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        List<PackageTrafficSearchResult> packages = Packages.getPackages(this);
        for (PackageTrafficSearchResult aPackage : packages) {
            // if (aPackage.getNumber().startsWith(query)) {}
            if (aPackage.getNumber().equals(query)) {
                LinearLayout linearLayout = (LinearLayout) LayoutInflater
                        .from(this)
                        .inflate(R.layout.traffic_header_card, mScrollView, false);
                mScrollView.addView(Kuaidi100Fetcher.generateCard(aPackage, linearLayout));
            }
        }
    }
}
