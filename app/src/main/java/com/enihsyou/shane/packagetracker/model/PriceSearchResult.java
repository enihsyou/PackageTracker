package com.enihsyou.shane.packagetracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class PriceSearchResult {
    @SerializedName("orderList")
    private OrderList orderList;
    public List<PriceInfo> getPriceInfos(){
        return Arrays.asList(orderList.priceInfo);
    }

    static class OrderList {
        @SerializedName("priceInfo")
        private PriceInfo[] priceInfo;
    }

    static class PriceInfo {
        @SerializedName("companyName")
        private String companyName;
        @SerializedName("expressCode")
        private String expressCode;
        @SerializedName("tel")
        private String telephone;
        @SerializedName("produceType")
        private String productType;
        @SerializedName("totalPrice")
        private String totalPrice;

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

