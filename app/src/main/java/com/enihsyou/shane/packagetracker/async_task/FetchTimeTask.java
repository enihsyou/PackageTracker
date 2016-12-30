package com.enihsyou.shane.packagetracker.async_task;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.activity.SendPackageActivity;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.model.TimeSearchResult;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.util.Arrays;

/**
 * 获取快递时效信息
 */
public class FetchTimeTask extends AsyncTask<String, Void, TimeSearchResult> implements
    Kuaidi100Fetcher.SetImage {
    private static final String TAG = "FetchTimeTask";
    private final Kuaidi100Fetcher fetcher;
    private final SendPackageActivity mActivity;

    public FetchTimeTask(SendPackageActivity activity) {
        mActivity = activity;
        fetcher = new Kuaidi100Fetcher();
    }

    @Override
    protected TimeSearchResult doInBackground(String... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("参数有两个 " + Arrays.toString(params));
        }
        String from = params[0];
        String to = params[1];
        try {
            return fetcher.timeResult(from, to);
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: 网络错误？获取时效失败", e);
            Snackbar.make(mActivity.getCurrentFocus(), R.string.network_error, Snackbar.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(TimeSearchResult timeSearchResult) {
        if (timeSearchResult == null) {
            Log.i(TAG, "onPostExecute: 失败 查询时效 获得空结果");
            return;
        }
        Log.d(TAG, "onPostExecute: 成功 查询时效 获得数量: " + timeSearchResult.getEntries().size());
        GridLayout gridLayout = mActivity.getGridLayout();
        gridLayout.removeAllViews();
        gridLayout.setColumnCount(2);
        for (TimeSearchResult.Entry entry : timeSearchResult.getEntries()) {
            String companyName = entry.getCompanyName();
            String time = entry.getTime();
            HttpUrl logoUrl = HttpUrl.parse(entry.getCompanyLogoUrl());
            CardView card =
                (CardView) mActivity.getLayoutInflater().inflate(R.layout.card_time, mActivity.getGridLayout(), false);
            ImageView logo = (ImageView) card.findViewById(R.id.logo);
            TextView companyNameText = (TextView) card.findViewById(R.id.company_name);
            TextView timeText = (TextView) card.findViewById(R.id.time);

            fetcher.DownloadImage(this, logoUrl, logo);
            companyNameText.setText(companyName.trim());
            timeText.setText(time.trim());
            mActivity.getGridLayout().addView(card);
        }
    }

    @Override
    public void SetBitmap(final Bitmap bitmap, final ImageView view) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setImageBitmap(bitmap);
            }
        });
    }
}
