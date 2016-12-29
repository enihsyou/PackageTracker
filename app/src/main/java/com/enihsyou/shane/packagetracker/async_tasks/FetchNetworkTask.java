package com.enihsyou.shane.packagetracker.async_tasks;

import android.os.AsyncTask;
import android.util.Log;
import com.enihsyou.shane.packagetracker.activity.SearchNetworkActivity;
import com.enihsyou.shane.packagetracker.model.NetworkSearchResult;

public class FetchNetworkTask extends AsyncTask<String ,Void, NetworkSearchResult>{
    private static final String TAG = "FetchNetworkTask";
    private SearchNetworkActivity mActivity;

    public FetchNetworkTask(SearchNetworkActivity activity) {
        mActivity = activity;
    }

    @Override
    protected NetworkSearchResult doInBackground(String... params) {

        return null;
    }

    @Override
    protected void onPostExecute(NetworkSearchResult networkSearchResult) {
        if (networkSearchResult == null) {
            Log.i(TAG, "onPostExecute: 失败 查询网点 获得空结果");
            return;
        }
        Log.d(TAG, "onPostExecute: 成功 查询网点 获得数量: " + networkSearchResult.getNetLists().size());
    }
}
