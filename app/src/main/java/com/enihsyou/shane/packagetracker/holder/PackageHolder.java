package com.enihsyou.shane.packagetracker.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.model.PackageTrafficSearchResult;
import com.enihsyou.shane.packagetracker.view.TrafficCardView;

public class PackageHolder extends RecyclerView.ViewHolder {
    public TrafficCardView getViewRoot() {
        return mViewRoot;
    }

    public TextView getCompanyName() {
        return mCompanyName;
    }

    public TextView getPackageNumber() {
        return mPackageNumber;
    }

    public ImageView getCompanyHead() {
        return mCompanyHead;
    }

    public PackageTrafficSearchResult getItem() {
        return mItem;
    }

    private TrafficCardView mViewRoot;
    private TextView mCompanyName;
    private TextView mPackageNumber;
    private ImageView mCompanyHead;
    private PackageTrafficSearchResult mItem;

    public PackageHolder(View view) {
        super(view);
        TrafficCardView cardView = (TrafficCardView) view.findViewById(R.id.header_detail_container);
        mViewRoot = cardView;
        mCompanyName = cardView.getCompanyName();
        mPackageNumber = cardView.getPackageNumber();
        mCompanyHead = cardView.getCompanyHead();
    }

    public void bindItem(PackageTrafficSearchResult item) {
        mItem = item;
        mCompanyName.setText(item.getCompanyString());
        mPackageNumber.setText(item.getNumber());
    }
}
