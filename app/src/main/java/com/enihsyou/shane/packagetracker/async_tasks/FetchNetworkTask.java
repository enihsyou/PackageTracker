package com.enihsyou.shane.packagetracker.async_tasks;

import android.os.AsyncTask;
import android.util.Log;
import com.enihsyou.shane.packagetracker.model.NetworkSearchResult;

public class FetchNetworkTask extends AsyncTask<String ,Void, NetworkSearchResult>{
    private static final String TAG = "FetchNetworkTask";

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
    }
}
