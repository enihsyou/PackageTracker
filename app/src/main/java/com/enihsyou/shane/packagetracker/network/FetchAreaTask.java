package com.enihsyou.shane.packagetracker.network;

import android.os.AsyncTask;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.model.NetworkCityResult;

import java.io.IOException;

public class FetchAreaTask extends AsyncTask<String ,Void, NetworkCityResult[]>{
    private Kuaidi100Fetcher fetcher;

    public FetchAreaTask() {
        fetcher = new Kuaidi100Fetcher();
    }

    @Override
    protected NetworkCityResult[] doInBackground(String... params) {
        if (params.length != 1) throw new IllegalArgumentException("参数有一个");

        String queryNumber = params[0];
        if (queryNumber != null && !queryNumber.isEmpty()) {
            try {
                return fetcher.networkCityResult(queryNumber);
            } catch (IOException ignored) {}// FIXME: 2016/12/9 错误提示
        }
        return null;
    }
}
