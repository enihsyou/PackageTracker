package com.enihsyou.shane.packagetracker.model;

import java.io.Serializable;

/**
 * Created by Sleaf on 2016/12/22 0022.
 * *******************************************
 * 发送到服务器的数据仅接受第一行
 * 每次发送数据必须带head信息，如果是获取类的必须带用户名和密码。
 * 发回的数据包含的类别详见userdata类。
 * ……
 * *******************************************
 */

// TODO: 2016/12/24 0024 添加快递的方法

public class KUAIDI implements Serializable{
    String package_id;
    String package_info;

    public KUAIDI(String id, String info) {
        this.package_id = id;
        this.package_info = info;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getPackage_info() {
        return package_info;
    }

    public void setPackage_info(String package_info) {
        this.package_info = package_info;
    }
}
