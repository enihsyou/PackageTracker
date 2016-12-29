package com.enihsyou.shane.packagetracker.model;

import com.enihsyou.shane.packagetracker.enums.EnumCompanyCodeString;

/**
 * 从API获取的由快递单号推测的快递公司信息
 */
public class CompanyEachAutoSearch {
    private String companyCode;
    private String numberPrefix;
    private String numberCount;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getNumberPrefix() {
        return numberPrefix;
    }

    public void setNumberPrefix(String numberPrefix) {
        this.numberPrefix = numberPrefix;
    }

    public String getNumberCount() {
        return numberCount;
    }

    public void setNumberCount(String numberCount) {
        this.numberCount = numberCount;
    }

    @Override
    public String toString() {
        String name;
        try {
            name = Enum.valueOf(EnumCompanyCodeString.class, companyCode).toString();
        } catch (IllegalArgumentException e) {
            name = companyCode;
        }
        return name;
    }
}
