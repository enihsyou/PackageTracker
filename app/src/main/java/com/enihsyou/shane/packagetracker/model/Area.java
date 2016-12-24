package com.enihsyou.shane.packagetracker.model;

import java.util.ArrayList;

public class Area extends Place {
    public Area(String name, String code, ArrayList<? extends Place> nexts) {
        super(name, code, nexts);
    }

    public void populate() {
        this.nexts = null;
    }
}
