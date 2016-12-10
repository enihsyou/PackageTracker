package com.enihsyou.shane.packagetracker.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.enihsyou.shane.packagetracker.R;
import com.enihsyou.shane.packagetracker.model.PackageEachTraffic;

import java.util.List;

public class TrafficCardView extends CardView {
    private static final String TAG = "TrafficCardView";
    private final LayoutInflater mInflater;
    private ImageView mCompanyHead;
    private TextView mPackageNumber;
    private TextView mCompanyName;
    private LinearLayout mDetailContainer;

    public TrafficCardView(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
    }

    public TrafficCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
    }

    public TrafficCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
    }

    /**
     * 在完成创建视图之后，再findViewByID，不然会有空指针错误
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    private void initViews() {
        mCompanyHead = (ImageView) findViewById(R.id.company_head_card_view);
        mPackageNumber = (TextView) findViewById(R.id.package_number_card_view);
        mCompanyName = (TextView) findViewById(R.id.company_name_card_view);
        mDetailContainer = (LinearLayout) findViewById(R.id.each_detail_container);
    }

    /**
     * @param companyName 公司名字，传入已经转义过的字符串
     */
    public void setCompanyName(String companyName) {
        mCompanyName.setText(companyName);
    }

    public void setPackageNumber(String packageNumber) {
        mPackageNumber.setText(packageNumber);
    }

    public void addTraffics(List<PackageEachTraffic> traffics) {
        for (PackageEachTraffic eachTraffic : traffics) {
            // 详细跟踪信息的根布局
            TrafficCardDetailLayout trafficLayout =
                    (TrafficCardDetailLayout) mInflater.inflate(R.layout.traffic_body,
                            mDetailContainer,
                            false);

            /*文字框赋值*/
            trafficLayout.setDatetime(eachTraffic.getTimeString());
            trafficLayout.setDetailContext(eachTraffic.getContext());

            mDetailContainer.addView(trafficLayout);
        }
    }

    public void setCompanyHead(Drawable drawable) {
        mCompanyHead.setImageDrawable(drawable);
    }
}
