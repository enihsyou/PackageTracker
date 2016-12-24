package com.enihsyou.shane.packagetracker.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一件包裹的一条运送信息
 */
public class PackageEachTraffic {
    private static SimpleDateFormat dateFormat =
            new SimpleDateFormat("yy-MM-dd\nkk:mm:ss", Locale.getDefault());
    private static Pattern REGEX_PHONE = Pattern.compile(".*(?=手机|电话|号码).*?(\\d{7,}).*");

    private Date time;
    private String location;
    private String context;
    private String phone;

    public String getPhone() {
        if (phone == null) parsePhone();
        return phone;
    }

    private void parsePhone() {
        if (context != null) {
            Matcher matcher = REGEX_PHONE.matcher(context);
            if (matcher.find()) phone = matcher.group(1);
        }
    }

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
