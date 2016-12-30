package com.enihsyou.shane.packagetracker.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.adapter.PackageTrafficsRecyclerViewAdapter;
import com.enihsyou.shane.packagetracker.enums.StatusString;
import com.enihsyou.shane.packagetracker.model.PackageTrafficSearchResult;
import com.enihsyou.shane.packagetracker.model.Packages;

/**
 * 展示所有跟踪中的包裹的列表
 * <p/>
 * 包含这个fragment的Activity必须实现{@link OnListFragmentInteractionListener}接口
 */
public class PackageTrafficsFragment extends Fragment {
    private static final String PACKAGE_TYPE = "PACKAGE_TYPE";
    private OnListFragmentInteractionListener mListener;
    private PackageTrafficsRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private StatusString mPackageType;
    private ImageView mEmptyView;

    public static PackageTrafficsFragment newInstance(StatusString type) {
        PackageTrafficsFragment fragment = new PackageTrafficsFragment();
        Bundle args = new Bundle();

        args.putSerializable(PACKAGE_TYPE, type);

        fragment.setArguments(args);
        return fragment;
    }

    public PackageTrafficsRecyclerViewAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPackageType = (StatusString) getArguments().getSerializable(PACKAGE_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_packagetraffics_list, container, false);
        /*设置适配器*/
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_package_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mEmptyView = (ImageView) view.findViewById(R.id.empty_view);

        mAdapter =
            new PackageTrafficsRecyclerViewAdapter(this,
                Packages.getPackages(getActivity(), mPackageType), mListener, mPackageType);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
        if (mAdapter.getItemCount() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * 这个接口必须被包含这个Fragment的Activity给实现，
     * 这样才能让这个Fragment和他的Activity或者其他Fragment进行交流。
     */
    public interface OnListFragmentInteractionListener {
        void onItemClicked(PackageTrafficSearchResult item);

        void onItemLongPressed(PackageTrafficSearchResult item);
    }
}
