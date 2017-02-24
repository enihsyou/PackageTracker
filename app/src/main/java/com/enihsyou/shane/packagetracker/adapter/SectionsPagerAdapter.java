package com.enihsyou.shane.packagetracker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.enihsyou.shane.packagetracker.enums.StatusString;
import com.enihsyou.shane.packagetracker.fragment.PackageTrafficsFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 * 一个扩展了{@link FragmentPagerAdapter}的适配器，包含了三个视图页面
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private PackageTrafficsFragment Fragment1;
    private PackageTrafficsFragment Fragment2;
    private PackageTrafficsFragment Fragment3;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        Fragment1 = PackageTrafficsFragment.newInstance(null);
        Fragment2 = PackageTrafficsFragment.newInstance(StatusString.在途);
        Fragment3 = PackageTrafficsFragment.newInstance(StatusString.签收);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return Fragment1;
            case 1:
                return Fragment2;
            case 2:
                return Fragment3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
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
            default:
                return null;
        }
    }
}
