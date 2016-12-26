package com.enihsyou.shane.packagetracker.network;

import android.os.AsyncTask;
import android.util.Log;
import com.enihsyou.shane.packagetracker.activity.SendNewPackageActivity;
import com.enihsyou.shane.packagetracker.helper.GoogleFetcher;
import com.enihsyou.shane.packagetracker.model.CurrentLocationResult;

import java.io.IOException;

public class FetchLocationTask extends AsyncTask<Double, Void, CurrentLocationResult> {
    private static final String TAG = "FetchLocationTask";
    private SendNewPackageActivity mActivity;
    private GoogleFetcher fetcher;

    public FetchLocationTask(SendNewPackageActivity activity) {
        fetcher = new GoogleFetcher();
        mActivity = activity;
    }

    @Override
    protected CurrentLocationResult doInBackground(Double... params) {
        if (params.length != 2) throw new IllegalArgumentException("参数有两个");
        String lat = String.valueOf(params[0]);
        String lng = String.valueOf(params[1]);

        try {
            return fetcher.locationResult(lat, lng);
        } catch (IOException ignored) {} // FIXME: 2016/12/21 错误提示
        return null;
    }

    @Override
    protected void onPostExecute(CurrentLocationResult currentLocationResult) {
        if (currentLocationResult == null) return;
        Log.d(TAG, "onPostExecute: Google定位返回结果 " + currentLocationResult);
        Log.d(TAG, "onPostExecute: Google定位详细信息 " + currentLocationResult.getResults());
        mActivity.updateLocationSelection(currentLocationResult);
    }
}
