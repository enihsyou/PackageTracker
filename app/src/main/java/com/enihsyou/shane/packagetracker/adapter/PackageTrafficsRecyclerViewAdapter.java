package com.enihsyou.shane.packagetracker.adapter;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.enums.StatusString;
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
    private final OnListFragmentInteractionListener mListener;
    private final StatusString mType;
    private final Fragment mActivity;
    private List<PackageTrafficSearchResult> mValues;

    public PackageTrafficsRecyclerViewAdapter(Fragment activity, List<PackageTrafficSearchResult> items,
        OnListFragmentInteractionListener listener, StatusString type) {
        mActivity = activity;
        mValues = items;
        mListener = listener;
        mType = type;
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

    /**
     * @param item    对象
     * @param posList 在适配器里的位置
     */
    public void remove(PackageTrafficSearchResult item, int posList) {
        Packages.removeTraffic(mActivity.getContext(), item); //移除数据堆里的
        mValues = Packages.getPackages(mActivity.getContext(), mType); //重新获取
        notifyItemRemoved(posList); //动画
    }

    public List<PackageTrafficSearchResult> getValues() {
        return mValues;
    }

    /**
     * @param item    对象
     * @param posList 在适配器里的位置
     * @param posPile 在数据堆里的位置
     */
    public void undoRemove(PackageTrafficSearchResult item, int posList, int posPile) {
        Packages.addTraffic(mActivity.getContext(), item, posPile); //加进数据堆里 按顺序
        mValues = Packages.getPackages(mActivity.getContext(), mType); //重新获取
        notifyItemInserted(posList); //动画
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
