package com.enihsyou.shane.packagetracker.adapter;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.fragment.PackageTrafficsFragment.OnListFragmentInteractionListener;
import com.enihsyou.shane.packagetracker.helper.OnDownloadCallback;
import com.enihsyou.shane.packagetracker.holder.PackageHolder;
import com.enihsyou.shane.packagetracker.model.PackageTrafficSearchResult;
import com.enihsyou.shane.packagetracker.model.Packages;

import java.util.List;

/**
 * 能够展示一个 {@link PackageTrafficSearchResult} ，
 * 并且可以回调指定的 {@link OnListFragmentInteractionListener}的{@link RecyclerView.Adapter}
 */
public class PackageTrafficsRecyclerViewAdapter
    extends RecyclerView.Adapter<PackageHolder> implements OnDownloadCallback {
    private static final String TAG = "PackageTrafficsRecycler";

    private final List<PackageTrafficSearchResult> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Fragment mActivity;

    public PackageTrafficsRecyclerViewAdapter(Fragment activity, List<PackageTrafficSearchResult> items,
        OnListFragmentInteractionListener listener) {
        mActivity = activity;
        mValues = items;
        mListener = listener;
    }

    @Override
    public PackageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.traffic_header_card, parent, false);
        return new PackageHolder(parent.getContext(), view, this);
    }

    @Override
    public void onBindViewHolder(final PackageHolder holder, int position) {
        PackageTrafficSearchResult item = mValues.get(position);
        holder.bindItem(item);

        holder.getViewRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClicked(holder.getItem());
                }
            }
        });
        holder.getViewRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    mListener.onItemLongPressed(holder.getItem());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void remove(PackageTrafficSearchResult item, int position) {
        Packages.removeTraffic(item); //移除数据堆里的
        mValues.remove(position); //移除适配器里的
        notifyItemRemoved(position); //动画
    }

    public List<PackageTrafficSearchResult> getValues() {
        return mValues;
    }

    public void undoRemove(PackageTrafficSearchResult item, int position) {
        Packages.addTraffic(item, position); //加进数据堆里 按顺序
        mValues.add(position, item); //移除适配器里的
        notifyItemInserted(position); //动画
    }

    @Override
    public void SetBitmapCallBack(final Bitmap bitmap, final ImageView view) {
        mActivity.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setImageBitmap(bitmap);
            }
        });
    }
}
