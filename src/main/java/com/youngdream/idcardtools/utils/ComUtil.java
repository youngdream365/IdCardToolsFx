package com.youngdream.idcardtools.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.UUID;

/**
 * 通用工具类
 *
 * @author YoungDream
 **/
public class ComUtil {
    private ComUtil() {
    }

    public static boolean isEmpty(CharSequence cs) {
        return (cs == null || cs.length() == 0);
    }

    public static boolean notEmpty(CharSequence cs) {
        return !isEmpty(cs);
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
     * 简单的UUID（去除连字符）
     */
    public static String simpleUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 转义特殊字符，如逗号、换行符、双引号
     */
    public static String escapeCsv(String text) {
        if (isEmpty(text)) {
            return text;
        }
        if (text.contains(",") || text.contains("\"")
                || text.contains("\n") || text.contains("\r")) {
            // 将双引号替换为两个双引号，开头和结尾加上引号
            text = text.replace("\"", "\"\"");
            return "\"" + text + "\"";
        }
        return text;
    }

}
