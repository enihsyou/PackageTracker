package com.enihsyou.shane.packagetracker.model;

import android.content.Context;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Packages {
    private static List<PackageTrafficSearchResult> packages;

    private Packages() {
        packages = new ArrayList<>();
        packages.add(new PackageTrafficSearchResult());
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
}
