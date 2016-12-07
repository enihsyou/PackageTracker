package com.enihsyou.shane.packagetracker;

import java.util.List;

public class PackageTrafficSearchResult {
    private String company;
    private String number;
    private int status;
    private List<PackageEachTraffic> traffics;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public List<PackageEachTraffic> getTraffics() {
        return traffics;
    }

    public void setTraffics(List<PackageEachTraffic> traffics) {
        this.traffics = traffics;
    }
}
