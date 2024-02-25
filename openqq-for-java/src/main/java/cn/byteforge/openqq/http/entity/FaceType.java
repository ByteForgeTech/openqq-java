package cn.byteforge.openqq.http.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 表情枚举类
 * <a href="https://github.com/kyubotics/coolq-http-api/wiki/%E8%A1%A8%E6%83%85-CQ-%E7%A0%81-ID-%E8%A1%A8">
 * https://github.com/kyubotics/coolq-http-api/wiki/%E8%A1%A8%E6%83%85-CQ-%E7%A0%81-ID-%E8%A1%A8
 * </a>
 * */
@Getter
@AllArgsConstructor
public enum FaceType {

    UNKNOWN(-1, "未知"),
    JY(0, "惊讶"),
    PZ(1, "撇嘴"),
    SE(2, "色"),
    FD(3, "发呆"),
    DY(4, "得意"),
    LL(5, "流泪"),
    HX(6, "害羞"),
    BZ(7, "闭嘴"),
    SHUI(8, "睡"),
    DK(9, "大哭"),
    GG(10, "尴尬"),
    FN(11, "发怒"),
    TP(12, "调皮"),
    CY(13, "呲牙"),
    WX(14, "微笑"),
    NG(15, "难过"),
    KUK(16, "酷"),
    ZK(18, "抓狂"),
    TUU(19, "吐"),
    TX(20, "偷笑"),
    KA(21, "可爱"),
    BAIY(22, "白眼"),
    AM(23, "傲慢"),
    JIE(24, "饥饿"),
    KUN(25, "困"),
    JK(26, "惊恐"),
    LH(27, "流汗"),
    HANX(28, "憨笑"),
    DB(29, "悠闲"),
    FENDOU(30, "奋斗"),
    ZHM(31, "咒骂"),
    YIW(32, "疑问"),
    XU(33, "嘘"),
    YUN(34, "晕"),
    ZHEM(35, "折磨"),
    SHUAI(36, "衰"),
    KL(37, "骷髅"),
    QIAO(38, "敲打"),
    ZJ(39, "再见"),
    FAD(41, "发抖"),
    AIQ(42, "爱情"),
    TIAO(43, "跳跳"),
    ZT(46, "猪头"),
    YB(49, "拥抱"),
    DG(53, "蛋糕"),
    SHD(54, "闪电"),
    ZHD(55, "炸弹"),
    DAO(56, "刀"),
    ZQ(57, "足球"),
    BB(59, "便便"),
    KF(60, "咖啡"),
    FAN(61, "饭"),
    YAO(62, "药"),
    MG(63, "玫瑰"),
    DX(64, "凋谢"),
    XIN(66, "爱心"),
    XS(67, "心碎"),
    LW(69, "礼物"),
    _072(72, "信封", true),
    TY(74, "太阳"),
    YL(75, "月亮"),
    QIANG(76, "赞"),
    RUO(77, "踩"),
    WS(78, "握手"),
    SHL(79, "胜利"),
    FW(85, "飞吻"),
    OH(86, "怄火"),
    XIG(89, "西瓜"),
    _090(90, "下雨", true),
    _091(91, "多云", true),
    LENGH(96, "冷汗"),
    CH(97, "擦汗"),
    KB(98, "抠鼻"),
    GZ(99, "鼓掌"),
    QD(100, "糗大了"),
    HUAIX(101, "坏笑"),
    ZHH(102, "左哼哼"),
    YHH(103, "右哼哼"),
    HQ(104, "哈欠"),
    BS(105, "鄙视"),
    WQ(106, "委屈"),
    KK(107, "快哭了"),
    YX(108, "阴险"),
    QQ(109, "左亲戚"),
    XIA(110, "吓"),
    XJJ(111, "小纠结"),
    CD(112, "菜刀"),
    PJ(113, "啤酒"),
    LQ(114, "篮球"),
    PP(115, "乒乓"),
    SA(116, "示爱"),
    PCH(117, "瓢虫"),
    BQ(118, "抱拳"),
    GY(119, "勾引"),
    QT(120, "拳头"),
    CJ(121, "差劲"),
    AINI(122, "爱你"),
    BU(123, "NO"),
    HD(124, "OK"),
    ZHQ(125, "转圈"),
    KT(126, "磕头"),
    HT(127, "回头"),
    TSH(128, "跳绳"),
    HSH(129, "挥手"),
    JD(130, "激动"),
    JW(131, "街舞"),
    XW(132, "献吻"),
    ZUOTJ(133, "左太极"),
    YOUTJ(134, "右太极"),
    SHX(136, "双喜"),
    BP(137, "嗨皮牛耶"),
    DL(138, "灯笼"),
    _139(139, "发财", true),
    KG(140, "K歌", true),
    _141(141, "购物", true),
    _142(142, "信封", true),
    _143(143, "帅", true),
    HEC(144, "喝彩"),
    QIDAO(145, "祈祷"),
    BAOJIN(146, "爆筋"),
    BANGBANGT(147, "棒棒糖"),
    HN(148, "喝奶"),
    _149(149, "面条", true),
    _150(150, "香蕉", true),
    FJ(151, "飞机"),
    _152(152, "汽车", true),
    _153(153, "左车头", true),
    _154(154, "车厢", true),
    _155(155, "右车头", true),
    _156(156, "下雨", true),
    _157(157, "多云", true),
    CP(158, "钞票"),
    _159(159, "熊猫", true),
    _160(160, "灯泡", true),
    _161(161, "小风车", true),
    _162(162, "闹钟", true),
    _163(163, "雨伞", true),
    _164(164, "气球", true),
    _165(165, "钻戒", true),
    _166(166, "沙发", true),
    _167(167, "卷纸", true),
    YAO_(168, "药"),
    SHQ(169, "手枪"),
    _170(170, "青蛙", true),
    CHA(171, "茶"),
    ZYJ(172, "眨眼睛"),
    LB(173, "泪奔"),
    WN(174, "无奈"),
    MM(175, "卖萌"),
    XJJ_(176, "小纠结"),
    PX(177, "喷血"),
    XYX(178, "斜眼笑"),
    DOGE(179, "doge"),
    JX(180, "惊喜"),
    SR(181, "骚扰"),
    XK(182, "笑哭"),
    WZM(183, "我最美"),
    XHX(184, "河蟹"),
    YT(185, "羊驼"),
    _186(186, "栗子", true),
    YOUL(187, "幽灵"),
    DAN(188, "蛋"),
    _189(189, "九宫格", true),
    JH(190, "菊花"),
    _191(191, "香皂", true),
    HB(192, "红包"),
    DX_(193, "大笑"),
    BKX(194, "不开心"),
    LM(197, "冷漠"),
    EE(198, "呃呃"),
    HAOB(199, "好棒"),
    BT(200, "拜托"),
    DZ(201, "点赞"),
    WL(202, "无聊"),
    TL(203, "托脸"),
    CHI(204, "吃"),
    SH(205, "送花"),
    HP(206, "害怕"),
    HC(207, "花痴"),
    XY(208, "小样"),
    BL(210, "飙泪"),
    WBK(211, "我不看"),
    TS(212, "托腮"),
    _214(214, "啵啵"),
    _215(215, "糊脸"),
    _216(216, "拍头"),
    _217(217, "扯一扯"),
    _218(218, "舔一舔"),
    _219(219, "蹭一蹭"),
    _220(220, "酷", true),
    _221(221, "顶呱呱"),
    _222(222, "抱抱"),
    _223(223, "暴击"),
    _224(224, "开枪"),
    _225(225, "撩一撩"),
    _226(226, "拍桌"),
    _227(227, "拍手"),
    _229(229, "干杯"),
    _230(230, "嘲讽"),
    _231(231, "哼"),
    _232(232, "佛系"),
    _233(233, "掐一掐"),
    _235(235, "颤抖"),
    _237(237, "偷看"),
    _238(238, "扇脸"),
    _239(239, "原谅"),
    _240(240, "喷脸"),
    _241(241, "生日快乐"),
    _243(243, "甩头"),
    _244(244, "扔狗"),
    _262(262, "脑阔疼"),
    _263(263, "沧桑"),
    _264(264, "捂脸"),
    _265(265, "辣眼睛"),
    _266(266, "哦哟"),
    _267(267, "头秃"),
    _268(268, "问号脸"),
    _269(269, "暗中观察"),
    _270(270, "emm"),
    _271(271, "吃瓜"),
    _272(272, "呵呵哒"),
    _273(273, "我酸了"),
    WW(277, "汪汪"),
    _278(278, "汗"),
    _281(281, "无眼笑"),
    _282(282, "敬礼"),
    _283(283, "狂笑"),
    _284(284, "面无表情"),
    _285(285, "摸鱼"),
    _286(286, "魔鬼笑"),
    _287(287, "哦"),
    _288(288, "请"),
    _289(289, "睁眼"),
    _290(290, "敲开心"),
    _292(292, "让我康康"),
    _293(293, "摸锦鲤"),
    _294(294, "期待"),
    _295(295, "拿到红包"),
    _297(297, "拜谢"),
    _298(298, "元宝"),
    _299(299, "牛啊"),
    _300(300, "胖三斤"),
    _301(301, "好闪"),
    _302(302, "左拜年"),
    _303(303, "右拜年"),
    _306(306, "牛气冲天"),
    _307(307, "喵喵"),
    _311(311, "打call"),
    _312(312, "变形"),
    _314(314, "仔细分析"),
    _317(317, "菜汪"),
    _318(318, "崇拜"),
    _319(319, "比心"),
    _322(322, "拒绝"),
    _323(323, "嫌弃"),
    _324(324, "庆祝"),
    _325(325, "吃糖"),
    _326(326, "生气"),
    _332(332, "举牌牌"),
    _333(333, "烟花"),
    _334(334, "虎虎生威"),
    _336(336, "豹富"),
    _337(337, "花朵脸"),
    _338(338, "我想开了"),
    _339(339, "舔屏"),
    _341(341, "打招呼"),
    _342(342, "酸Q"),
    _343(343, "我方了"),
    _344(344, "大怨种"),
    _345(345, "红包多多"),
    _346(346, "你真棒棒"),
    _347(347, "大展宏兔"),
    _348(348, "福萝卜");

    private final Integer id;

    /**
     * 表情名称
     * @apiNote 默认为快捷键标识，以 _ 开头的名称表示该表情无快捷键
     * */
    private final String name;

    /**
     * 该表情当前是否属于经典表情
     * */
    private final boolean deprecated;

    FaceType(Integer id, String name) {
        this(id, name, false);
    }

    public static FaceType getFaceType(int id) {
        if (id < 0) return UNKNOWN;
        return Arrays.stream(FaceType.values())
                .filter(face -> face.id == id)
                .findFirst()
                .orElse(UNKNOWN);
    }

}