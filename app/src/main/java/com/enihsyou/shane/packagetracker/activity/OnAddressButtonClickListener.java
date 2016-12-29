package com.enihsyou.shane.packagetracker.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.enihsyou.shane.packagetracker.dialog.ChooseAreaDialog;
import com.enihsyou.shane.packagetracker.enums.DialogType;
import com.enihsyou.shane.packagetracker.model.Place;

import java.util.ArrayList;

public class OnAddressButtonClickListener implements View.OnClickListener {
    private static final String TAG = "OnAddressButtonClickListener";

    public void setPosition(int position) {
        mPosition = position;
    }

    private int mPosition;
    private final FragmentManager mManager;
    private final String mTitle;
    private final DialogType mType;
    private ArrayList<? extends Place> mList;

    public OnAddressButtonClickListener(AppCompatActivity activity, DialogType type, String title, ArrayList<? extends Place> list) {
        mManager = activity.getSupportFragmentManager();
        mType = type;
        mTitle = title;
        mList = list;
    }

    public void setList(ArrayList<? extends Place> list) {
        mList = list;
    }

    @Override
    public void onClick(View v) {
        ChooseAreaDialog dialog = ChooseAreaDialog.newInstance(mType, mTitle, mPosition, mList);
        dialog.show(mManager, String.format("选择框 %s", mTitle));
    }
}
