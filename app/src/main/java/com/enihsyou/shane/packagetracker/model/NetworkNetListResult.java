package com.enihsyou.shane.packagetracker.model;

import com.google.gson.annotations.SerializedName;

/**
 * 每个网点详细的查询结果
 */
public class NetworkNetListResult {
    @SerializedName("id")
    private int id;
    @SerializedName("companyNumber")
    private String companyNumber; //快递公司代码
    @SerializedName("companyName")
    private String companyName; //快递公司名字
    @SerializedName("name")
    private String name; //服务站名字 // TODO: 2016/12/14 JsonAdapter
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
    @SerializedName("telephone")
    private String telephone; //一个电话

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
