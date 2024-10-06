package com.youngdream.idcardtools.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 * 大陆居民身份证工具类
 *
 * @author YoungDream
 * <p>
 * 参照《GB11643-1999》实现；
 * 当前使用的行政区划数据来源于：“中华人民共和国民政部”官网
 * <a href="http://www.mca.gov.cn/article/sj/xzqh/1980/202105/20210500033655.shtml">2020年中华人民共和国行政区划代码</a>
 */
public class IdCardUtil {
    /**
     * 采用格里高利历（四年一闰，百年不闰，四百年再闰）
     * 年：[1-9][0-9]{3},
     * 大月1，3，5，7，8，10，12，
     * 小月2，4，6，9，11（平二月28天，闰二月29天）
     */
    public static final String DATE_LEAP_YEAR_PATTERN = "^([0-9]{4})(((01|03|05|07|08|10|12)((0[1-9])|([1|2][0-9])|(3[0|1])))|((04|06|09|11)((0[1-9])|([1|2][0-9])|30))|(02((0[1-9])|(1[0-9])|(2[0-9]))))$";
    public static final String DATE_NONLEAP_YEAR_PATTERN = "^([0-9]{4})(((01|03|05|07|08|10|12)((0[1-9])|([1|2][0-9])|(3[0|1])))|((04|06|09|11)((0[1-9])|([1|2][0-9])|30))|(02((0[1-9])|(1[0-9])|(2[0-8]))))$";

    private IdCardUtil() {
    }

    public static boolean checkIdCard(String idCard) {
        if (checkG1(idCard)) {
            idCard = g1ToG2(idCard);
        }
        return checkG2(idCard);
    }

    /**
     * 一代身份证
     * 130503 670401 001
     * 行政区划代码 出生日期(yyMMdd) 序列号(男奇|女偶)
     * 13为河北，05为邢台，03为桥西区（现2015已改名信都区），出生日期为1967年04月01日，顺序号为001，男性。
     * 第一代居民身份证是中国自1984年为中华人民共和国公民颁发的身份证明性证件，
     * 第一阶段采用印刷和照相翻拍技术塑封而成，为聚酯薄膜密封、单页卡式，15位编码。
     * 1995年7月1日起启用新的防伪居民身份证，采用全息透视塑封套防伪。
     * 第一代居民身份证采用印刷和照相翻拍技术塑封而成，比较容易被伪造，有法律规定第一代居民身份证在有效期内继续有效。
     */
    public static boolean checkG1(String idCard) {
        if (isEmpty(idCard) || !idCard.matches("\\d{15}")) {
            return false;
        } else {
            String birthDate = "19" + idCard.substring(6, 12);
            return checkDate(birthDate);
        }
    }

    /**
     * 二代身份证
     * 431102 19860924 663 X
     * 行政区划号码 出生日期(yyyyMMdd) 序列号(男奇|女偶) 校验位
     * 43为湖南省，11为永州市，02为零陵区，出生日期为1986年9月24日，顺序号为663，男性。
     * 1999年10月1日起，建立和实行公民身份号码制度，身份代码是唯一的、终身不变的18位号码。
     * 2004年1月1日，第二代居民身份证开始换发，第一代居民身份证已经于2013年1月1日正式停用。
     */
    public static boolean checkG2(String idCard) {
        if (isEmpty(idCard) || !idCard.matches("^\\d{17}[0-9xX]$")) {
            return false;
        } else {
            String birthDate = idCard.substring(6, 14);
            return checkDate(birthDate) && checkBit(idCard);
        }
    }

    /**
     * 身份证升码
     * 一代证转为二代证
     */
    public static String g1ToG2(String idCard) {
        String g2IdCardPrefix = idCard.substring(0, 6) + "19" + idCard.substring(6);
        String bit = getCheckBit(g2IdCardPrefix);
        return g2IdCardPrefix + bit;
    }

    /**
     * 验证日期
     * 合法(true)，非法(false)
     */
    public static boolean checkDate(String yyyyMMdd) {
        if (isEmpty(yyyyMMdd) || !yyyyMMdd.matches("^[0-9]{8}$")) {
            return false;
        }
        int year = Integer.parseInt(yyyyMMdd.substring(0, 4));
        return isLeapYear(year) ? yyyyMMdd.matches(DATE_LEAP_YEAR_PATTERN) : yyyyMMdd.matches(DATE_NONLEAP_YEAR_PATTERN);
    }

    /**
     * 验证性别
     * 男(true)，女(false)
     */
    public static boolean checkGender(String idCard) {
        if (checkG1(idCard)) {
            idCard = g1ToG2(idCard);
        }
        int in = Integer.parseInt(idCard.substring(15, 17));
        return isOdd(in);
    }

