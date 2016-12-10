package com.enihsyou.shane.packagetracker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.enihsyou.shane.packagetracker.R;

public class TrafficCardDetailLayout extends LinearLayout {
    private TextView mDetailDatetime;
    private TextView mDetailContext;

    public TrafficCardDetailLayout(Context context) {
        super(context);
    }

    public TrafficCardDetailLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TrafficCardDetailLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    private void initViews() {
        mDetailDatetime = (TextView) findViewById(R.id.detail_datetime);
        mDetailContext = (TextView) findViewById(R.id.detail_context);
    }

    public void setDatetime(String datetime) {
        mDetailDatetime.setText(datetime);
    }

    public void setDetailContext(String context) {
        mDetailContext.setText(context);
    }
}
