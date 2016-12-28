package com.enihsyou.shane.packagetracker.model;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Spinner;
import com.enihsyou.shane.packagetracker.async_tasks.FetchAreaTask;

import java.util.ArrayList;
import java.util.List;
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

    public void populate(){
        if (nexts.size() > 0) return;
        try {
            new FetchAreaTask(this).execute(this.code).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.e(TAG, String.format("populate: 获取%s地区列表被中断", name), e);
        }
    }

    public AsyncTask<String, Void, List<NetworkCityResult>> populate(Spinner spinner) {
        if (nexts.size() > 0) return null;
        return new FetchAreaTask(spinner, this).execute(this.code);
    }
}
