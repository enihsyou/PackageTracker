package com.enihsyou.shane.packagetracker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddNewPackageActivity extends AppCompatActivity {
    private static final String TAG = "AddNewPackageActivity";
    private LinearLayout mCardContainer;
    private TextInputLayout mNumberEditWrapper;
    private EditText mNumberEdit;
    private Spinner mSpinner;
    private Button mConform;
    private Button mTakePicture;

    private ArrayAdapter<CompanyEachAutoSearch> spinnerAdapter;
    private ArrayList<CompanyEachAutoSearch> spinnerItems = new ArrayList<>();
    private InputMethodManager mInputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_package);

        /*设置键盘*/
        mInputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        /*视图赋值*/
        mCardContainer = (LinearLayout) findViewById(R.id.new_card_container);
        mNumberEditWrapper = (TextInputLayout) findViewById(R.id.package_number_input_wrapper);
        mNumberEdit = (EditText) findViewById(R.id.package_number_input);
        mSpinner = (Spinner) findViewById(R.id.company_spinner);
        mConform = (Button) findViewById(R.id.button_conform);
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
                new FetchCompanyTask(s.toString()).execute();
            }
        });
        mNumberEdit.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d(TAG, "onEditorAction: 点击键盘确认键");

                    new FetchPackageTask(mNumberEdit.getText().toString(),
                            ((CompanyEachAutoSearch) mSpinner.getSelectedItem()).getCompanyCode()).execute();
                    return true;
                }
                return false;
            }
        });

        /*设置下拉选项框*/
        //添加所有快递公司列表到下拉框
        for (CompanyCodeToString codeToString : CompanyCodeToString.values()) {
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

                new FetchPackageTask(mNumberEdit.getText().toString(),
                        ((CompanyEachAutoSearch) mSpinner.getSelectedItem()).getCompanyCode()).execute();
            }
        });
    }

    private class FetchCompanyTask extends AsyncTask<String, Void, CompanyAutoSearchResult> {
        private String queryNumber;
        private Kuaidi100Fetcher fetcher;

        FetchCompanyTask(String queryNumber) {
            this.queryNumber = queryNumber;
            fetcher = new Kuaidi100Fetcher();
        }

        @Override
        protected CompanyAutoSearchResult doInBackground(String... params) {
            if (!"".equals(queryNumber)) {
                try {
                    return fetcher.companyResult(queryNumber);
                } catch (IOException ignored) {}// FIXME: 2016/12/9 错误提示
            }
            return null;
        }

        @Override
        protected void onPostExecute(CompanyAutoSearchResult companyAutoSearchResult) {
            if (companyAutoSearchResult == null) return; // 如果获取失败

            spinnerAdapter.clear(); //先清除，再添加
            List<CompanyEachAutoSearch> companies = companyAutoSearchResult.getCompanies();
            if (companies.isEmpty()) { //如果没有匹配，添加一个空的
                CompanyEachAutoSearch tempResult = new CompanyEachAutoSearch();
                tempResult.setCompanyCode("none");
                spinnerAdapter.add(tempResult);
            }
            spinnerAdapter.addAll(companies);
        }
    }

    private class FetchPackageTask
            extends AsyncTask<String, Void, PackageTrafficSearchResult> {
        private String queryNumber;
        private String queryCompany;
        private Kuaidi100Fetcher fetcher;


        FetchPackageTask(String queryNumber, String queryCompany) {
            this.queryNumber = queryNumber;
            this.queryCompany = queryCompany;
            fetcher = new Kuaidi100Fetcher();
        }

        @Override
        protected PackageTrafficSearchResult doInBackground(String... params) {
            if (!"".equals(queryNumber) && !"".equals(queryCompany)) {
                try {
                    return fetcher.packageResult(queryNumber, queryCompany);
                } catch (IOException ignored) {}// FIXME: 2016/12/9 错误提示
            }
            return null;
        }

        @Override
        protected void onPostExecute(PackageTrafficSearchResult packageTrafficSearchResult) {
            if (packageTrafficSearchResult == null) { // 如果获取不到正确的结果
                /*设置输入框检查器*/
                mNumberEditWrapper.setError(getResources().getString(R.string.wrong_package_number));

                return;
            }
            /*先收起键盘*/
            mInputMethodManager.hideSoftInputFromWindow(mNumberEdit.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);

            /*下面创建快递信息卡片*/
            // 用XML创建视图
            LayoutInflater layoutInflater = LayoutInflater.from(AddNewPackageActivity.this);
            // 卡片的根布局
            View detailCard = layoutInflater
                    .inflate(R.layout.card_package, mCardContainer, false);
            // 获得卡片部件，之后的每个小部件都添加到这张卡片里面
            CardView detailContainer = (CardView) detailCard.findViewById(R.id.detail_container);
            // 获得卡片里面的详细跟踪信息的展示布局，之后的每个详细跟踪信息都添加到这里面
            LinearLayout eachDetailContainer =
                    (LinearLayout) detailContainer.findViewById(R.id.each_detail_container);
            // 获得各个部件
            ImageView companyHead =
                    (ImageView) detailContainer.findViewById(R.id.company_head_card_view);
            TextView packageNumber =
                    (TextView) detailContainer.findViewById(R.id.package_number_card_view);
            TextView companyName =
                    (TextView) detailContainer.findViewById(R.id.company_name_card_view);
            // 设置CardView各个部件
            packageNumber.setText(packageTrafficSearchResult.getNumber());
            companyName.setText(CompanyCodeToString
                    .valueOf(packageTrafficSearchResult.getCompany())
                    .toString());
            for (PackageEachTraffic eachTraffic : packageTrafficSearchResult.getTraffics()) {
                // 详细跟踪信息的根布局
                View trafficLayout = layoutInflater
                        .inflate(R.layout.traffic_detail, eachDetailContainer, false);

                TextView detailDatetime =
                        (TextView) trafficLayout.findViewById(R.id.detail_datetime);
                TextView detailContext = (TextView) trafficLayout.findViewById(R.id.detail_context);
                /*文字框赋值*/
                detailDatetime.setText(eachTraffic.getTimeString());
                detailContext.setText(eachTraffic.getContext());

                eachDetailContainer.addView(trafficLayout);
            }

            mCardContainer.addView(detailCard);
        }
    }
}
