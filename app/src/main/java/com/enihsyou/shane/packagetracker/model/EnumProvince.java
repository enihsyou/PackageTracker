package com.enihsyou.shane.packagetracker.model;

import android.util.SparseArray;

import java.io.IOException;

public enum EnumProvince {
    北京(110000, true),
    上海(310000, true),
    天津(120000, true),
    重庆(500000, true),
    安徽(34, false),
    澳门(82, false),
    福建(35, false),
    甘肃(62, false),
    广东(44, false),
    广西(45, false),
    贵州(52, false),
    海南(46, false),
    河北(13, false),
    河南(41, false),
    湖北(42, false),
    湖南(43, false),
    吉林(22, false),
    江苏(32, false),
    江西(36, false),
    辽宁(21, false),
    宁夏(64, false),
    青海(63, false),
    山东(37, false),
    山西(14, false),
    陕西(61, false),
    四川(51, false),
    台湾(71, false),
    西藏(54, false),
    新疆(65, false),
    香港(81, false),
    云南(53, false),
    浙江(33, false),
    黑龙江(23, false),
    内蒙古(15, false);
    private static SparseArray<String> cities = new SparseArray<>();

    static {
        cities.put(130100, "石家庄市");
        cities.put(130200, "唐山市");
        cities.put(130300, "秦皇岛市");
        cities.put(130400, "邯郸市");
        cities.put(130500, "邢台市");
        cities.put(130600, "保定市");
        cities.put(130700, "张家口市");
        cities.put(130800, "承德市");
        cities.put(130900, "沧州市");
        cities.put(131000, "廊坊市");
        cities.put(131100, "衡水市");
        cities.put(140100, "太原市");
        cities.put(140200, "大同市");
        cities.put(140300, "阳泉市");
        cities.put(140400, "长治市");
        cities.put(140500, "晋城市");
        cities.put(140600, "朔州市");
        cities.put(140700, "晋中市");
        cities.put(140800, "运城市");
        cities.put(140900, "忻州市");
        cities.put(141000, "临汾市");
        cities.put(141100, "吕梁市");
        cities.put(150100, "呼和浩特市");
        cities.put(150200, "包头市");
        cities.put(150300, "乌海市");
        cities.put(150400, "赤峰市");
        cities.put(150500, "通辽市");
        cities.put(150600, "鄂尔多斯市");
        cities.put(150700, "呼伦贝尔市");
        cities.put(150800, "巴彦淖尔市");
        cities.put(150900, "乌兰察布市");
        cities.put(152200, "兴安盟");
        cities.put(152500, "锡林郭勒盟");
        cities.put(152900, "阿拉善盟");
        cities.put(210100, "沈阳市");
        cities.put(210200, "大连市");
        cities.put(210300, "鞍山市");
        cities.put(210400, "抚顺市");
        cities.put(210500, "本溪市");
        cities.put(210600, "丹东市");
        cities.put(210700, "锦州市");
        cities.put(210800, "营口市");
        cities.put(210900, "阜新市");
        cities.put(211000, "辽阳市");
        cities.put(211100, "盘锦市");
        cities.put(211200, "铁岭市");
        cities.put(211300, "朝阳市");
        cities.put(211400, "葫芦岛市");
        cities.put(220100, "长春市");
        cities.put(220200, "吉林市");
        cities.put(220300, "四平市");
        cities.put(220400, "辽源市");
        cities.put(220500, "通化市");
        cities.put(220600, "白山市");
        cities.put(220700, "松原市");
        cities.put(220800, "白城市");
        cities.put(222400, "延边");
        cities.put(230100, "哈尔滨市");
        cities.put(230200, "齐齐哈尔市");
        cities.put(230300, "鸡西市");
        cities.put(230400, "鹤岗市");
        cities.put(230500, "双鸭山市");
        cities.put(230600, "大庆市");
        cities.put(230700, "伊春市");
        cities.put(230800, "佳木斯市");
        cities.put(230900, "七台河市");
        cities.put(231000, "牡丹江市");
        cities.put(231100, "黑河市");
        cities.put(231200, "绥化市");
        cities.put(232700, "大兴安岭");
        cities.put(320100, "南京市");
        cities.put(320200, "无锡市");
        cities.put(320300, "徐州市");
        cities.put(320400, "常州市");
        cities.put(320500, "苏州市");
        cities.put(320600, "南通市");
        cities.put(320700, "连云港市");
        cities.put(320800, "淮安市");
        cities.put(320900, "盐城市");
        cities.put(321000, "扬州市");
        cities.put(321100, "镇江市");
        cities.put(321200, "泰州市");
        cities.put(321300, "宿迁市");
        cities.put(330100, "杭州市");
        cities.put(330200, "宁波市");
        cities.put(330300, "温州市");
        cities.put(330400, "嘉兴市");
        cities.put(330500, "湖州市");
        cities.put(330600, "绍兴市");
        cities.put(330700, "金华市");
        cities.put(330800, "衢州市");
        cities.put(330900, "舟山市");
        cities.put(331000, "台州市");
        cities.put(331100, "丽水市");
        cities.put(340100, "合肥市");
        cities.put(340200, "芜湖市");
        cities.put(340300, "蚌埠市");
        cities.put(340400, "淮南市");
        cities.put(340500, "马鞍山市");
        cities.put(340600, "淮北市");
        cities.put(340700, "铜陵市");
        cities.put(340800, "安庆市");
        cities.put(341000, "黄山市");
        cities.put(341100, "滁州市");
        cities.put(341200, "阜阳市");
        cities.put(341300, "宿州市");
        cities.put(341500, "六安市");
        cities.put(341600, "亳州市");
        cities.put(341700, "池州市");
        cities.put(341800, "宣城市");
        cities.put(350100, "福州市");
        cities.put(350200, "厦门市");
        cities.put(350300, "莆田市");
        cities.put(350400, "三明市");
        cities.put(350500, "泉州市");
        cities.put(350600, "漳州市");
        cities.put(350700, "南平市");
        cities.put(350800, "龙岩市");
        cities.put(350900, "宁德市");
        cities.put(360100, "南昌市");
        cities.put(360200, "景德镇市");
        cities.put(360300, "萍乡市");
        cities.put(360400, "九江市");
        cities.put(360500, "新余市");
        cities.put(360600, "鹰潭市");
        cities.put(360700, "赣州市");
        cities.put(360800, "吉安市");
        cities.put(360900, "宜春市");
        cities.put(361000, "抚州市");
        cities.put(361100, "上饶市");
        cities.put(370100, "济南市");
        cities.put(370200, "青岛市");
        cities.put(370300, "淄博市");
        cities.put(370400, "枣庄市");
        cities.put(370500, "东营市");
        cities.put(370600, "烟台市");
        cities.put(370700, "潍坊市");
        cities.put(370800, "济宁市");
        cities.put(370900, "泰安市");
        cities.put(371000, "威海市");
        cities.put(371100, "日照市");
        cities.put(371200, "莱芜市");
        cities.put(371300, "临沂市");
        cities.put(371400, "德州市");
        cities.put(371500, "聊城市");
        cities.put(371600, "滨州市");
        cities.put(371700, "菏泽市");
        cities.put(410100, "郑州市");
        cities.put(410200, "开封市");
        cities.put(410300, "洛阳市");
        cities.put(410400, "平顶山市");
        cities.put(410500, "安阳市");
        cities.put(410600, "鹤壁市");
        cities.put(410700, "新乡市");
        cities.put(410800, "焦作市");
        cities.put(410900, "濮阳市");
        cities.put(411000, "许昌市");
        cities.put(411100, "漯河市");
        cities.put(411200, "三门峡市");
        cities.put(411300, "南阳市");
        cities.put(411400, "商丘市");
        cities.put(411500, "信阳市");
        cities.put(411600, "周口市");
        cities.put(411700, "驻马店市");
        cities.put(415100, "济源市");
        cities.put(420100, "武汉市");
        cities.put(420200, "黄石市");
        cities.put(420300, "十堰市");
        cities.put(420500, "宜昌市");
        cities.put(420600, "襄阳市");
        cities.put(420700, "鄂州市");
        cities.put(420800, "荆门市");
        cities.put(420900, "孝感市");
        cities.put(421000, "荆州市");
        cities.put(421100, "黄冈市");
        cities.put(421200, "咸宁市");
        cities.put(421300, "随州市");
        cities.put(422800, "恩施");
        cities.put(425400, "仙桃市");
        cities.put(425500, "潜江市");
        cities.put(425600, "天门市");
        cities.put(427100, "神农架");
        cities.put(430100, "长沙市");
        cities.put(430200, "株洲市");
        cities.put(430300, "湘潭市");
        cities.put(430400, "衡阳市");
        cities.put(430500, "邵阳市");
        cities.put(430600, "岳阳市");
        cities.put(430700, "常德市");
        cities.put(430800, "张家界市");
        cities.put(430900, "益阳市");
        cities.put(431000, "郴州市");
        cities.put(431100, "永州市");
        cities.put(431200, "怀化市");
        cities.put(431300, "娄底市");
        cities.put(433100, "湘西");
        cities.put(440100, "广州市");
        cities.put(440300, "深圳市");
        cities.put(440400, "珠海市");
        cities.put(441900, "东莞市");
        cities.put(440500, "汕头市");
        cities.put(440600, "佛山市");
        cities.put(440700, "江门市");
        cities.put(440800, "湛江市");
        cities.put(440200, "韶关市");
        cities.put(440900, "茂名市");
        cities.put(441200, "肇庆市");
        cities.put(441300, "惠州市");
        cities.put(441400, "梅州市");
        cities.put(441500, "汕尾市");
        cities.put(441600, "河源市");
        cities.put(441700, "阳江市");
        cities.put(441800, "清远市");
        cities.put(442000, "中山市");
        cities.put(445100, "潮州市");
        cities.put(445200, "揭阳市");
        cities.put(445300, "云浮市");
        cities.put(450100, "南宁市");
        cities.put(450200, "柳州市");
        cities.put(450300, "桂林市");
        cities.put(450400, "梧州市");
        cities.put(450500, "北海市");
        cities.put(450600, "防城港市");
        cities.put(450700, "钦州市");
        cities.put(450800, "贵港市");
        cities.put(450900, "玉林市");
        cities.put(451000, "百色市");
        cities.put(451100, "贺州市");
        cities.put(451200, "河池市");
        cities.put(451300, "来宾市");
        cities.put(451400, "崇左市");
        cities.put(460100, "海口市");
        cities.put(460200, "三亚市");
        cities.put(460300, "三沙市");
        cities.put(465100, "五指山市");
        cities.put(465200, "琼海市");
        cities.put(465300, "儋州市");
        cities.put(465500, "文昌市");
        cities.put(465600, "万宁市");
        cities.put(465700, "东方市");
        cities.put(467100, "定安县");
        cities.put(467200, "屯昌县");
        cities.put(467300, "澄迈县");
        cities.put(467400, "临高县");
        cities.put(467500, "白沙");
        cities.put(467600, "昌江");
        cities.put(467700, "乐东");
        cities.put(467800, "陵水");
        cities.put(467900, "保亭");
        cities.put(468000, "琼中");
        cities.put(510100, "成都市");
        cities.put(510300, "自贡市");
        cities.put(510400, "攀枝花市");
        cities.put(510500, "泸州市");
        cities.put(510600, "德阳市");
        cities.put(510700, "绵阳市");
        cities.put(510800, "广元市");
        cities.put(510900, "遂宁市");
        cities.put(511000, "内江市");
        cities.put(511100, "乐山市");
        cities.put(511300, "南充市");
        cities.put(511400, "眉山市");
        cities.put(511500, "宜宾市");
        cities.put(511600, "广安市");
        cities.put(511700, "达州市");
        cities.put(511800, "雅安市");
        cities.put(511900, "巴中市");
        cities.put(512000, "资阳市");
        cities.put(513200, "阿坝");
        cities.put(513300, "甘孜");
        cities.put(513400, "凉山");
        cities.put(520100, "贵阳市");
        cities.put(520200, "六盘水市");
        cities.put(520300, "遵义市");
        cities.put(520400, "安顺市");
        cities.put(520500, "毕节市");
        cities.put(520600, "铜仁市");
        cities.put(522300, "黔西南");
        cities.put(522600, "黔东南");
        cities.put(522700, "黔南");
        cities.put(530100, "昆明市");
        cities.put(530300, "曲靖市");
        cities.put(530400, "玉溪市");
        cities.put(530500, "保山市");
        cities.put(530600, "昭通市");
        cities.put(530700, "丽江市");
        cities.put(530800, "普洱市");
        cities.put(530900, "临沧市");
        cities.put(532300, "楚雄");
        cities.put(532500, "红河");
        cities.put(532600, "文山");
        cities.put(532800, "西双版纳");
        cities.put(532900, "大理");
        cities.put(533100, "德宏");
        cities.put(533300, "怒江");
        cities.put(533400, "迪庆");
        cities.put(540100, "拉萨市");
        cities.put(542100, "昌都");
        cities.put(542200, "山南");
        cities.put(542300, "日喀则");
        cities.put(542400, "那曲");
        cities.put(542500, "阿里");
        cities.put(542600, "林芝");
        cities.put(610100, "西安市");
        cities.put(610200, "铜川市");
        cities.put(610300, "宝鸡市");
        cities.put(610400, "咸阳市");
        cities.put(610500, "渭南市");
        cities.put(610600, "延安市");
        cities.put(610700, "汉中市");
        cities.put(610800, "榆林市");
        cities.put(610900, "安康市");
        cities.put(611000, "商洛市");
        cities.put(620100, "兰州市");
        cities.put(620200, "嘉峪关市");
        cities.put(620300, "金昌市");
        cities.put(620400, "白银市");
        cities.put(620500, "天水市");
        cities.put(620600, "武威市");
        cities.put(620700, "张掖市");
        cities.put(620800, "平凉市");
        cities.put(620900, "酒泉市");
        cities.put(621000, "庆阳市");
        cities.put(621100, "定西市");
        cities.put(621200, "陇南市");
        cities.put(622900, "临夏");
        cities.put(623000, "甘南");
        cities.put(630100, "西宁市");
        cities.put(632100, "海东");
        cities.put(632200, "海北");
        cities.put(632300, "黄南");
        cities.put(632500, "海南");
        cities.put(632600, "果洛");
        cities.put(632700, "玉树");
        cities.put(632800, "海西");
        cities.put(640100, "银川市");
        cities.put(640200, "石嘴山市");
        cities.put(640300, "吴忠市");
        cities.put(640400, "固原市");
        cities.put(640500, "中卫市");
        cities.put(650100, "乌鲁木齐市");
        cities.put(650200, "克拉玛依市");
        cities.put(652100, "吐鲁番");
        cities.put(652200, "哈密");
        cities.put(652300, "昌吉");
        cities.put(652700, "博尔塔拉");
        cities.put(652800, "巴音郭楞");
        cities.put(652900, "阿克苏");
        cities.put(653000, "克孜勒苏");
        cities.put(653100, "喀什");
        cities.put(653200, "和田");
        cities.put(654000, "伊犁");
        cities.put(654200, "塔城");
        cities.put(654300, "阿勒泰");
        cities.put(655100, "石河子市");
        cities.put(655200, "阿拉尔市");
        cities.put(655300, "图木舒克市");
        cities.put(655400, "五家渠市");
        cities.put(710100, "台北");
        cities.put(710200, "新北");
        cities.put(710300, "台中");
        cities.put(710400, "台南");
        cities.put(710500, "高雄");
        cities.put(710600, "基隆");
        cities.put(710900, "桃园");
        cities.put(711000, "新竹");
        cities.put(711100, "苗栗");
        cities.put(711200, "彰化");
        cities.put(711300, "南投");
        cities.put(711400, "云林");
        cities.put(711500, "嘉义");
        cities.put(711600, "屏东");
        cities.put(711700, "宜兰");
        cities.put(711800, "花莲");
        cities.put(711900, "台东");
        cities.put(712000, "澎湖");
        cities.put(810100, "香港");
        cities.put(810200, "九龙");
        cities.put(810300, "新界");
        cities.put(820100, "花地玛堂");
        cities.put(820200, "圣安多尼堂");
        cities.put(820300, "大堂");
        cities.put(820400, "望德堂");
        cities.put(820500, "风顺堂");
        cities.put(820600, "嘉模堂");
        cities.put(820700, "圣方济各堂");
        cities.put(820800, "路氹城");
        cities.put(820900, "澳门新城");
    }

    private int code;
    private boolean isCity;

    EnumProvince(int code, boolean isCity) {
        this.code = code;
        this.isCity = isCity;
    }

    /**
     * 返回下一层城市或地区列表，如果已经是个城市了，则通过网络获取地区列表
     * 如果是省份，则从类中获得省内城市
     *
     * @param isCity 是否是城市
     *
     * @return 下一层
     *
     * @throws IOException 网络错误
     */
    public SparseArray<String> getCity(boolean isCity) throws IOException {
        SparseArray<String> result = new SparseArray<>();
        if (isCity) { //如果这一层已经是城市了，则获取当前城市地区列表
            NetworkCityResult[] cityResult =
                new Kuaidi100Fetcher().networkCityResult(String.valueOf(code));
            for (NetworkCityResult networkCityResult : cityResult) { // 添加每一个结果，使用地区全名
                result.append(Integer.parseInt(networkCityResult.getCode()), networkCityResult.getFullName());
            }
        } else {
            for (int i = 0; i < cities.size(); i++) {
                int key = cities.keyAt(i);
                String s = cities.get(key);
                if (s.startsWith(String.valueOf(code))) result.append(key, s); //按顺序添加 没使用put
            }
        }
        return result;
    }
}
