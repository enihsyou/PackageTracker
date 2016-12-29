package com.enihsyou.shane.packagetracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PriceSearchResult {
    @SerializedName("orderList")
    private List<Entry> orderList;

    public List<Entry> getPriceInfo() {
        return orderList;
    }

    public static class Entry {
        @SerializedName("priceInfo")
        private PriceInfo priceInfo;

        public PriceInfo getPriceInfo() {
            return priceInfo;
        }
    }

    public static class PriceInfo {
        @SerializedName("companyName")
        private String companyName; //公司名字
        @SerializedName("expressCode")
        private String expressCode; //公司代号
        @SerializedName("companyCode")
        private String companyCode;  //公司代号
        @SerializedName("tel")
        private String telephone;//联系电话
        @SerializedName("produceType")
        private String productType;//快递类型
        @SerializedName("totalPrice")
        private String totalPrice;//总价

        public String getCompanyName() {
            return companyName;
        }

        public String getCode() {
            if (expressCode!= null && !expressCode.trim().isEmpty())
                return expressCode;
            else if (companyCode!=null &&!companyCode.trim().isEmpty())
                return companyCode;
            return "";
        }

        public String getTelephone() {
            return telephone;
        }

        public String getProductType() {
            return productType;
        }

        public String getTotalPrice() {
            return totalPrice;
        }
    }
}

