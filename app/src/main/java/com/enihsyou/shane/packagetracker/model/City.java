package com.enihsyou.shane.packagetracker.model;

import com.enihsyou.shane.packagetracker.network.FetchAreaTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class City extends Place {
    public City(String name, String code, ArrayList<? extends Place> nexts) {
        super(name, code, nexts);
    }

    public void populate() throws IOException {
        NetworkCityResult[] results = new NetworkCityResult[0];
        try {
            results = new FetchAreaTask().execute(this.code).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        ArrayList<Area> nexts = new ArrayList<>();
        for (NetworkCityResult result : results) {
            String name = result.getName();
            String code = result.getCode();
            nexts.add(new Area(name, code, null));
        }
        this.nexts = nexts;
    }
}
