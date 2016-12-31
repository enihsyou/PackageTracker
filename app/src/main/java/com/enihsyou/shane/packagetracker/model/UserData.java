package com.enihsyou.shane.packagetracker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class UserData  implements Serializable{
    @SerializedName("head")
    private int head = -1;
    @SerializedName("id")
    private int id = -1;
    @SerializedName("username")
    private String username;
    @SerializedName("usermail")
    private String usermail;
    @SerializedName("password")
    private String password;
    @SerializedName("remarks")
    private String remarks;
    @SerializedName("isSuccessful")
    @Expose(deserialize = false)
    private boolean isSuccessful = false;
    @SerializedName("packageList")
    private ArrayList<KUAIDI> packageList = new ArrayList<>();

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<KUAIDI> getPackageList() {
        return packageList;
    }

    public void addPackage(KUAIDI kuaidi) {
        if (this.getPackageList().isEmpty()) {
            packageList.add(kuaidi);
        } else {
            for (KUAIDI kuaidiform : packageList) {
                if (kuaidiform.getPackage_id().equals(kuaidi.getPackage_id())) {
                    packageList.remove(kuaidiform);
                    break;
                }
            }
            packageList.add(kuaidi);
        }
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
