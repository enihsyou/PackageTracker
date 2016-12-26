package com.enihsyou.shane.packagetracker.model;

import java.util.ArrayList;

public class Area extends Place {
    private String fullName;
    public Area(String name, String code, ArrayList<? extends Place> nexts) {
        super(name, code, nexts);
    }

    public Area(String name, String code, ArrayList<? extends Place> nexts, String fullName) {
        super(name, code, nexts);
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void populate() {
        this.nexts = null;
    }
}
