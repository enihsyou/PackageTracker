package com.enihsyou.shane.packagetracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 网点查询结果
 */
public class NetworkSearchResult {
    @SerializedName("status")
    private int status; // 是否成功
    @SerializedName("netList")
    private List<NetworkNetListResult> netLists; // 返回网点列表

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<NetworkNetListResult> getNetLists() {
        return netLists;
    }

    public void setNetLists(List<NetworkNetListResult> netLists) {
        this.netLists = netLists;
    }

    /**
     * 每个网点详细的查询结果
     */
    public static class NetworkNetListResult {
        private static transient Pattern HtmlTag = Pattern.compile("</?font.*?>");
        @SerializedName("id")
        private long id;
        @SerializedName("companyNumber")
        private String companyNumber; //快递公司代码
        @SerializedName("companyName")
        private String companyName; //快递公司名字
        @SerializedName("name")
        private String name; //服务站名字
        @SerializedName("linkman")
        private String linkMan; //工作人员名字
        @SerializedName("address")
        private String address; //地址
        @SerializedName("workArea")
        private String workArea; //派送范围
        @SerializedName("refuseArea")
        private String refuseArea; //不派送范围
        @SerializedName("detailText")
        private String detailText; //详细介绍
        @SerializedName("telOne")
        private String telephone; //一个电话

        /**
         * 清除API附带的HTML代码
         */
        public void cleanHtml() {
            this.name = HtmlTag.matcher(this.name).replaceAll("");
            this.detailText = HtmlTag.matcher(this.detailText).replaceAll("");
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getCompanyNumber() {
            return companyNumber;
        }

        public void setCompanyNumber(String companyNumber) {
            this.companyNumber = companyNumber;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLinkMan() {
            return linkMan;
        }

        public void setLinkMan(String linkMan) {
            this.linkMan = linkMan;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getWorkArea() {
            return workArea;
        }

        public void setWorkArea(String workArea) {
            this.workArea = workArea;
        }

        public String getRefuseArea() {
            return refuseArea;
        }

        public void setRefuseArea(String refuseArea) {
            this.refuseArea = refuseArea;
        }

        public String getDetailText() {
            return detailText;
        }

        public void setDetailText(String detailText) {
            this.detailText = detailText;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }
    }
}
