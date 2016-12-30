package com.enihsyou.shane.packagetracker.holder;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.model.NetworkSearchResult;

public class NetworkHolder extends BaseHolder<NetworkSearchResult.NetworkNetList>{
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
    public void bindItem(NetworkSearchResult.NetworkNetList entry) {
        String companyName = entry.getCompanyName();
        String companyCode = entry.getCompanyCode();
        String name = entry.getName();
        String address = entry.getAddress();
        String telephone = entry.getTelephone();

        mSiteNameText.setText(name);
        mCompanyNameText.setText(companyName);
        mAddressText.setText(address);
        mPhoneText.setText(telephone);
        mPhoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: click on " + ((TextView) v).getText().toString());
            }
        });
    }
}
