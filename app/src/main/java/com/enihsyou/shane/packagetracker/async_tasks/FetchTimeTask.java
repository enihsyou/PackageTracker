package com.enihsyou.shane.packagetracker.async_tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.model.TimeSearchResult;

import java.io.IOException;

public class FetchTimeTask extends AsyncTask<String, Void, TimeSearchResult> {
    private static final String TAG = "FetchTimeTask";
    private final Kuaidi100Fetcher fetcher;
    private final Activity mActivity;

    public FetchTimeTask(Activity activity) {
        mActivity = activity;
        fetcher = new Kuaidi100Fetcher();
    }

    @Override
    protected TimeSearchResult doInBackground(String... params) {
        if (params.length != 2) throw new IllegalArgumentException("参数有两个");
        String from = params[0];
        String to = params[1];
        try {
            return fetcher.timeResult(from, to);
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: 网络错误？", e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(TimeSearchResult timeSearchResult) {
        if (timeSearchResult == null) {
            Log.i(TAG, "onPostExecute: 失败 查询时效 获得空结果");
            return;
        }
        Log.d(TAG, "onPostExecute: 成功 查询时效 获得数量: " + timeSearchResult.getEntries().size());

    }
}
