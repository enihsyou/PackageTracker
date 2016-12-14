package com.enihsyou.shane.packagetracker.model;

/**
 * 从JSON的快件状态数字转成文字
 */
public enum EnumStatusString {
    s0("在途"),
    s1("揽件"),
    s2("疑难"),
    s3("签收"),
    s4("退签"),
    s5("派件"),
    s6("退回");

    private String status;

    EnumStatusString(String s) {
        status = s;
    }
    public static String getStatus(int s){
        String query = "s" + s;
        return EnumStatusString.valueOf(query).toString();
    }

    @Override
    public String toString() {
        return status;
    }
}
