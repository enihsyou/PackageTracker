package com.enihsyou.shane.packagetracker.model;

import com.enihsyou.shane.packagetracker.enums.CompanyCodeString;
import com.enihsyou.shane.packagetracker.enums.StatusString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * 搜索 快递跟踪进度 的结果
 */
public class PackageTrafficSearchResult implements Serializable {
    private String company; //公司代号
    private String number; //快递单号
    private int status; //快递状态
    private ArrayList<PackageEachTraffic> traffics;
    private Date lastTime; //最后动态时间

    public PackageTrafficSearchResult() {
        company = "testCompanyCode";
        number = "1234567890";
        status = 0;
        traffics = new ArrayList<>();
        lastTime = null;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getLastTime() {
        refreshLastSeenTime();
        return lastTime;
    }

    private void refreshLastSeenTime() {
        /*更新最后时间*/
        if (lastTime == null) {
            lastTime = traffics.get(traffics.size() - 1).getTime();
        } else {
            PackageEachTraffic traffic = traffics.get(traffics.size() - 1);
            Date trafficTime = traffic.getTime();
            if (trafficTime.after(lastTime)) lastTime = trafficTime;
        }
    }

    public String getCompanyCode() {
        return company;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "单号: %s，公司: %s，状态: %s",
            getNumber(), getCompanyString(), getStatus());
    }

    public String getNumber() {
        return number;
    }

    public String getCompanyString() {
        String name;
        try {
            name = Enum.valueOf(CompanyCodeString.class, company).toString();
        } catch (IllegalArgumentException e) {
            name = company;
        }
        return name;
    }

    public StatusString getStatus() {
        return StatusString.values()[status];
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ArrayList<PackageEachTraffic> getTraffics() {
        return traffics;
    }

    public void setTraffics(ArrayList<PackageEachTraffic> traffics) {
        this.traffics = traffics;
        refreshLastSeenTime();
    }
}
