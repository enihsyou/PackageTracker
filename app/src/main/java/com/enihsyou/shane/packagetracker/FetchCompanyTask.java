package com.enihsyou.shane.packagetracker;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

class FetchCompanyTask extends AsyncTask<String, Void, CompanyAutoSearchResult> {
    private AddNewPackageActivity mAddNewPackageActivity;
    private Kuaidi100Fetcher fetcher;

    FetchCompanyTask(AddNewPackageActivity addNewPackageActivity) {
        mAddNewPackageActivity = addNewPackageActivity;
        fetcher = new Kuaidi100Fetcher();
    }

    @Override
    protected CompanyAutoSearchResult doInBackground(String... params) {
        if (params.length!=1) return null;

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

        mAddNewPackageActivity.spinnerAdapter.clear(); //先清除，再添加
        List<CompanyEachAutoSearch> companies = companyAutoSearchResult.getCompanies();
        if (companies.isEmpty()) { //如果没有匹配，添加一个空的
            CompanyEachAutoSearch tempResult = new CompanyEachAutoSearch();
            tempResult.setCompanyCode("none");
            mAddNewPackageActivity.spinnerAdapter.add(tempResult);
        }
        else mAddNewPackageActivity.spinnerAdapter.addAll(companies);
    }
}
