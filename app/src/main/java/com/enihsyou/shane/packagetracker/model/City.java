package com.enihsyou.shane.packagetracker.model;

import android.util.Log;
import com.enihsyou.shane.packagetracker.async_tasks.FetchAreaTask;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class City extends Place {
    private static final String TAG = "City";

    public City(String name, String code, ArrayList<Place> nexts) {
        super(name, code, nexts);
    }

    public City(String name, String code, ArrayList<Place> nexts, boolean directControlled) {
        super(name, code, nexts);
        this.directControlled = directControlled;
    }


    public void populate(){
        if (nexts.size() > 0) return;
        try {
            new FetchAreaTask(this).execute(this.code).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, String.format("populate: 获取%s县区列表被中断", name), e);
        }
    }
}
