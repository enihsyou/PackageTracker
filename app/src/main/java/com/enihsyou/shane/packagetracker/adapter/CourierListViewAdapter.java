package com.enihsyou.shane.packagetracker.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.enihsyou.shane.packagetracker.holder.CourierHolder;
import com.enihsyou.shane.packagetracker.model.CourierSearchResult;

import java.util.List;

public class CourierListViewAdapter
    extends BaseListViewAdapter<CourierSearchResult.Courier> implements CourierHolder.Callback{
    private static final String TAG = "NetworkListViewAdapter";
    private List<CourierSearchResult.Courier> items;

    public CourierListViewAdapter(Activity context, int resource, List<CourierSearchResult.Courier> objects) {
        super(context, resource);
        mActivity = context;
        mResource = resource;
        items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CourierHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            convertView = inflater.inflate(mResource, parent, false);

            viewHolder = new CourierHolder((CardView) convertView, this);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CourierHolder) convertView.getTag();
        }
        viewHolder.bindItem(items.get(position));
        return convertView;
    }

    @Override
    public void SetBitmapCallBack(final Bitmap bitmap, final ImageView view) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setImageBitmap(bitmap);
            }
        });
    }
}
