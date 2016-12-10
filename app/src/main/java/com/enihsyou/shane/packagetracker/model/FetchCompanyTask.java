package com.enihsyou.shane.packagetracker.model;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import com.enihsyou.shane.packagetracker.AddNewPackageActivity;

import java.io.IOException;
import java.util.List;

public class FetchCompanyTask extends AsyncTask<String, Void, CompanyAutoSearchResult> {
    private AddNewPackageActivity mAddNewPackageActivity;
    private Kuaidi100Fetcher fetcher;

    public FetchCompanyTask(AddNewPackageActivity addNewPackageActivity) {
        mAddNewPackageActivity = addNewPackageActivity;
        fetcher = new Kuaidi100Fetcher();
    }

    @Override
    protected CompanyAutoSearchResult doInBackground(String... params) {
        if (params.length != 1) return null;

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
                mAddNewPackageActivity.getSpinnerAdapter();

        spinnerAdapter.clear(); //先清除，再添加
        List<CompanyEachAutoSearch> companies = companyAutoSearchResult.getCompanies();
        if (companies.isEmpty()) { //如果没有匹配，添加一个空的
            CompanyEachAutoSearch tempResult = new CompanyEachAutoSearch();
            tempResult.setCompanyCode("none");
            spinnerAdapter.add(tempResult);
        } else {
            spinnerAdapter.addAll(companies);
        }
    }
}
