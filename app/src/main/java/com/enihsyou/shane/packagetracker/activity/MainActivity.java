package com.enihsyou.shane.packagetracker.activity;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.adapter.PackageTrafficsRecyclerViewAdapter;
import com.enihsyou.shane.packagetracker.adapter.SectionsPagerAdapter;
import com.enihsyou.shane.packagetracker.fragment.PackageTrafficsFragment;
import com.enihsyou.shane.packagetracker.model.PackageTrafficSearchResult;
import com.enihsyou.shane.packagetracker.model.Packages;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener,
    PackageTrafficsFragment.OnListFragmentInteractionListener {
    public static final String PACKAGE_NUMBER = "PACKAGE_NUMBER";
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

        mFab = (FabSpeedDial) findViewById(R.id.fab_main);
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
                        startActivity(new Intent(MainActivity.this, SearchNetworkActivity.class));
                        break;
                    case R.id.action_chop_hands:
                        launchTaobao();
                        break;
                    default:
                        Snackbar.make(getCurrentFocus(),
                            "发生什么了 我是谁 我在哪", Snackbar.LENGTH_SHORT)
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


        new TapTargetSequence(this)
            .targets(
                TapTarget.forToolbarNavigationIcon(mToolbar, "这是侧边抽屉，你也可以向右滑动打开它")
                // TapTarget.forToolbarMenuItem(mToolbar, R.id.menu_search, "搜索")
            ).listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                Snackbar.make(getCurrentFocus(), "完成教程", Snackbar.LENGTH_SHORT).setAction("再来一遍", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Oops!")
                    .setMessage("你跳过了教程")
                    .setPositiveButton("跳过", null)
                    .setNegativeButton("继续", null).show();
                TapTargetView.showFor(dialog,
                    TapTarget.forView(dialog.getButton(DialogInterface.BUTTON_POSITIVE), "Uh oh!",
                        String.format(Locale.getDefault(), "你跳过了第%d步", lastTarget.id()))
                        .cancelable(false)
                        .tintTarget(false), new TapTargetView.Listener() {
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);
                            dialog.dismiss();
                        }
                    });
            }
        }).start();
    }

    private void launchTaobao() {
        Intent taobao = new Intent(Intent.ACTION_VIEW);
        taobao.setClassName("com.taobao.taobao", "com.taobao.tao.welcome.Welcome");
                        /*检查是否安装了淘宝*/
        List<ResolveInfo> activities =
            getPackageManager().queryIntentActivities(taobao, PackageManager.GET_ACTIVITIES);
        if (activities.size() > 0) {
            Log.v(TAG, "onMenuItemSelected: 找到 " + activities + " 启动淘宝App");
            startActivity(taobao);
        } else {
            /*如果没有安装*/
            Toast.makeText(MainActivity.this, "没有找到淘宝应用，打开网页", Toast.LENGTH_SHORT).show();
            Intent web = new Intent(Intent.ACTION_VIEW);
            web.setData(Uri.parse("https://www.taobao.com/"));
            startActivity(web);
        }
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
        getMenuInflater().inflate(R.menu.menu_search, menu);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        /*处理左侧抽屉的选择*/
        switch (item.getItemId()) {
            case R.id.nav_receive_package:
                startActivity(new Intent(this, AddNewPackageActivity.class));
                break;
            case R.id.nav_send_package:
                startActivity(new Intent(MainActivity.this, SendNewPackageActivity.class));
                break;
            case R.id.nav_find_network:
                startActivity(new Intent(MainActivity.this, SearchNetworkActivity.class));
                break;
            case R.id.nav_open_taobao:
                launchTaobao();
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClicked(PackageTrafficSearchResult item) {
        Intent intent = new Intent(this, TrackDetailActivity.class);
        intent.putExtra(PACKAGE_NUMBER, item.getNumber());
        startActivity(intent);
    }

    @Override
    public void onItemLongPressed(final PackageTrafficSearchResult item) {
        final int posPile = Packages.getPackages(this, null).indexOf(item); //堆里的位置
        final PackageTrafficsRecyclerViewAdapter adapter = (
            (PackageTrafficsFragment) mSectionsPagerAdapter.getItem(mTabLayout.getSelectedTabPosition())).getAdapter();

        final int posList = adapter.getValues().indexOf(item);//适配器里的位置
        Log.d(TAG, "onItemLongPressed: 当前选择Tab" + mTabLayout.getSelectedTabPosition());

        /*移除*/
        adapter.remove(item, posList);
        Log.d(TAG, "onItemLongPressed: 已移除" + item + "  " + posList);

        Snackbar.make(this.getCurrentFocus(), getString(R.string.item_has_been_removed, item.getNumber()), Snackbar.LENGTH_LONG).setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.undoRemove(item, posPile);
                Log.d(TAG, "onItemLongPressed: 已添加" + item + "  " + posPile);
            }
        }).show();
    }
}
