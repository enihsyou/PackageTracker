package com.enihsyou.shane.packagetracker.model;

import java.util.ArrayList;

import static com.enihsyou.shane.packagetracker.model.Places.PROVINCES;

public class Province extends Place {

    public Province(String name, String code, ArrayList<Place> nexts) {
        super(name, code, nexts);
    }

    public Province(String name, String code, ArrayList<Place> nexts, boolean directControlled) {
        super(name, code, nexts);
        this.directControlled = directControlled;
    }

    public void populate() {
        if (nexts.size() > 0) return;
        ArrayList<Place> nexts = new ArrayList<>();
        if (isDirectControlled()) {
            for (Province province : PROVINCES) {
                String cityCode = province.getCode();
                String cityName = province.getName();
                nexts.add(new City(cityName, cityCode, null, isDirectControlled()));
            }
        } else {
            for (int i = 0; i < cities.size(); i++) {
                int cityCode = cities.keyAt(i);
                String cityName = cities.get(cityCode);
                String s = String.valueOf(cityCode);
                if (s.startsWith(code.substring(0, 2))) {
                    nexts.add(new City(cityName, s, null));
                }
            }
        }
        this.nexts = nexts;
    }
}
