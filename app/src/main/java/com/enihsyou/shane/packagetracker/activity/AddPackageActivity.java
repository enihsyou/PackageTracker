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
import com.enihsyou.shane.packagetracker.async_task.FetchCompanyTask;
import com.enihsyou.shane.packagetracker.async_task.FetchPackageTask;
import com.enihsyou.shane.packagetracker.enums.CompanyCodeString;
import com.enihsyou.shane.packagetracker.model.CompanyEachAutoSearch;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;

public class AddPackageActivity extends AppCompatActivity {
    private static final String TAG = "AddPackageActivity";
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
                Log.v(TAG, "afterTextChanged: 在单号框输入字符 " + s.toString());
                new FetchCompanyTask(AddPackageActivity.this).execute(s.toString());
            }
        });
        mNumberEdit.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.v(TAG, "onEditorAction: 点击键盘上的确认键");
                    new FetchPackageTask(AddPackageActivity.this)
                        .execute(mNumberEdit.getText().toString(),
                            ((CompanyEachAutoSearch) mSpinner.getSelectedItem()).getCompanyCode());
                    return true;
                }
                return false;
            }
        });

        /*设置下拉选项框*/
        //添加所有快递公司列表到下拉框
        for (CompanyCodeString codeToString : CompanyCodeString.values()) {
            CompanyEachAutoSearch e = new CompanyEachAutoSearch();
            e.setCompanyCode(codeToString.name());
            spinnerItems.add(e);
        }
        //添加视图适配器
        spinnerAdapter = new ArrayAdapter<>(this,
            R.layout.spinner_dropdown_item,
            spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);

        /*设置确认按钮的监听器*/
        mConform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "onClick: 按下面板确认按钮");
                if (mNumberEdit.getText().toString().isEmpty()) {
                    mNumberEditWrapper.setError(getResources().getString(R.string.wrong_package_number));
                }
                new FetchPackageTask(AddPackageActivity.this)
                    .execute(mNumberEdit.getText().toString(),
                        ((CompanyEachAutoSearch) mSpinner.getSelectedItem()).getCompanyCode());
            }
        });

        /*拍照按钮*/
        final IntentIntegrator intent = new IntentIntegrator(this);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = captureImage.resolveActivity(getPackageManager()) != null;
        mTakePicture.setEnabled(canTakePhoto);
        mTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.initiateScan();
                // startActivityForResult(intent, 0);
                mTakePicture.setEnabled(false); //现在不可用
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
