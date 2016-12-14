package com.enihsyou.shane.packagetracker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.enihsyou.shane.packagetracker.fragment.PackageTrafficsFragment;
import com.enihsyou.shane.packagetracker.fragment.PlaceholderFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 * 一个扩展了{@link FragmentPagerAdapter}的适配器，包含了三个视图页面
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PackageTrafficsFragment.newInstance();
            case 1:
            case 2:
            default:
                return PlaceholderFragment.newInstance(position + 1);
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "全部";
            case 1:
                return "在途中";
            case 2:
                return "已签收";
        }
        return null;
    }
}
