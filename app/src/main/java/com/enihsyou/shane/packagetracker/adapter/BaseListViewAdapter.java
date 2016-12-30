package com.enihsyou.shane.packagetracker.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.enihsyou.shane.packagetracker.holder.BaseHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListViewAdapter<V> extends ArrayAdapter<BaseHolder> {
    private static final String TAG = "NetworkListViewAdapter";
    Activity mActivity;
    int mResource;
    List<V> items = new ArrayList<>();

    public BaseListViewAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void setItems(List<V> lists) {
        items = lists;
        notifyDataSetChanged();
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
