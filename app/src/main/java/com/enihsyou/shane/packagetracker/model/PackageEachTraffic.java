package com.enihsyou.shane.packagetracker.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 一件包裹的一条运送信息
 */
public class PackageEachTraffic {
    private static SimpleDateFormat dateFormat =
            new SimpleDateFormat("yy-MM-dd\nkk:mm:ss", Locale.getDefault());

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

    public String getTimeString() {
        return dateFormat.format(this.time);
    }
}
