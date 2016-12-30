package com.enihsyou.shane.packagetracker.holder;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.helper.OnDownloadCallback;
import com.enihsyou.shane.packagetracker.model.CourierSearchResult;
import okhttp3.HttpUrl;

public class CourierHolder extends BaseHolder<CourierSearchResult.Courier>
    implements Kuaidi100Fetcher.SetImage {
    private static final String TAG = "CourierHolder";
    private static final Kuaidi100Fetcher FETCHER = new Kuaidi100Fetcher();
    private ImageView mHeader;
    private TextView mCourierNameText;
    private TextView mCompanyNameText;
    private TextView mAreaText;
    private TextView mPhoneText;

    private OnDownloadCallback mOnDownloadCallback;

    public CourierHolder(CardView card, OnDownloadCallback listener) {
        mOnDownloadCallback = listener;

        mHeader = (ImageView) card.findViewById(R.id.header);
        mCourierNameText = (TextView) card.findViewById(R.id.courier_name);
        mCompanyNameText = (TextView) card.findViewById(R.id.company_name);
        mAreaText = (TextView) card.findViewById(R.id.area);
        mPhoneText = (TextView) card.findViewById(R.id.phone_number);

        mPhoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: click on " + ((TextView) v).getText().toString());
            }
        });
    }

    public void bindItem(CourierSearchResult.Courier entry) {
        String companyName = entry.getCompanyName();
        String companyCode = entry.getCompanyCode();
        String name = entry.getCourierName();
        String workArea = entry.getWorkArea();
        String telephone = entry.getCourierTel();
        String url = entry.getLogoUrl();
        String guid = entry.getGuid();

        if (!url.trim().isEmpty()) {
            HttpUrl logoUrl = Kuaidi100Fetcher.resolveRelativeUrl(url.trim());
            FETCHER.DownloadImage(this, logoUrl, mHeader);
        }
        mCourierNameText.setText(name);
        mCompanyNameText.setText(companyName);
        mAreaText.setText(workArea);
        mPhoneText.setText(telephone);
        mPhoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: click on " + ((TextView) v).getText().toString());

            }
        });
    }

    @Override
    public void SetBitmap(final Bitmap bitmap, final ImageView view) {
        mOnDownloadCallback.SetBitmapCallBack(bitmap, view);
    }
}
