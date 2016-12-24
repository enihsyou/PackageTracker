package com.enihsyou.shane.packagetracker.model;

import android.content.Context;
import android.widget.LinearLayout;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存用户添加的跟踪列表
 */
public class Packages {
    private static List<PackageTrafficSearchResult> packages;

    private Packages() {
        packages = new ArrayList<>();
    }

    public static List<PackageTrafficSearchResult> getPackages(Context context) {
        if (packages == null) new Packages();
        return packages;
    }

    public static void addTraffic(PackageTrafficSearchResult result) {
        getPackages().add(result);
    }

    private static List<PackageTrafficSearchResult> getPackages() {
        if (packages == null) new Packages();
        return packages;
    }

    public static void removeTraffic(PackageTrafficSearchResult result) {
        getPackages().remove(result);
    }

    public static LinearLayout getCard(int position,
        LinearLayout cardContainer) {
        PackageTrafficSearchResult searchResult = getOne(position);
        return Kuaidi100Fetcher.generateCard(searchResult, cardContainer);
    }

    public static PackageTrafficSearchResult getOne(int position) {
        try {
            return getPackages().get(position);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static boolean isDuplicated(PackageTrafficSearchResult result) {
        for (PackageTrafficSearchResult aPackage : packages) {
            if (result.equals(aPackage)) return true;
            if (aPackage.getNumber().equals(result.getNumber())
                && aPackage.getCompanyCode().equals(result.getCompanyCode())) {
                if (result.getLastTime().after(aPackage.getLastTime())) { return false; }
            }
        }
        return false;
    }
}
