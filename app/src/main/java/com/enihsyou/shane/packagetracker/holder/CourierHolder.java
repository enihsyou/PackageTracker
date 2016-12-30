package com.enihsyou.shane.packagetracker.holder;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.async_task.FetchCourierPhoneTask;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.helper.OnDownloadCallback;
import com.enihsyou.shane.packagetracker.model.CourierSearchResult;
import okhttp3.HttpUrl;

import java.util.concurrent.ExecutionException;

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

    @SuppressWarnings("Duplicates")
    public void bindItem(CourierSearchResult.Courier entry) {
        String companyName = entry.getCompanyName();
        String companyCode = entry.getCompanyCode();
        String name = entry.getCourierName();
        String workArea = entry.getWorkArea();
        String telephone = entry.getCourierTel();
        String url = entry.getLogoUrl();
        final String guid = entry.getGuid();

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
                final Context context = v.getContext();
                BottomSheetDialog sheetDialog = new BottomSheetDialog(context);
                @SuppressLint("InflateParams") View view =
                    LayoutInflater.from(context).inflate(R.layout.bottom_sheet_phonenumber, null);
                TextView number = (TextView) view.findViewById(R.id.phone_number);
                Button call = (Button) view.findViewById(R.id.make_call);
                Button copy = (Button) view.findViewById(R.id.copy_text);
                String detailPhone = null;
                try {
                    detailPhone =
                        new FetchCourierPhoneTask(v).execute(guid).get().getCourier().getCourierTel();
                } catch (InterruptedException | ExecutionException e) {
                    Log.e(TAG, "onClick: 中断？", e);
                }
                if (detailPhone != null) {
                    number.setText(detailPhone);
                    final String finalDetailPhone = detailPhone;
                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent makePhoneCall = new Intent(Intent.ACTION_DIAL);
                            makePhoneCall.setData(Uri.parse("tel:" + finalDetailPhone));
                            Log.d(TAG, "makePhoneCall: Dial " + finalDetailPhone);
                            context.startActivity(makePhoneCall);
                        }
                    });
                    copy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ClipboardManager clipboard =
                                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("phoneNumber", finalDetailPhone);
                            clipboard.setPrimaryClip(clip);
                        }
                    });
                    sheetDialog.setContentView(view);
                    sheetDialog.show();
                }
            }
        });
    }

    @Override
    public void SetBitmap(final Bitmap bitmap, final ImageView view) {
        mOnDownloadCallback.SetBitmapCallBack(bitmap, view);
    }
}
