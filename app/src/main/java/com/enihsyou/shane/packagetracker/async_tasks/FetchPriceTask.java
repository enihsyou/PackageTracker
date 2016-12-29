package com.enihsyou.shane.packagetracker.async_tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.activity.SendNewPackageActivity;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.model.PriceSearchResult;
import okhttp3.HttpUrl;

import java.io.IOException;

public class FetchPriceTask extends AsyncTask<String, Void, PriceSearchResult> implements
    Kuaidi100Fetcher.SetImage {
    private static final String TAG = "FetchPriceTask";
    private Kuaidi100Fetcher fetcher;
    private SendNewPackageActivity mActivity;

    public FetchPriceTask(SendNewPackageActivity activity) {
        fetcher = new Kuaidi100Fetcher();
        mActivity = activity;
    }

    @Override
    protected PriceSearchResult doInBackground(String... params) {
        if (params.length != 4) throw new IllegalArgumentException("参数有四个");
        String locationSendCode = params[0];
        String locationReceiveCode = params[1];
        String street = params[2];
        String weight = params[3];
        try {
            return fetcher.priceResult(locationSendCode, locationReceiveCode, street, weight, 1);
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: 网络错误？", e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(PriceSearchResult priceSearchResult) {
        if (priceSearchResult == null) {
            Log.i(TAG, "onPostExecute: 失败 查询价格 获得空结果");
            return;
        }
        Log.d(TAG, "onPostExecute: 成功 查询价格 获得数量: " + priceSearchResult.getPriceInfo().size());
        GridLayout gridLayout = mActivity.getGridLayout();
        gridLayout.removeAllViews();
        gridLayout.setColumnCount(1);
        for (PriceSearchResult.Entry entry : priceSearchResult.getPriceInfo()) {
            String companyName = entry.getPriceInfo().getCompanyName();
            String totalPrice = entry.getPriceInfo().getTotalPrice();
            String productType = entry.getPriceInfo().getProductType();
            String telephone = entry.getPriceInfo().getTelephone();

            String Code = entry.getPriceInfo().getCode();
            HttpUrl logoUrl = null;
            if (!Code.trim().isEmpty()) {
                logoUrl = Kuaidi100Fetcher.buildCompanyLogoUrl(Code, ".gif");
            }

            CardView card =
                (CardView) mActivity.getLayoutInflater().inflate(R.layout.price_card, gridLayout, false);

            ImageView logo = (ImageView) card.findViewById(R.id.logo);
            TextView companyNameText = (TextView) card.findViewById(R.id.company_name);
            TextView totalPriceText = (TextView) card.findViewById(R.id.price);
            TextView productTypeText = (TextView) card.findViewById(R.id.express_type);
            TextView telephoneText = (TextView) card.findViewById(R.id.phone_number);

            fetcher.DownloadImage(this, logoUrl, logo);
            companyNameText.setText(companyName.trim());
            totalPriceText.setText(String.format("%s元", totalPrice.trim()));
            productTypeText.setText(productType == null ? "普通快递" : productType.trim());
            telephoneText.setText(telephone == null ? "" : telephone.trim());

            gridLayout.addView(card);
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
