package com.enihsyou.shane.packagetracker.model;

import java.util.ArrayList;

public class Area extends Place {
    private String fullName;
    public Area(String name, String code, ArrayList<Place> nexts) {
        super(name, code, nexts);
    }


    public Area(String name, String code, ArrayList<Place> nexts, String fullName, boolean directControlled) {
        super(name, code, nexts);
        this.fullName = fullName;
        this.directControlled = directControlled;
    }

    public String getFullName() {
        return fullName;
    }
    public String getFirst(){
        return fullName.split(",")[0];
    }
    public String getSecond(){
        return fullName.split(",")[1];
    }
    public void populate() {
        this.nexts = null;
    }
}
