package com.enihsyou.shane.packagetracker.async_tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.model.Area;
import com.enihsyou.shane.packagetracker.model.City;
import com.enihsyou.shane.packagetracker.model.NetworkCityResult;
import com.enihsyou.shane.packagetracker.model.Place;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FetchAreaTask extends AsyncTask<String, Void, List<NetworkCityResult>> {
    private static final String TAG = "FetchAreaTask";
    private Kuaidi100Fetcher fetcher;
    private Spinner spinner;
    private City selectedItem;
    // private int positon;

    public FetchAreaTask(Spinner spinner, City selectedItem) {
        fetcher = new Kuaidi100Fetcher();
        this.spinner = spinner;
        // this.positon = position;
        this.selectedItem = selectedItem;
    }

    public FetchAreaTask(City selectedItem) {
        fetcher = new Kuaidi100Fetcher();
        this.selectedItem = selectedItem;
    }

    @Override
    protected List<NetworkCityResult> doInBackground(String... params) {
        if (params.length != 1) throw new IllegalArgumentException("参数有一个");
        String queryNumber = params[0];
        if (queryNumber != null && !("00".equals(queryNumber)) && !queryNumber.isEmpty()) {
            try {
                List<NetworkCityResult> results = fetcher.networkCityResult(queryNumber);
                ArrayList<Area> nexts = new ArrayList<>();
                for (NetworkCityResult result : results) {
                    String name = result.getName();
                    String code = result.getCode();
                    String fullName = result.getFullName();
                    nexts.add(new Area(name, code, null, fullName));
                }
                Collections.reverse(nexts);
                selectedItem.nexts = nexts;
                return results;
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: 网络错误？", e);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<NetworkCityResult> networkCityResults) {
        if (networkCityResults == null) {
            Log.i(TAG, "onPostExecute: 失败 查询第三级地区 获得空结果");
            return;
        }
        /*如果传递了spinner，设置spinner的选项*/
        if (spinner == null) return;
        ArrayList<? extends Place> areas = selectedItem.nexts;
        // int originPosition = spinner.getSelectedItemPosition();
        ArrayAdapter<? extends Place> cityArrayAdapter =
            new ArrayAdapter<>(spinner.getContext(), R.layout.spinner_textview, areas);
        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(cityArrayAdapter);
        // spinner.setSelection(this.positon);
    }
}
