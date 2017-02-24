package com.enihsyou.shane.packagetracker.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.helper.Kuaidi100Fetcher;
import com.enihsyou.shane.packagetracker.helper.OnDownloadCallback;
import com.enihsyou.shane.packagetracker.model.PackageTrafficSearchResult;
import com.enihsyou.shane.packagetracker.view.TrafficCardView;
import com.squareup.picasso.Picasso;

public class PackageHolder extends RecyclerView.ViewHolder implements Kuaidi100Fetcher.SetImage{
    private static final String TAG = "PackageHolder";
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

    private final Context mContext;
    private OnDownloadCallback mCallback;
    private TrafficCardView mViewRoot;
    private TextView mCompanyName;
    private TextView mPackageNumber;
    private ImageView mCompanyHead;
    private PackageTrafficSearchResult mItem;

    private static Kuaidi100Fetcher fetcher = new Kuaidi100Fetcher();
    public PackageHolder(Context context, View view, OnDownloadCallback callback) {
        super(view);
        mContext = context;
        mCallback = callback;
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
        // fetcher.DownloadImage(this, Kuaidi100Fetcher.buildCompanyLogoUrl(item.getCompanyCode()), mCompanyHead);
        Picasso.with(mContext).load(Kuaidi100Fetcher.buildCompanyLogoUrl(item.getCompanyCode()).toString())
            .placeholder(R.drawable.package_variant_closed)
            .into(mCompanyHead);

    }

    @Override
    public void SetBitmap(Bitmap bitmap, ImageView view) {
        mCallback.SetBitmapCallBack(bitmap, view);
    }
}
