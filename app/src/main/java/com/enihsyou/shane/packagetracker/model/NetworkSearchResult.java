package com.enihsyou.shane.packagetracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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

}
