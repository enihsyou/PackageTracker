package com.enihsyou.shane.packagetracker.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.enihsyou.shane.packagetracker.holder.NetworkHolder;
import com.enihsyou.shane.packagetracker.model.NetworkSearchResult;

import java.util.List;

public class NetworkListViewAdapter extends BaseListViewAdapter<NetworkSearchResult.NetworkNetList>{
    private static final String TAG = "NetworkListViewAdapter";
    private List<NetworkSearchResult.NetworkNetList> items;

    public NetworkListViewAdapter(Activity context, int resource, List<NetworkSearchResult.NetworkNetList> objects) {
        super(context, resource);
        mActivity = context;
        mResource = resource;
        items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NetworkHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            convertView = inflater.inflate(mResource, parent, false);

            viewHolder = new NetworkHolder((CardView) convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NetworkHolder) convertView.getTag();
        }
        viewHolder.bindItem(items.get(position));
        return convertView;
    }
}
