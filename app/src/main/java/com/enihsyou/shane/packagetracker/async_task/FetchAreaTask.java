package com.enihsyou.shane.packagetracker.async_task;

import android.os.AsyncTask;
import android.util.Log;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.model.Area;
import com.enihsyou.shane.packagetracker.model.City;
import com.enihsyou.shane.packagetracker.model.NetworkCityResult;
import com.enihsyou.shane.packagetracker.model.Place;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FetchAreaTask extends AsyncTask<String, Void, List<NetworkCityResult>> {
    private static final String TAG = "FetchAreaTask";
    private Kuaidi100Fetcher fetcher;
    private City selectedItem;

    public FetchAreaTask(City selectedItem) {
        fetcher = new Kuaidi100Fetcher();
        this.selectedItem = selectedItem;
    }

    @Override
    protected List<NetworkCityResult> doInBackground(String... params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("参数有一个 " + Arrays.toString(params));
        }
        String queryNumber = params[0];
        if (queryNumber != null && !("00".equals(queryNumber)) && !queryNumber.isEmpty()) {
            try {
                Log.v(TAG, "doInBackground: 启动网络服务获取县区列表 " + queryNumber);
                List<NetworkCityResult> results = fetcher.networkCityResult(queryNumber);
                ArrayList<Place> nexts = new ArrayList<>();
                for (NetworkCityResult result : results) {
                    String name = result.getName();
                    String code = result.getCode();
                    String fullName = result.getFullName();
                    nexts.add(new Area(name, code, null, fullName, selectedItem.isDirectControlled()));
                }
                Collections.reverse(nexts);
                selectedItem.nexts = nexts;
                return results;
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: 网络错误？获取区县列表失败", e);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<NetworkCityResult> networkCityResults) {
        if (networkCityResults == null) {
            Log.i(TAG, "onPostExecute: 失败 查询第三级县区 获得空结果");
        }
    }
}
