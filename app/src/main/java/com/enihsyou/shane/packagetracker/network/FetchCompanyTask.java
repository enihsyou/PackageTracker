package com.enihsyou.shane.packagetracker.network;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import com.enihsyou.shane.packagetracker.activity.AddNewPackageActivity;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.model.CompanyAutoSearchResult;
import com.enihsyou.shane.packagetracker.model.CompanyEachAutoSearch;

import java.io.IOException;
import java.util.List;

public class FetchCompanyTask extends AsyncTask<String, Void, CompanyAutoSearchResult> {
    private AddNewPackageActivity mActivity;
    private Kuaidi100Fetcher fetcher;

    public FetchCompanyTask(AddNewPackageActivity addNewPackageActivity) {
        mActivity = addNewPackageActivity;
        fetcher = new Kuaidi100Fetcher();
    }

    @Override
    protected CompanyAutoSearchResult doInBackground(String... params) {
        if (params.length != 1) throw new IllegalArgumentException("参数有一个");

        String queryNumber = params[0];
        if (!"".equals(queryNumber)) {
            try {
                return fetcher.companyResult(queryNumber);
            } catch (IOException ignored) {}// FIXME: 2016/12/9 错误提示
        }
        return null;
    }

    @Override
    protected void onPostExecute(CompanyAutoSearchResult companyAutoSearchResult) {
        if (companyAutoSearchResult == null) return; // 如果获取失败

        ArrayAdapter<CompanyEachAutoSearch> spinnerAdapter =
            mActivity.getSpinnerAdapter();

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
