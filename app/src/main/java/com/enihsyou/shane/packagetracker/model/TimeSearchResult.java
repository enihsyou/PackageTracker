package com.enihsyou.shane.packagetracker.model;

import java.util.ArrayList;
import java.util.List;

public class TimeSearchResult {
    private List<Entry> entries;

    public TimeSearchResult() {
        this.entries = new ArrayList<>();
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public void addEntries(String companyName, String companyLogoUrl, String time) {
        this.entries.add(new Entry(companyName, companyLogoUrl, time));
    }

    public static class Entry {
        String companyName;
        String companyLogoUrl;
        String time;

        public Entry(String companyName, String companyLogoUrl, String time) {
            this.companyName = companyName;
            this.companyLogoUrl = companyLogoUrl;
            this.time = time;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCompanyLogoUrl() {
            return companyLogoUrl;
        }

        public void setCompanyLogoUrl(String companyLogoUrl) {
            this.companyLogoUrl = companyLogoUrl;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
