package com.enihsyou.shane.packagetracker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.model.CompanyEachAutoSearch;
import com.enihsyou.shane.packagetracker.model.EnumCompanyCodeString;
import com.enihsyou.shane.packagetracker.network.FetchCompanyTask;
import com.enihsyou.shane.packagetracker.network.FetchPackageTask;

import java.util.ArrayList;

public class AddNewPackageActivity extends AppCompatActivity {
    private static final String TAG = "AddNewPackageActivity";
    ArrayAdapter<CompanyEachAutoSearch> spinnerAdapter;
    private LinearLayout mCardContainer;
    private TextInputLayout mNumberEditWrapper;
    private EditText mNumberEdit;
    private Spinner mSpinner;
    private Button mConform;
    private Button mTakePicture;
    private ArrayList<CompanyEachAutoSearch> spinnerItems = new ArrayList<>();

    public LinearLayout getCardContainer() {
        return mCardContainer;
    }

    public Spinner getSpinner() {
        return mSpinner;
    }

    public TextInputLayout getNumberEditWrapper() {
        return mNumberEditWrapper;
    }

    public ArrayAdapter<CompanyEachAutoSearch> getSpinnerAdapter() {
        return spinnerAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_package);

        /*视图赋值*/
        mCardContainer = (LinearLayout) findViewById(R.id.new_card_container);
        mNumberEditWrapper = (TextInputLayout) findViewById(R.id.package_number_input_wrapper);
        mNumberEdit = mNumberEditWrapper.getEditText();
        mSpinner = (Spinner) findViewById(R.id.company_spinner);
        mConform = (Button) findViewById(R.id.button_confirm);
        mTakePicture = (Button) findViewById(R.id.button_take_picture);

        /*设置输入框的动作监听器*/
        mNumberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: 在单号框输入字符 " + s.toString());
                new FetchCompanyTask(AddNewPackageActivity.this).execute(s.toString());
            }
        });
        mNumberEdit.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d(TAG, "onEditorAction: 点击键盘确认键");

                    new FetchPackageTask(AddNewPackageActivity.this)
                            .execute(mNumberEdit.getText().toString(),
                                    ((CompanyEachAutoSearch) mSpinner.getSelectedItem()).getCompanyCode());
                    return true;
                }
                return false;
            }
        });

        /*设置下拉选项框*/
        //添加所有快递公司列表到下拉框
        for (EnumCompanyCodeString codeToString : EnumCompanyCodeString.values()) {
            CompanyEachAutoSearch e = new CompanyEachAutoSearch();
            e.setCompanyCode(codeToString.name());
            spinnerItems.add(e);
        }
        //添加视图适配器
        spinnerAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_dropdown_item,
                spinnerItems);
        mSpinner.setAdapter(spinnerAdapter);

        /*设置确认按钮的监听器*/
        mConform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 按下面板确认按钮");
                if (mNumberEdit.getText().toString().equals("")){
                   mNumberEditWrapper.setError(getResources().getString(R.string.wrong_package_number));
                }
                new FetchPackageTask(AddNewPackageActivity.this)
                        .execute(mNumberEdit.getText().toString(),
                                ((CompanyEachAutoSearch) mSpinner.getSelectedItem()).getCompanyCode());
            }
        });

        /*拍照按钮*/
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = captureImage.resolveActivity(getPackageManager()) != null;
        mTakePicture.setEnabled(canTakePhoto);
        mTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, 0);
            }
        });
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view == null) return;
        InputMethodManager mInputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
        view.clearFocus();
    }
}
