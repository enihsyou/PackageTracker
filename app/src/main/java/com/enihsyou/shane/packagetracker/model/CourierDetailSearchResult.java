package com.enihsyou.shane.packagetracker.model;

import com.google.gson.annotations.SerializedName;

public class CourierDetailSearchResult {
    @SerializedName("status")
    private int status;
    @SerializedName("courier")
    private CourierSearchResult.Courier courier;

    public int getStatus() {
        return status;
    }

    public CourierSearchResult.Courier getCourier() {
        return courier;
    }

}
