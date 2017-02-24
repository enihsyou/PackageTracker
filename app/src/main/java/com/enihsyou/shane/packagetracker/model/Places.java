package com.enihsyou.shane.packagetracker.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 地区列表数据
 */
public class Places {
    public static final ArrayList<Province> PROVINCES =
        new ArrayList<>(Arrays.asList(new Province[]{
            new Province("北京", "110000", null, true),
            new Province("天津", "120000", null, true),
            new Province("上海", "310000", null, true),
            new Province("重庆", "500000", null, true),
            new Province("广东", "440000", null),
            new Province("浙江", "330000", null),
            new Province("江苏", "320000", null),
            new Province("山东", "370000", null),
            new Province("河南", "410000", null),
            new Province("河北", "130000", null),
            new Province("福建", "350000", null),
            new Province("湖北", "420000", null),
            new Province("陕西", "610000", null),
            new Province("辽宁", "210000", null),
            new Province("四川", "510000", null),
            new Province("安徽", "340000", null),
            new Province("湖南", "430000", null),
            new Province("山西", "140000", null),
            new Province("黑龙江", "230000", null),
            new Province("广西", "450000", null),
            new Province("内蒙古", "150000", null),
            new Province("吉林", "220000", null),
            new Province("云南", "530000", null),
            new Province("江西", "360000", null),
            new Province("贵州", "520000", null),
            new Province("甘肃", "620000", null),
            new Province("新疆", "650000", null),
            new Province("海南", "460000", null),
            new Province("宁夏", "640000", null),
            new Province("青海", "630000", null),
            new Province("西藏", "540000", null),
            new Province("台湾", "710000", null),
            new Province("香港", "810000", null),
            new Province("澳门", "820000", null)
        }));
    public static Province getProvince(int first){
        return PROVINCES.get(first);
    }
    public static City getCity(int first, int second){
        return (City) getProvince(first).nexts.get(second);
    }
    public static Area getArea(int first, int second, int third){
        return (Area) getCity(first, second).nexts.get(third);
    }
}
