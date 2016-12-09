package com.enihsyou.shane.packagetracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import java.io.IOException;
import java.util.ArrayList;

public class AddNewPackageActivity extends AppCompatActivity {
    private static final String TAG = "AddNewPackageActivity";

    private EditText mNumberEdit;
    private Spinner mSpinner;
    private Button mConform;
    private Button mTakePicture;

    private ArrayAdapter<CompanyEachAutoSearch> spinnerAdapter;
    private ArrayList<CompanyEachAutoSearch> spinnerItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_package);

        mNumberEdit = (EditText) findViewById(R.id.package_number_input);
        mSpinner = (Spinner) findViewById(R.id.company_spinner);
        mConform = (Button) findViewById(R.id.button_conform);
        mTakePicture = (Button) findViewById(R.id.button_take_picture);

        spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerItems);

        mNumberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                new FetchCompanyTask(s.toString()).execute();
            }
        });
        mNumberEdit.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    new FetchPackageTask(mNumberEdit.getText().toString(),
                            mSpinner.getSelectedItem().toString()).execute();
                    return true;
                }
                return false;
            }
        });
        mSpinner.setAdapter(spinnerAdapter);
        mConform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchPackageTask(mNumberEdit.getText().toString(),
                        mSpinner.getSelectedItem().toString()).execute();
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
            Log.i(TAG, "onPostExecute: " + companyAutoSearchResult.getCompanies().size());
            spinnerAdapter.clear();
            spinnerAdapter.addAll(companyAutoSearchResult.getCompanies());
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
            Log.i(TAG,
                    "onPostExecute: Result size: " + packageTrafficSearchResult
                            .getTraffics()
                            .size());
            for (PackageEachTraffic eachTraffic : packageTrafficSearchResult.getTraffics()) {
                Log.i(TAG, "onPostExecute: Result: " + eachTraffic.getContext());
            }
        }
    }
}
