package com.youngdream.idcardtools.service;

import com.youngdream.idcardtools.common.Const;
import com.youngdream.idcardtools.entity.Area;
import com.youngdream.idcardtools.entity.IdCardInfo;
import com.youngdream.idcardtools.utils.IdCardUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * IdCard解析，服务类
 *
 * @author YoungDream
 */
public class AnalyzerService {
    /**
     * 反解析出身份证信息
     */
    public String getIdCardInfoStr(String idCard) {
        StringBuilder info = new StringBuilder();
        if (IdCardUtil.checkG1(idCard)) {
            info.append(idCard).append(" 为一代身份证（15位）").append(System.lineSeparator());
            idCard = IdCardUtil.g1ToG2(idCard);
            info.append("已自动升码为二代（18位）：").append(idCard).append(System.lineSeparator());
        } else if (IdCardUtil.checkG2(idCard)) {
            info.append(idCard).append(" 为二代身份证（18位）").append(System.lineSeparator());
        } else {
            return info.append("该身份证号码不符合标准，无法识别").toString();
        }
        List<Area> areas = exactMatchArea(idCard);
        LocalDate birthday = IdCardUtil.birthday(idCard);
        int age = birthday.until(LocalDate.now()).getYears();
        String ageStr = age > 100 ? age + "（人瑞）" : age < 0 ? age + "（尚未出生)" : String.valueOf(age);
        info.append("行政区划：").append(areas.size() > 0 ? areas.get(0).getFullname() : "暂未找到，该行政区或已变更").append(System.lineSeparator());
        info.append("性别：").append(IdCardUtil.checkGender(idCard) ? "男" : "女")
                .append("，出生日期：").append(birthday.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"))).append(System.lineSeparator())
                .append("当前年龄（周岁）：").append(ageStr).append("，")
                .append("属相：").append(IdCardUtil.getChineseZodiac(idCard)).append(System.lineSeparator())
                .append("星座：").append(IdCardUtil.getZodiacSign(idCard));
        return info.toString();
    }

    /**
     *
     */
    public IdCardInfo getIdCardInfo(String idCard) {
        String nowDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (!IdCardUtil.checkIdCard(idCard)) {
            return (new IdCardInfo.Builder()).originalId(idCard).analyzerResult("验证不通").analyzerTime(nowDateTime).areaVersion(Const.areaData2020.getComment().getStatisticalDeadline()).build();
        } else {
            List<Area> areas = exactMatchArea(idCard);
            String g2IdCard = IdCardUtil.getG2IdCard(idCard);
            LocalDate birthday = IdCardUtil.birthday(g2IdCard);
            int age = birthday.until(LocalDate.now()).getYears();
            String ageStr = age > 100 ? age + "（人瑞）" : (age < 0 ? age + "（尚未出生)" : String.valueOf(age));
            return new IdCardInfo.Builder()
                    .originalId(idCard)
                    .analyzerResult("验证通过")
                    .currentId(g2IdCard)
                    .izG1OrG2(IdCardUtil.checkG1(idCard) ? "一代" : "二代")
                    .areaInfo(areas.size() > 0 ? areas.get(0).getFullname() : "暂未找到，该行政区或已变更")
                    .gender(IdCardUtil.checkGender(idCard) ? "男" : "女")
                    .birthday(birthday.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")))
                    .currentAge(ageStr)
                    .chineseZodiac(IdCardUtil.getChineseZodiac(idCard))
                    .zodiac(IdCardUtil.getZodiacSign(idCard))
                    .analyzerTime(nowDateTime)
                    .areaVersion(Const.areaData2020.getComment().getStatisticalDeadline())
                    .build();
        }
    }

    /**
     * 匹配最近的地址
     */
    private List<Area> exactMatchArea(String idCard) {
        List<Area> collect = new ArrayList<>();
        if (idCard.length() > 6) {
            // 区/县/旗
            collect = Const.areaData2020.getArea().stream()
                    .filter(a -> Integer.valueOf(idCard.substring(0, 6)).equals(a.getCode()))
                    .collect(Collectors.toList());
            if (collect.size() > 0) {
                return collect;
            } else {
                // 市/直辖市/州/盟
                collect = Const.areaData2020.getArea().stream()
                        .filter(a -> Integer.valueOf(idCard.substring(0, 4) + "00").equals(a.getCode()))
                        .collect(Collectors.toList());
                if (collect.size() > 0) {
                    return collect;
                } else {
                    // 省/自治区/直辖市/港澳台
                    collect = Const.areaData2020.getArea().stream()
                            .filter(a -> Integer.valueOf(idCard.substring(0, 2) + "0000").equals(a.getCode()))
                            .collect(Collectors.toList());
                    if (collect.size() > 0) {
                        return collect;
                    }
                }
            }
        }
        return collect;
    }

}
