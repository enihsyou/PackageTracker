package com.enihsyou.shane.packagetracker.model;

import java.util.ArrayList;

public class Area extends Place {
    private String fullName;
    public Area(String name, String code, ArrayList<Place> nexts) {
        super(name, code, nexts);
    }

    public Area(String name, String code, ArrayList<Place> nexts, String fullName) {
        super(name, code, nexts);
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
    public String getFirst(){
        return fullName.split("-")[0];
    }
    public String getSecond(){
        return fullName.split("-")[1];
    }
    public void populate() {
        this.nexts = null;
    }
}
