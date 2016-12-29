package com.enihsyou.shane.packagetracker.model;

import com.google.gson.annotations.SerializedName;

public class NetworkCityResult {
    @SerializedName("name")
    private String name; //县区名称
    @SerializedName("number")
    private String code; // 县区代号
    @SerializedName("fullName")
    private String fullName; //县区全名

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
