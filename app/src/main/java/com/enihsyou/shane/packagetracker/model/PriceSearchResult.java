package com.enihsyou.shane.packagetracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PriceSearchResult {
    @SerializedName("orderList")
    private List<Entry> orderList;

    public List<Entry> getPriceInfo() {
        return orderList;
    }

    static class Entry {
        @SerializedName("priceInfo")
        private PriceInfo priceInfo;
    }

    static class PriceInfo {
        @SerializedName("companyName")
        private String companyName; //公司名字
        @SerializedName("expressCode")
        private String expressCode; //公司代号
        @SerializedName("tel")
        private String telephone;//联系电话
        @SerializedName("produceType")
        private String productType;//快递类型
        @SerializedName("totalPrice")
        private String totalPrice;//总价

        public String getCompanyName() {
            return companyName;
        }

        public String getExpressCode() {
            return expressCode;
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

