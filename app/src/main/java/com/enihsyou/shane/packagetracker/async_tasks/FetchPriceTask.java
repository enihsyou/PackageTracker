package com.enihsyou.shane.packagetracker.async_tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.model.PriceSearchResult;

import java.io.IOException;

public class FetchPriceTask extends AsyncTask<String, Void, PriceSearchResult> {
    private static final String TAG = "FetchPriceTask";
    private Kuaidi100Fetcher fetcher;
    private Activity mActivity;

    public FetchPriceTask(Activity activity) {
        fetcher = new Kuaidi100Fetcher();
        mActivity = activity;
    }

    @Override
    protected PriceSearchResult doInBackground(String... params) {
        if (params.length != 4) throw new IllegalArgumentException("参数有四个");
        String locationSendCode = params[0];
        String locationReceiveCode = params[1];
        String street = params[2];
        String weight = params[3];
        try {
            return fetcher.priceResult(locationSendCode, locationReceiveCode, street, weight);
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: 网络错误？", e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(PriceSearchResult priceSearchResult) {
        if (priceSearchResult == null) {
            Log.i(TAG, "onPostExecute: 失败 查询价格 获得空结果");
            return;
        }
        Log.d(TAG, "onPostExecute: 成功 查询价格 获得数量: " + priceSearchResult.getPriceInfo().size());
    }
}
