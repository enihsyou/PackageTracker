package com.enihsyou.shane.packagetracker;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

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
}
