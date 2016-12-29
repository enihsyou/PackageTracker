package com.enihsyou.shane.packagetracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CourierSearchResult {
    @SerializedName("status")
    private int status;
    @SerializedName("coList")
    private List<Courier> couriers;

    public int getStatus() {
        return status;
    }

    public List<Courier> getCouriers() {
        return couriers;
    }

    public static class Courier {
        @SerializedName("companyName")
        private String companyName; //公司名字
        @SerializedName("companyCode")
        private String companyCode;//公司代码
        @SerializedName("courierName")
        private String courierName;//快递员名字
        @SerializedName("courierTel")
        private String courierTel;//快递员电话 带马
        @SerializedName("workArea")
        private String workArea;//工作区域
        @SerializedName("guid")
        private String guid;//快递员id
        @SerializedName("logo")
        private String logoUrl;//头像

        public String getWorkArea() {
            return workArea;
        }

        public String getCompanyName() {
            return companyName;
        }

        public String getCompanyCode() {
            return companyCode;
        }

        public String getCourierName() {
            return courierName;
        }

        public String getCourierTel() {
            return courierTel;
        }

        public String getGuid() {
            return guid;
        }

        public String getLogoUrl() {
            return logoUrl;
        }
    }
}
