package com.enihsyou.shane.packagetracker.model;

import java.util.ArrayList;

public class Province extends Place {
    private boolean directControlled;

    public Province(String name, String code, ArrayList<? extends Place> nexts) {
        super(name, code, nexts);
    }

    public Province(String name, String code, ArrayList<? extends Place> nexts, boolean directControlled) {
        super(name, code, nexts);
        this.directControlled = directControlled;
    }

    public boolean isDirectControlled() {
        return directControlled;
    }

    public void populate() {
        ArrayList<City> nexts = new ArrayList<>();
        for (int i = 0; i < cities.size(); i++) {
            int cityCode = cities.keyAt(i);
            String cityName = cities.get(cityCode);
            String s = String.valueOf(cityCode);
            if (s.startsWith(code.substring(0, 2))) {
                nexts.add(new City(cityName, s, null, directControlled));
            }
        }
        this.nexts = nexts;
    }
}