    /**
     * 生成校验位
     * 校验码采用《ISO 7064:1983》，MOD 11-2校验码系统；
     * 根据身份证前17位数字计算生成
     */
    public static String getCheckBit(String idCardBase) {
        int[] weight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        String[] validate = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (Character.getNumericValue(idCardBase.toCharArray()[i]) * weight[i]);
        }
        return validate[sum % 11];
    }

    /**
     * 验证校验码
     */
    public static boolean checkBit(String idCard) {
        return idCard.substring(idCard.length() - 1).equalsIgnoreCase(getCheckBit(idCard));
    }

    /**
     * 根据年月日获取生肖,属相
     */
    public static String getChineseZodiac(String dateStr) {
        String[] str = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
        int year = Integer.parseInt(dateStr.substring(0, 4));
        return str[(year - 4) % 12];
    }

    /**
     * 根据月日获取星座
     */
    public static String getZodiac(String MMdd) {
        String monthAndDay = MMdd.substring(4, 8);
        String zodiac = "";
        if (monthAndDay.matches("(01((2[0-9])|(3[0|1])))|(02((0[1-9])|(1[0-8])))")) {
            //水瓶座0120-0218
            zodiac = "水瓶座(01月20日-02月18日)";
        } else if (monthAndDay.matches("(02(19|(2[0-9])|(3[0|1])))|(03((0[1-9])|(1[0-9])|20))")) {
            //双鱼座0219-0320
            zodiac = "双鱼座(02月19日-03月20日)";
        } else if (monthAndDay.matches("(03((2[1-9])|(3[0|1])))|(04((0[1-9])|(1[0-9])))")) {
            //白羊座0321-0419
            zodiac = "白羊座(03月21日-04月19日)";
        } else if (monthAndDay.matches("(04((2[0-9])|(3[0|1])))|(05((0[1-9])|(1[0-9])|20))")) {
            //金牛座0420-0520
            zodiac = "金牛座(04月20日-05月20日)";
        } else if (monthAndDay.matches("(05((2[1-9])|(3[0|1])))|(06((0[1-9])|(1[0-9])|(2[0-1])))")) {
            //双子座0521-0621
            zodiac = "双子座(05月21日-06月21日)";
        } else if (monthAndDay.matches("(06((2[2-9])|(3[0|1])))|(07((0[1-9])|(1[0-9])|(2[0-2])))")) {
            //巨蟹座0622-0722
            zodiac = "巨蟹座(06月22日-07月22日)";
        } else if (monthAndDay.matches("(07((2[3-9])|(3[0|1])))|(08((0[1-9])|(1[0-9])|(2[0-2])))")) {
            //狮子座0723-0822
            zodiac = "狮子座(07月23日-08月22日)";
        } else if (monthAndDay.matches("(08((2[3-9])|(3[0|1])))|(09((0[1-9])|(1[0-9])|(2[0-2])))")) {
            //处女座0823-0922
            zodiac = "处女座(08月23日-09月22日)";
        } else if (monthAndDay.matches("(09((2[3-9])|(3[0|1])))|(10((0[1-9])|(1[0-9])|(2[0-3])))")) {
            //天秤座0923-1023
            zodiac = "天秤座(09月23日-10月23日)";
        } else if (monthAndDay.matches("(10((2[4-9])|(3[0|1])))|(11((0[1-9])|(1[0-9])|(2[0-2])))")) {
            //天蝎座1024-1122
            zodiac = "天蝎座(10月24日-11月22日)";
        } else if (monthAndDay.matches("(11((2[3-9])|(3[0|1])))|(12((0[1-9])|(1[0-9])|(2[0-1])))")) {
            //射手座1123-1221
            zodiac = "射手座(11月23日-12月21日)";
        } else if (monthAndDay.matches("(12((2[2-9])|(3[0|1])))|(01((0[1-9])|(1[0-9])))")) {
            //摩羯座1222-0119
            zodiac = "摩羯座(12月22日-01月19日)";
        }
        return zodiac;
    }

    /**
     * 获取区间内随机日期，内部做了比较交换
     */
    public static String randomDate(LocalDate form, LocalDate to) {
        //比较器，正大负小
        if (form.compareTo(to) > 0) {
            LocalDate tempLocalDate = form;
            form = to;
            to = tempLocalDate;
        }
        // 加一天
        long until = form.until(to, ChronoUnit.DAYS) + 1;
        //左闭右开[0,bound);
        int days = new Random().nextInt(Math.toIntExact(until));
        return form.plusDays(days).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /**
     * 验证闰年(true)，平年(false)
     */
    private static boolean isLeapYear(int year) {
        return ((year & 3) == 0) && ((year % 100) != 0 || (year % 400) == 0);
    }

    /**
     * 验证奇数(true)，偶数(false)
     */
    public static boolean isOdd(int num) {
        return (num & 1) == 1;
    }

    /**
     * 校验空字符串
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 校验非空字符串
     */
    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }
}
