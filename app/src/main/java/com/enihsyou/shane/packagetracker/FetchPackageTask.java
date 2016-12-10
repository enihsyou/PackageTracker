package com.enihsyou.shane.packagetracker;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.io.IOException;

class FetchPackageTask extends AsyncTask<String, Void, PackageTrafficSearchResult> {
    private static final String TAG = "FetchPackageTask";

    private AddNewPackageActivity mAddNewPackageActivity;
    private Kuaidi100Fetcher fetcher;

    FetchPackageTask(AddNewPackageActivity addNewPackageActivity) {
        mAddNewPackageActivity = addNewPackageActivity;
        fetcher = new Kuaidi100Fetcher();
    }

    @Override
    protected PackageTrafficSearchResult doInBackground(String... params) {
        if (params.length != 2) return null;

        String queryNumber = params[0];
        String queryCompany = params[1];
        if (!"".equals(queryNumber) && !"".equals(queryCompany)) {
            try {
                return fetcher.packageResult(queryNumber, queryCompany);
            } catch (IOException ignored) {}// FIXME: 2016/12/9 错误提示
        }
        return null;
    }

    @Override
    protected void onPostExecute(PackageTrafficSearchResult packageTrafficSearchResult) {
        if (packageTrafficSearchResult == null) { // 如果获取不到正确的结果
            /*设置输入框检查器*/
            mAddNewPackageActivity.mNumberEditWrapper.setError(mAddNewPackageActivity
                    .getResources().getString(R.string.wrong_package_number));

            return;
        }
        /*先收起键盘*/
        mAddNewPackageActivity.hideKeyboard();
        View detailCard = fetcher.generateCard(packageTrafficSearchResult,
                mAddNewPackageActivity.mCardContainer, LayoutInflater.from(mAddNewPackageActivity));
        Log.d(TAG,
                String.format("onPostExecute: 成功解析快递信息 %s,有%d条记录",
                        packageTrafficSearchResult.getNumber(),
                        packageTrafficSearchResult.getTraffics().size()));

        mAddNewPackageActivity.mCardContainer.addView(detailCard);
    }

}
