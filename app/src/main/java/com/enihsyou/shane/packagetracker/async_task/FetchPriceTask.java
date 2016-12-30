package com.enihsyou.shane.packagetracker.async_task;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.activity.SendPackageActivity;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.model.PriceSearchResult;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.util.Arrays;

/**
 * 获取包裹运费信息
 */
public class FetchPriceTask extends AsyncTask<String, Void, PriceSearchResult> implements
    Kuaidi100Fetcher.SetImage{
    private static final String TAG = "FetchPriceTask";
    private final Kuaidi100Fetcher fetcher;
    private final SendPackageActivity mActivity;

    public FetchPriceTask(SendPackageActivity activity) {
        fetcher = new Kuaidi100Fetcher();
        mActivity = activity;
    }

    @Override
    protected PriceSearchResult doInBackground(String... params) {
        if (params.length != 4) {
            throw new IllegalArgumentException("参数有四个 " + Arrays.toString(params));
        }
        String locationSendCode = params[0];
        String locationReceiveCode = params[1];
        String street = params[2];
        String weight = params[3];
        try {
            return fetcher.priceResult(locationSendCode, locationReceiveCode, street, weight, 1);
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: 网络错误？获取价格失败", e);
            Snackbar.make(mActivity.getCurrentFocus(), R.string.network_error, Snackbar.LENGTH_SHORT).show();
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
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
            final String telephone = entry.getPriceInfo().getTelephone();

            String Code = entry.getPriceInfo().getCode();
            HttpUrl logoUrl = null;
            if (!Code.trim().isEmpty()) {
                logoUrl = Kuaidi100Fetcher.buildCompanyLogoUrl(Code, ".gif");
            }

            CardView card =
                (CardView) mActivity.getLayoutInflater().inflate(R.layout.card_price, gridLayout, false);

            final ImageView logo = (ImageView) card.findViewById(R.id.logo);
            final TextView companyNameText = (TextView) card.findViewById(R.id.company_name);
            final TextView totalPriceText = (TextView) card.findViewById(R.id.price);
            final TextView productTypeText = (TextView) card.findViewById(R.id.express_type);
            final TextView telephoneText = (TextView) card.findViewById(R.id.phone_number);

            fetcher.DownloadImage(this, logoUrl, logo);
            companyNameText.setText(companyName.trim());
            totalPriceText.setText(String.format("%s元", totalPrice.trim()));
            productTypeText.setText(productType == null ? "普通快递" : productType.trim());
            if (telephone == null) {
                telephoneText.setText("");
            } else {
                telephoneText.setText(telephone.trim());// TODO: 2016/12/30 处理多个电话号码的情况
                telephoneText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BottomSheetDialog sheetDialog = new BottomSheetDialog(mActivity);
                        @SuppressLint("InflateParams") View view =
                            mActivity.getLayoutInflater().inflate(R.layout.bottom_sheet_phonenumber, null);
                        TextView number = (TextView) view.findViewById(R.id.phone_number);
                        Button call = (Button) view.findViewById(R.id.make_call);
                        Button copy = (Button) view.findViewById(R.id.copy_text);
                        number.setText(telephone);
                        call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent makePhoneCall = new Intent(Intent.ACTION_DIAL);
                                makePhoneCall.setData(Uri.parse("tel:" + telephone));
                                Log.d(TAG, "makePhoneCall: Dial " + telephone);
                                mActivity.startActivity(makePhoneCall);
                            }
                        });
                        copy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ClipboardManager clipboard =
                                    (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("phoneNumber", telephone);
                                clipboard.setPrimaryClip(clip);
                            }
                        });
                        sheetDialog.setContentView(view);
                        sheetDialog.show();
                    }
                });
            }
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
