package com.enihsyou.shane.packagetracker;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * 一件包裹的一条运送信息
 */
public class PackageEachTraffic {
    private Date time;
    private String location;
    private String context;

    public Date getTime() {
        return time;
    }

    public void setTime(String time, DateFormat formatter) {
        try {
            this.time = formatter.parse(time);
        } catch (ParseException never) {
            //won't happen
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
