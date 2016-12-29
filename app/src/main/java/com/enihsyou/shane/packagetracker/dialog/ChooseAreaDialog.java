package com.enihsyou.shane.packagetracker.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;
import com.enihsyou.shane.packagetracker.enums.DialogType;
import com.enihsyou.shane.packagetracker.model.Place;

import java.util.ArrayList;

public class ChooseAreaDialog extends DialogFragment {
    private static final String TAG = "ChooseAreaDialog";
    private static final String PLACE_LIST = "PLACE_LIST";
    private static final String TITLE = "TITLE";
    private static final String TYPE = "TYPE";
    private static final String POSITION = "POSITION";
    private ArrayList<Place> mPlaceList;
    private String mTitle;
    private OnChooseListener mListener;
    private DialogType mType;
    private int mPosition;

    /**
     * @param type      对话框的类型
     * @param title     对话框标题
     * @param position  当前选择的序号
     * @param placeList 内容列表
     *
     * @return 对话框的Fragment
     */
    public static ChooseAreaDialog newInstance(DialogType type, String title, int position, ArrayList<? extends Place> placeList) {
        Bundle args = new Bundle();
        args.putSerializable(TYPE, type);
        args.putString(TITLE, title);
        args.putInt(POSITION, position);
        args.putSerializable(PLACE_LIST, placeList);
        ChooseAreaDialog fragment = new ChooseAreaDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnChooseListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnChooseListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        mType = (DialogType) getArguments().getSerializable(TYPE);
        mPlaceList = (ArrayList<Place>) getArguments().getSerializable(PLACE_LIST);
        mPosition = getArguments().getInt(POSITION);
        mTitle = getArguments().getString(TITLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(mTitle);
        builder.setSingleChoiceItems(
            new ArrayAdapter<>(this.getActivity(),
                android.R.layout.select_dialog_singlechoice, mPlaceList), mPosition,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mListener.OnItemClick(ChooseAreaDialog.this, mType, which, mPlaceList.get(which));
                    /*关闭对话框*/
                    dialog.dismiss();
                }
            });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.OnDialogPositiveClick(ChooseAreaDialog.this);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.OnDialogNegativeClick(ChooseAreaDialog.this);
            }
        });
        return builder.create();
    }

    public interface OnChooseListener {
        void OnItemClick(DialogFragment dialog, DialogType type, int which, Place place);

        void OnDialogPositiveClick(DialogFragment dialog);

        void OnDialogNegativeClick(DialogFragment dialog);
    }
}
