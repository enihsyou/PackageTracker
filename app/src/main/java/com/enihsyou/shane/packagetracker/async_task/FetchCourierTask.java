package com.enihsyou.shane.packagetracker.async_task;

import android.os.AsyncTask;
import android.util.Log;
import com.enihsyou.shane.packagetracker.activity.SearchNetworkActivity;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.model.CourierSearchResult;

import java.io.IOException;
import java.util.Arrays;

public class FetchCourierTask extends AsyncTask<String, Void, CourierSearchResult> {
    private static final String TAG = "FetchCourierTask";
    private SearchNetworkActivity mActivity;
    private Kuaidi100Fetcher fetcher;

    public FetchCourierTask(SearchNetworkActivity activity) {
        mActivity = activity;
        fetcher = new Kuaidi100Fetcher();
    }

    @Override
    protected CourierSearchResult doInBackground(String... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("参数有两个 " + Arrays.toString(params));
        }
        String area = params[0];
        String keyword = params[1];
        try {
            return fetcher.courierResult(area, keyword);
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: 网络错误？", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(CourierSearchResult courierSearchResult) {
        if (courierSearchResult == null) {
            Log.i(TAG, "onPostExecute: 失败 查询快递员 获得空结果");
            return;
        }
        Log.d(TAG, "onPostExecute: 成功 查询快递员 获得数量: " + courierSearchResult.getCouriers().size());
    }
}
