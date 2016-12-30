package com.enihsyou.shane.packagetracker.async_task;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.model.CourierDetailSearchResult;

import java.io.IOException;
import java.util.Arrays;

public class FetchCourierPhoneTask extends AsyncTask<String, Void, CourierDetailSearchResult> {
    private static final String TAG = "FetchCourierTask";
    private View mRootView;
    private Kuaidi100Fetcher fetcher;

    public FetchCourierPhoneTask(View rootView) {
        mRootView = rootView;
        fetcher = new Kuaidi100Fetcher();
    }

    @Override
    protected CourierDetailSearchResult doInBackground(String... params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("参数有一个 " + Arrays.toString(params));
        }
        String guid = params[0];
        try {
            return fetcher.courierDetailResult(guid);
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: 网络错误？获取快递员详细信息失败", e);
            Snackbar.make(mRootView, R.string.network_error, Snackbar.LENGTH_SHORT).show();
        }
        return null;
    }

    // @Override
    // protected void onPostExecute(CourierDetailSearchResult courierSearchResult) {
    //     if (courierSearchResult == null) {
    //         Log.i(TAG, "onPostExecute: 失败 查询快递员详细信息 获得空结果");
    //         return;
    //     }
    //     String tel = courierSearchResult.getCourier().getCourierTel();
    //     Log.d(TAG, "onPostExecute: 成功 查询快递员详细信息 获得号码: " + tel);
    //     CourierListAdapter adapter =
    //         new CourierListAdapter(mActivity, R.layout.card_courier, courierSearchResult.getCouriers());
    //     mActivity.setListViewAdapter(adapter);
    //     adapter.setItems(courierSearchResult.getCouriers());
    // }
}
