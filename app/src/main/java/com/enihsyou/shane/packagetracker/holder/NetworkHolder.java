package com.enihsyou.shane.packagetracker.holder;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.model.NetworkSearchResult;

public class NetworkHolder extends BaseHolder<NetworkSearchResult.NetworkNetList> {
    private static final String TAG = "NetworkHolder";
    private TextView mSiteNameText;
    private TextView mCompanyNameText;
    private TextView mAddressText;
    private TextView mPhoneText;

    public NetworkHolder(CardView card) {
        mSiteNameText = (TextView) card.findViewById(R.id.site_name);
        mCompanyNameText = (TextView) card.findViewById(R.id.company_name);
        mAddressText = (TextView) card.findViewById(R.id.address);
        mPhoneText = (TextView) card.findViewById(R.id.phone_number);

        mPhoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: click on " + ((TextView) v).getText().toString());
            }
        });
    }

    @SuppressWarnings("Duplicates")
    public void bindItem(NetworkSearchResult.NetworkNetList entry) {
        String companyName = entry.getCompanyName();
        String companyCode = entry.getCompanyCode();
        String name = entry.getName();
        String address = entry.getAddress();
        final String telephone = entry.getTelephone();

        mSiteNameText.setText(name);
        mCompanyNameText.setText(companyName);
        mAddressText.setText(address);
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
                number.setText(telephone);
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent makePhoneCall = new Intent(Intent.ACTION_DIAL);
                        makePhoneCall.setData(Uri.parse("tel:" + telephone));
                        Log.d(TAG, "makePhoneCall: Dial " + telephone);
                        context.startActivity(makePhoneCall);
                    }
                });
                copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboard =
                            (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("phoneNumber", telephone);
                        clipboard.setPrimaryClip(clip);
                    }
                });
                sheetDialog.setContentView(view);
                sheetDialog.show();
            }
        });
    }
}
