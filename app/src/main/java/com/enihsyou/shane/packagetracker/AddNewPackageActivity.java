package com.enihsyou.shane.packagetracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
                new FetchTask(s.toString()).execute();
            }
        });

        mSpinner.setAdapter(spinnerAdapter);
    }

    private class FetchTask extends AsyncTask<String, Void, CompanyAutoSearchResult> {
        private String queryNumber;
        private Kuaidi100Fetcher fetcher;

        public FetchTask(String queryNumber) {
            this.queryNumber = queryNumber;
            fetcher = new Kuaidi100Fetcher();
        }

        @Override
        protected CompanyAutoSearchResult doInBackground(String... params) {
            if (queryNumber != null) {
                try {
                    return fetcher.companyResult(queryNumber);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
}
