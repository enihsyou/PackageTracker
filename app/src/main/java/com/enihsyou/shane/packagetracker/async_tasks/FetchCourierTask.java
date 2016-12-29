package com.enihsyou.shane.packagetracker.async_tasks;

import android.os.AsyncTask;
import android.util.Log;
import com.enihsyou.shane.packagetracker.activity.SearchNetworkActivity;
import com.enihsyou.shane.packagetracker.model.CourierSearchResult;

public class FetchCourierTask extends AsyncTask<String, Void, CourierSearchResult> {
    private static final String TAG = "FetchCourierTask";
    private SearchNetworkActivity mActivity;

    public FetchCourierTask(SearchNetworkActivity activity) {mActivity = activity;}

    @Override
    protected CourierSearchResult doInBackground(String... params) {
        return null;
    }

    @Override
    protected void onPostExecute(CourierSearchResult courierSearchResult) {
        super.onPostExecute(courierSearchResult);
        Log.d(TAG, "onPostExecute: 成功 查询快递员 获得数量: " + courierSearchResult.getCouriers().size());
    }
}
