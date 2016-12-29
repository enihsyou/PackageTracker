package com.enihsyou.shane.packagetracker.enums;

/**
 * 从JSON的快件状态数字转成文字
 */
public enum EnumStatusString {
    在途,
    揽件,
    疑难,
    签收,
    退签,
    派件,
    退回


    // public static String getStatus(int s){
    //     String query = "s" + s;
    //     return EnumStatusString.valueOf(query).toString();
    // }

    // @Override
    // public String toString() {
    //     return status;
    // }
}
