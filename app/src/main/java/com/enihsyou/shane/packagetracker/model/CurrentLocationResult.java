package com.enihsyou.shane.packagetracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrentLocationResult {
    @SerializedName("status")
    String status;
    @SerializedName("results")
    List<Results> results;

    static class Results {
        @SerializedName("address_components")
        List<AddressComponents> addresses;
        @SerializedName("formatted_address")
        String formattedAddress;
        @SerializedName("types")
        List<String> types;
    }

    static class AddressComponents {
        @SerializedName("long_name")
        String longName;
        @SerializedName("short_name")
        String shortName;
    }
}
