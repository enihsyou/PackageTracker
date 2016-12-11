package com.enihsyou.shane.packagetracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.enihsyou.shane.packagetracker.PackageTrafficsFragment.OnListFragmentInteractionListener;
import com.enihsyou.shane.packagetracker.model.PackageTrafficSearchResult;

import java.util.List;

/**
 * 能够展示一个 {@link PackageTrafficSearchResult} ，
 * 并且可以回调指定的 {@link OnListFragmentInteractionListener}的{@link RecyclerView.Adapter}
 * TODO: Replace the implementation with code for your data type.
 */
public class PackageTrafficsRecyclerViewAdapter
        extends RecyclerView.Adapter<PackageViewHolder> {

    private final List<PackageTrafficSearchResult> mValues;
    private final OnListFragmentInteractionListener mListener;

    public PackageTrafficsRecyclerViewAdapter(List<PackageTrafficSearchResult> items,
            OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public PackageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.traffic_header_card, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PackageViewHolder holder, int position) {
        PackageTrafficSearchResult item = mValues.get(position);
        holder.bindItem(item);

        holder.getViewRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.getItem());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

}
