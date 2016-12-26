package com.enihsyou.shane.packagetracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CurrentLocationResult {
    @SerializedName("status")
    String status;
    @SerializedName("results")
    List<Results> results;

    public String getStatus() {
        return status;
    }

    public List<Results> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Status: %s Sizes: %d", status, results.size());
    }

    public static class Results {
        @SerializedName("address_components")
        List<AddressComponents> addresses;
        @SerializedName("formatted_address")
        String formattedAddress;
        @SerializedName("types")
        List<String> types;

        public List<AddressComponents> getAddresses() {
            return addresses;
        }

        public String getFormattedAddress() {
            return formattedAddress;
        }

        public List<String> getTypes() {
            return types;
        }

        @Override
        public String toString() {
            return String.format(Locale.getDefault(), "%s, %s", formattedAddress, Arrays.toString(types.toArray()));
        }
    }

    public static class AddressComponents {
        @SerializedName("long_name")
        String longName;
        @SerializedName("short_name")
        String shortName;
        @SerializedName("types")
        List<String> types;

        public List<String> getTypes() {
            return types;
        }

        public String getLongName() {
            return longName;
        }

        public String getShortName() {
            return shortName;
        }

        @Override
        public String toString() {
            return String.format(Locale.getDefault(), "Long name: %s Short name: %s", longName, shortName);
        }
    }
}
