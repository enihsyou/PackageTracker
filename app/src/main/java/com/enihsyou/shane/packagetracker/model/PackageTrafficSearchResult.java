package com.enihsyou.shane.packagetracker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 搜索 快递跟踪进度 的结果
 */
public class PackageTrafficSearchResult {
    private String company;
    private String number;
    private int status;
    private List<PackageEachTraffic> traffics;

    public PackageTrafficSearchResult() {
        company = "testCompanyCode";
        number = "1234567890";
        status = 0;
        traffics = new ArrayList<>();
    }

    public String getCompanyCode() {
        return company;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "单号: %s，公司: %s，状态: %d",
                getNumber(), getCompanyString(), getStatus());
    }

    public String getCompanyString() {
        String name;
        try {
            name = Enum.valueOf(EnumCompanyCodeString.class, company).toString();
        } catch (IllegalArgumentException e) {
            name = company;
        }
        return name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusString() {
        String name;
        try {
            name = EnumStatusString.values()[status].toString();
        } catch (IllegalArgumentException e) {
            name = String.valueOf(status);
        }
        return name;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public List<PackageEachTraffic> getTraffics() {
        return traffics;
    }

    public void setTraffics(List<PackageEachTraffic> traffics) {
        this.traffics = traffics;
    }
}
