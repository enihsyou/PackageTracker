package com.enihsyou.shane.packagetracker.model;

import java.util.List;
import java.util.Locale;

public class CompanyAutoSearchResult {
    private String number;
    private List<CompanyEachAutoSearch> companies;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<CompanyEachAutoSearch> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyEachAutoSearch> companies) {
        this.companies = companies;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "单号: %s，%d个结果", number, companies.size());
    }
}
