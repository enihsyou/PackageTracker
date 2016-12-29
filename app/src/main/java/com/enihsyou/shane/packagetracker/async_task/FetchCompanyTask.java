package com.enihsyou.shane.packagetracker.async_task;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import com.enihsyou.shane.packagetracker.activity.AddNewPackageActivity;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.model.CompanyAutoSearchResult;
import com.enihsyou.shane.packagetracker.model.CompanyEachAutoSearch;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FetchCompanyTask extends AsyncTask<String, Void, CompanyAutoSearchResult> {
    private static final String TAG = "FetchCompanyTask";
    private AddNewPackageActivity mActivity;
    private Kuaidi100Fetcher fetcher;

    public FetchCompanyTask(AddNewPackageActivity addNewPackageActivity) {
        mActivity = addNewPackageActivity;
        fetcher = new Kuaidi100Fetcher();
    }

    @Override
    protected CompanyAutoSearchResult doInBackground(String... params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("参数有一个 " + Arrays.toString(params));
        }
        String queryNumber = params[0];
        if (queryNumber != null && !queryNumber.isEmpty()) {
            try {
                Log.v(TAG, "doInBackground: 启动网络服务获取公司列表 " + queryNumber);
                return fetcher.companyResult(queryNumber);
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: 网络错误？", e);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(CompanyAutoSearchResult companyAutoSearchResult) {
        if (companyAutoSearchResult == null) {
            Log.i(TAG, "onPostExecute: 失败 查询公司自动完成 获得空结果");
            return;
        }
        Log.d(TAG, "onPostExecute: 公司自动完成 " + companyAutoSearchResult);
        ArrayAdapter<CompanyEachAutoSearch> spinnerAdapter = mActivity.getSpinnerAdapter();

        spinnerAdapter.clear(); //先清除，再添加
        List<CompanyEachAutoSearch> companies = companyAutoSearchResult.getCompanies();
        if (companies.isEmpty()) { //如果没有匹配，添加一个空的
            CompanyEachAutoSearch tempResult = new CompanyEachAutoSearch();
            tempResult.setCompanyCode("无匹配");
            spinnerAdapter.add(tempResult);
        } else {
            spinnerAdapter.addAll(companies);
        }
    }
}
