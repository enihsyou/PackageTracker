package com.enihsyou.shane.packagetracker.async_task;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.activity.NeedLocationActivity;
import com.enihsyou.shane.packagetracker.helper.GoogleFetcher;
import com.enihsyou.shane.packagetracker.model.CurrentLocationResult;

import java.io.IOException;
import java.util.Arrays;

/**
 * 反向解析地理地址
 */
public class FetchLocationTask extends AsyncTask<Double, Void, CurrentLocationResult> {
    private static final String TAG = "FetchLocationTask";
    private NeedLocationActivity mActivity;
    private GoogleFetcher fetcher;

    public FetchLocationTask(NeedLocationActivity activity) {
        fetcher = new GoogleFetcher();
        mActivity = activity;
    }

    @Override
    protected CurrentLocationResult doInBackground(Double... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("参数有两个 " + Arrays.toString(params));
        }
        String lat = String.valueOf(params[0]);
        String lng = String.valueOf(params[1]);

        try {
            return fetcher.locationResult(lat, lng);
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: 网络错误？反向解析地理信息失败", e);
            Snackbar.make(mActivity.getCurrentFocus(), R.string.network_error, Snackbar.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(CurrentLocationResult currentLocationResult) {
        if (currentLocationResult == null) {
            Log.i(TAG, "onPostExecute: 失败 地理反向解析 获得空结果");
            return;
        }
        Log.d(TAG, "onPostExecute: Google定位返回结果 " + currentLocationResult);
        Log.d(TAG, "onPostExecute: Google定位详细信息 " + currentLocationResult.getResults());
        mActivity.updateLocationSelection(currentLocationResult);
    }
}
