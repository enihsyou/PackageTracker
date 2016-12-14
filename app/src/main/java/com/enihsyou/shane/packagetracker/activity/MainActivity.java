package com.enihsyou.shane.packagetracker.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.adapter.SectionsPagerAdapter;
import com.enihsyou.shane.packagetracker.fragment.PackageTrafficsFragment;
import com.enihsyou.shane.packagetracker.model.PackageTrafficSearchResult;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener,
    PackageTrafficsFragment.OnListFragmentInteractionListener {
    private static final String TAG = "MainActivity";

    private Toolbar mToolbar;
    private FabSpeedDial mFab;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;

    private ViewPager mViewPager; //负责3个滑动页面
    private SectionsPagerAdapter mSectionsPagerAdapter; //负责提供3个页面，保存在内存里
    private TabLayout mTabLayout;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFab = (FabSpeedDial) findViewById(R.id.fab_speed_dial);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        setSupportActionBar(mToolbar); //顺序错误会导致点击左上角按钮失效
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) supportActionBar.setDisplayHomeAsUpEnabled(true);

        /*创建3个滑动页面的适配器，并设置上去*/
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_page_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        /*给TabLayout设置ViewPager*/
        mTabLayout.setupWithViewPager(mViewPager);
        /*FAB的点击监听器*/
        mFab.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_receive_package:
                        startActivity(new Intent(MainActivity.this, AddNewPackageActivity.class));
                        break;
                    case R.id.action_send_package:
                        startActivity(new Intent(MainActivity.this, SendNewPackageActivity.class));
                        break;
                    case R.id.action_network_search:
                        startActivity(new Intent(MainActivity.this, NetworkSearchActivity.class));
                        break;
                    case R.id.action_chop_hands:
                        Intent taobao = new Intent(Intent.ACTION_VIEW);
                        taobao.setClassName("com.taobao.taobao", "com.taobao.tao.welcome.Welcome");
                        /*检查是否安装了淘宝*/
                        List<ResolveInfo> activities =
                            getPackageManager().queryIntentActivities(taobao, PackageManager.GET_ACTIVITIES);
                        Log.i(TAG, "onMenuItemSelected: 找到 " + activities + " 启动淘宝App");
                        if (activities.size() > 0) {
                            startActivity(taobao);
                        } else {
                            /*如果没有安装*/
                            Toast.makeText(MainActivity.this, "no taobao", Toast.LENGTH_SHORT).show();// TODO: 2016/12/14 如果没有安装 打开网页吧···
                        }
                        break;
                    default:
                        Snackbar.make(getCurrentFocus(),
                            "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                }
                return true; //是否被处理了
            }
        });
        /*点击左上角按钮打开抽屉*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
            mDrawer,
            mToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        /*处理左侧抽屉的选择*/
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_receive_package:
                startActivity(new Intent(this, AddNewPackageActivity.class));
                break;
            case R.id.nav_send_package:
                startActivity(new Intent(MainActivity.this, SendNewPackageActivity.class));
                break;
            case R.id.nav_find_network:
                startActivity(new Intent(MainActivity.this, NetworkSearchActivity.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_share:
                break;
        }
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(PackageTrafficSearchResult item) {
        Intent intent = new Intent(this, TrackDetailActivity.class);
        intent.putExtra("package_number", item.getNumber());
        startActivity(intent);
    }
}
