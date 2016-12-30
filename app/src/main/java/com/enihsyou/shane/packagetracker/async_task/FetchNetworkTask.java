package com.enihsyou.shane.packagetracker.async_task;

import android.os.AsyncTask;
import android.util.Log;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.activity.SearchNetworkActivity;
import com.enihsyou.shane.packagetracker.adapter.NetworkListViewAdapter;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.model.NetworkSearchResult;

import java.io.IOException;
import java.util.Arrays;

public class FetchNetworkTask extends AsyncTask<String, Void, NetworkSearchResult> {
    private static final String TAG = "FetchNetworkTask";
    private SearchNetworkActivity mActivity;
    private Kuaidi100Fetcher fetcher;

    public FetchNetworkTask(SearchNetworkActivity activity) {
        fetcher = new Kuaidi100Fetcher();
        mActivity = activity;
    }

    @Override
    protected NetworkSearchResult doInBackground(String... params) {
        if (params.length != 4) {
            throw new IllegalArgumentException("参数有四个 " + Arrays.toString(params));
        }
        String area = params[0];
        String keyword = params[1];
        String offset = params[2];
        String size = params[3];
        try {
            return fetcher.networkResult(area, keyword, offset, size);
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: 网络错误？", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(NetworkSearchResult networkSearchResult) {
        if (networkSearchResult == null) {
            Log.i(TAG, "onPostExecute: 失败 查询网点 获得空结果");
            return;
        }
        Log.d(TAG, "onPostExecute: 成功 查询网点 获得数量: " + networkSearchResult.getNetLists().size());
        NetworkListViewAdapter adapter =
            new NetworkListViewAdapter(mActivity, R.layout.network_card, networkSearchResult.getNetLists());
        mActivity.setListViewAdapter(adapter);
        adapter.setItems(networkSearchResult.getNetLists());
    }
}
