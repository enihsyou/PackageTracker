package com.enihsyou.shane.packagetracker.model;

import java.util.List;
import java.util.Locale;

/**
 * 输入框输入快递单号时的自动完成提示结果
 */
public class CompanyAutoSearchResult {
    private String number; //单号
    private List<CompanyEachAutoSearch> companies; //响应公司

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
