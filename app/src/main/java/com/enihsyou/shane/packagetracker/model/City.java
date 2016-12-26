package com.enihsyou.shane.packagetracker.model;

import android.util.Log;
import com.enihsyou.shane.packagetracker.network.FetchAreaTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class City extends Place {
    private static final String TAG = "City";

    public City(String name, String code, ArrayList<? extends Place> nexts) {
        super(name, code, nexts);
    }

    public City(String name, String code, ArrayList<? extends Place> nexts, boolean directControlled) {
        super(name, code, nexts);
        this.directControlled = directControlled;
    }

    public void populate() throws IOException {
        if (isDirectControlled()) {
            Log.e(TAG, "populate: 在非直辖市下，用错方法");
            return;
        }
        try {
            NetworkCityResult[] results = new FetchAreaTask().execute(this.code).get();
            ArrayList<Area> nexts = new ArrayList<>();
            for (NetworkCityResult result : results) {
                String name = result.getName();
                String code = result.getCode();
                String fullName = result.getFullName();
                nexts.add(new Area(name, code, null, fullName));
            }
            Collections.reverse(nexts);
            this.nexts = nexts;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
