package com.youngdream.idcardtools.utils;

import com.youngdream.idcardtools.common.Const;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 * 大陆居民身份证工具类
 *
 * @author YoungDream
 * <p>
 * 参照《GB11643-1999》实现；日期相关计算简便起见采用格里高利历。
 * 当前使用的行政区划数据来源于：“中华人民共和国民政部”官网
 * <a href="http://www.mca.gov.cn/article/sj/xzqh/1980/202105/20210500033655.shtml">2020年中华人民共和国行政区划代码</a>
 */
public class IdCardUtil {

    private IdCardUtil() {
    }

    /**
     * 检查是否符合规范
     */
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
            return isValidDate(birthDate);
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
            return isValidDate(birthDate) && checkBit(idCard);
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
     * 获取二代身份证号码
     */
    public static String getG2IdCard(String idCard) {
        if (checkG2(idCard)) {
            return idCard.toUpperCase();
        }
        return checkG1(idCard) ? g1ToG2(idCard): Const.EMPTY_STR;
    }

    /**
     * 验证日期
     * 合法(true)，非法(false)
     */
    public static boolean isValidDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        try {
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static LocalDate birthday(String idCard) {
        if (checkG1(idCard))
            idCard = g1ToG2(idCard);
        return LocalDate.of(
                Integer.parseInt(idCard.substring(6, 10)),
                Integer.parseInt(idCard.substring(10, 12)),
                Integer.parseInt(idCard.substring(12, 14)));
    }

    /**
     * 验证性别：男(true)，女(false)
     */
    public static boolean checkGender(String idCard) {
        int in = Integer.parseInt(getG2IdCard(idCard).substring(16, 17));
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
    public static String getChineseZodiac(String idCard) {
        String[] str = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
        int year = Integer.parseInt(idCard.substring(6, 10));
        return str[(year - 4) % 12];
    }

    /**
     * 根据月日获取星座
     */
    public static String getZodiacSign(String idCard) {
        LocalDate date = LocalDate.parse(idCard.substring(6, 14), DateTimeFormatter.ofPattern("yyyyMMdd"));
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        String zodiac = "";
        if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) {
            zodiac = "水瓶座";
        } else if ((month == 2 && day >= 19) || (month == 3 && day <= 20)) {
            zodiac = "双鱼座";
        } else if ((month == 3 && day >= 21) || (month == 4 && day <= 19)) {
            zodiac = "白羊座";
        } else if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) {
            zodiac = "金牛座";
        } else if ((month == 5 && day >= 21) || (month == 6 && day <= 21)) {
            zodiac = "双子座";
        } else if ((month == 6 && day >= 22) || (month == 7 && day <= 22)) {
            zodiac = "巨蟹座";
        } else if ((month == 7 && day >= 23) || (month == 8 && day <= 22)) {
            zodiac = "狮子座";
        } else if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) {
            zodiac = "处女座";
        } else if ((month == 9 && day >= 23) || (month == 10 && day <= 23)) {
            zodiac = "天秤座";
        } else if ((month == 10 && day >= 24) || (month == 11 && day <= 22)) {
            zodiac = "天蝎座";
        } else if ((month == 11 && day >= 23) || (month == 12 && day <= 21)) {
            zodiac = "射手座";
        } else if ((month == 12 && day >= 22) || (month == 1 && day <= 19)) {
            zodiac = "摩羯座";
        }
        return zodiac;
    }

    /**
     * 验证：闰年(true)，平年(false)
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
