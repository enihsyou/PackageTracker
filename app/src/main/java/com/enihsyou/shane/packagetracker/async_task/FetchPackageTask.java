package com.enihsyou.shane.packagetracker.async_task;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.activity.AddPackageActivity;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.model.PackageTrafficSearchResult;
import com.enihsyou.shane.packagetracker.model.Packages;

import java.io.IOException;
import java.util.Arrays;

/**
 * 从快递单号和公司代码获得包裹信息
 */
public class FetchPackageTask extends AsyncTask<String, Void, PackageTrafficSearchResult> {
    private static final String TAG = "FetchPackageTask";

    private AddPackageActivity mActivity;
    private Kuaidi100Fetcher fetcher;

    public FetchPackageTask(AddPackageActivity activity) {
        mActivity = activity;
        fetcher = new Kuaidi100Fetcher();
    }

    @Override
    protected PackageTrafficSearchResult doInBackground(String... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("参数有两个 " + Arrays.toString(params));
        }
        String queryNumber = params[0];
        String queryCompany = params[1];
        if (queryNumber != null && !queryNumber.isEmpty() && queryCompany != null
            && !queryCompany.isEmpty()) {
            try {
                return fetcher.packageResult(queryNumber, queryCompany);
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: 网络错误？", e);
                Snackbar.make(mActivity.getCurrentFocus(), R.string.network_error, Snackbar.LENGTH_SHORT).show();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(PackageTrafficSearchResult searchResult) {
        if (searchResult == null) { // 如果获取不到正确的结果
            Log.i(TAG, "onPostExecute: 失败 查询包裹 获得空结果");
            /*设置输入框检查器*/
            mActivity.getNumberEditWrapper().setError(mActivity
                .getResources().getString(R.string.wrong_package_number));
            return;
        }
        Log.d(TAG, String.format("onPostExecute: 成功解析快递信息 %s,有%d条记录",
            searchResult.getNumber(),
            searchResult.getTraffics().size()));

        /*先收起键盘*/
        mActivity.hideKeyboard();
        /*移除之前的输入错误提示*/
        mActivity.getNumberEditWrapper().setError("");

        // 新建的两张卡片将添加到下面这个容器里面
        LinearLayout attachRoot = mActivity.getCardContainer();

        /*判断现在是否已经有卡片*/
        if (attachRoot.getChildAt(1) == null) { //如果之前没有添加过卡片
            Log.d(TAG, "onPostExecute: position1 is null, add new card");
        } else {
            attachRoot.removeViewAt(1); //之前添加过一个了，现在清除掉
            Log.d(TAG, "onPostExecute: 之前添加过一个卡片了，先移除，再添加");
        }

        // 上面一个输入内容的卡片 下面一个显示结果的卡片
        LinearLayout trafficHeader = (LinearLayout) LayoutInflater
            .from(mActivity)
            .inflate(R.layout.traffic_header_card, mActivity.getCardContainer(), false);
        /*创建下面的结果显示卡片*/
        Kuaidi100Fetcher.generateCard(searchResult, trafficHeader);

        /*将卡片添加到适合的位置*/
        attachRoot.addView(trafficHeader);
        /*将结果添加到跟踪列表*/
        /*判断单号是否重复*/
        if (Packages.isDuplicated(searchResult)) {
            Log.d(TAG, String.format("onPostExecute: 单号: %s 已经重复", searchResult.getNumber()));
            return;
        }
        Packages.addTraffic(mActivity, searchResult);
    }
}
