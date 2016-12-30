package com.enihsyou.shane.packagetracker.model;

import android.content.Context;
import android.widget.LinearLayout;
import com.enihsyou.shane.packagetracker.enums.StatusString;
import com.enihsyou.shane.packagetracker.helper.DataBase;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * 保存用户添加的跟踪列表
 */
public class Packages implements Serializable {
    private static ArrayList<PackageTrafficSearchResult> packages;

    private Packages(Context context) {
        if (packages == null) {
            packages = DataBase.getInstance(context).readPackages();
        }
        if (packages == null) { packages = new ArrayList<>(); }
    }

    public static void setPackages(ArrayList<PackageTrafficSearchResult> packages) {
        Packages.packages = packages;
    }

    public static ArrayList<PackageTrafficSearchResult> getPackages(Context context, StatusString type) {
        if (packages == null) new Packages(context);
        if (type != null) {
            ArrayList<PackageTrafficSearchResult> result = new ArrayList<>();
            for (PackageTrafficSearchResult aPackage : packages) {
                if (aPackage.getStatus().equals(type)) { result.add(aPackage); }
            }
            return result;
        }
        return packages;
    }

    public static void addTraffic(Context context, PackageTrafficSearchResult result) {
        packages.add(result);
        DataBase.getInstance(context).writePackages(Packages.getPackages(context));
    }

    public static ArrayList<PackageTrafficSearchResult> getPackages(Context context) {
        if (packages == null) new Packages(context);
        return packages;
    }

    public static void removeTraffic(Context context, PackageTrafficSearchResult result) {
        packages.remove(result);
        DataBase.getInstance(context).writePackages(Packages.getPackages(context));
    }

    public static LinearLayout getCard(int position,
        LinearLayout cardContainer) {
        PackageTrafficSearchResult searchResult = getOne(position);
        return Kuaidi100Fetcher.generateCard(searchResult, cardContainer);
    }

    public static PackageTrafficSearchResult getOne(int position) {
        try {
            return packages.get(position);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static boolean isDuplicated(PackageTrafficSearchResult result) {
        for (PackageTrafficSearchResult aPackage : packages) {
            if (result.equals(aPackage)) return true;
            if (aPackage.getNumber().equals(result.getNumber())
                && aPackage.getCompanyCode().equals(result.getCompanyCode())) {
                return !result.getLastTime().after(aPackage.getLastTime());
            }
        }
        return false;
    }

    public static void addTraffic(Context context, PackageTrafficSearchResult item, int position) {
        packages.add(position, item);
        DataBase.getInstance(context).writePackages(Packages.getPackages(context));
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(packages);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        packages = (ArrayList<PackageTrafficSearchResult>) stream.readObject();
    }
}
