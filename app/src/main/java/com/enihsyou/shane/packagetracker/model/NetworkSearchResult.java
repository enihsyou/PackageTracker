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
    private List<NetworkNetList> netLists; // 返回网点列表
    @SerializedName("companyTotal")
    private List<Company> companyTotals; // 符合的公司列表

    public int getStatus() {
        return status;
    }

    public List<NetworkNetList> getNetLists() {
        return netLists;
    }

    /**
     * 每个网点详细的查询结果
     * 创建界面的时候 我们需要
     * 公司名字 {@code companyName} 公司代码 {@code companyCode}
     * 网点名字 {@code name}
     * 公司地址 {@code address}
     * 联系电话 {@code telephone}
     */
    public static class NetworkNetList {
        private static transient Pattern HtmlTag = Pattern.compile("</?font.*?>");
        @SerializedName("id")
        private long id;
        @SerializedName("companyId")
        private int companyId;
        @SerializedName("companyCode")
        private String companyCode; //快递公司代码
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

        public int getCompanyId() {
            return companyId;
        }

        public String getCompanyCode() {
            return companyCode;
        }

        public String getCompanyName() {
            return companyName;
        }

        public String getName() {
            return name;
        }

        public String getLinkMan() {
            return linkMan;
        }

        public String getAddress() {
            return address;
        }

        public String getWorkArea() {
            return workArea;
        }

        public String getRefuseArea() {
            return refuseArea;
        }

        public String getDetailText() {
            return detailText;
        }

        public String getTelephone() {
            return telephone;
        }
    }

    public static class Company {
        @SerializedName("companyName")
        private String companyName;
        @SerializedName("companyCode")
        private String companyCode;
        @SerializedName("companyId")
        private int companyId;
        @SerializedName("count")
        private int counts;

        public String getCompanyName() {
            return companyName;
        }

        public String getCompanyCode() {
            return companyCode;
        }

        public int getCompanyId() {
            return companyId;
        }

        public int getCounts() {
            return counts;
        }
    }
}
