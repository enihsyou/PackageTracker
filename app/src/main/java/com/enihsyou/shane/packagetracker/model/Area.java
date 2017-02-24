package com.enihsyou.shane.packagetracker.model;

import android.text.TextUtils;

import java.util.ArrayList;

public class Area extends Place {
    private String fullName;
    private String[] splitFullName;

    public Area(String name, String code, ArrayList<Place> nexts) {
        super(name, code, nexts);
    }


    public Area(String name, String code, ArrayList<Place> nexts, String fullName, boolean directControlled) {
        super(name, code, nexts);
        splitFullName = fullName.split(",");
        this.fullName = TextUtils.join("-", splitFullName);
        this.directControlled = directControlled;
    }

    public String getFullName() {
        return fullName;
    }

    public String getFirst() {
        return splitFullName[0];
    }

    public String getSecond() {
        return splitFullName[1];
    }

    public void populate() {
        this.nexts = null;
    }
}
