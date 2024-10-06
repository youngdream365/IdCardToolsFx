package com.youngdream.idcardtools.service;

import com.youngdream.idcardtools.entity.Area;
import com.youngdream.idcardtools.entity.Constant;
import com.youngdream.idcardtools.utils.IdCardUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分析器业务类
 *
 * @author YoungDream
 */
public class AnalyzerService {
    /**
     * 反解析出身份证信息
     */
    public String getIdCardInfo(String idCard) {
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
        LocalDate idCardDate = LocalDate.of(Integer.parseInt(idCard.substring(6, 10)), Integer.parseInt(idCard.substring(10, 12)), Integer.parseInt(idCard.substring(12, 14)));
        int age = idCardDate.until(LocalDate.now()).getYears();
        String ageStr = age > 100 ? age + "（人瑞）" : age < 0 ? age + "（尚未出生)" : String.valueOf(age);

        info.append("行政区划：");
        if (areas.size() == 0) {
            info.append("(暂未找到，可能该行政区已变更)").append(System.lineSeparator());
        } else {
            info.append(areas.get(0).getFullname()).append(System.lineSeparator());
        }
        info.append("性别：").append(IdCardUtil.checkGender(idCard) ? "男" : "女")
                .append("，出生日期：").append(idCard, 6, 10).append("年")
                .append(idCard, 10, 12).append("月").append(idCard, 12, 14).append("日").append(System.lineSeparator())
                .append("目前年龄：").append(ageStr).append("，")
                .append("属相：").append(IdCardUtil.getChineseZodiac(idCard.substring(6, 14))).append(System.lineSeparator())
                .append("星座：").append(IdCardUtil.getZodiac(idCard.substring(6, 14)));
        return info.toString();
    }

    public String getIdCardInfoLine(String idCard) {
        return getIdCardInfo(idCard).replace(System.lineSeparator(),"，");
    }

    /**
     * @desc 匹配最近的地址
     * @author YoungDream
     */
    private List<Area> exactMatchArea(String idCard) {
        List<Area> collect = new ArrayList<>();
        if (idCard.length() > 6) {
            collect = Constant.areaData2020.getArea().stream()
                    .filter(a -> Integer.valueOf(idCard.substring(0, 6)).equals(a.getCode()))
                    .collect(Collectors.toList());
            if (collect.size() > 0) {
                return collect;
            } else {
                collect = Constant.areaData2020.getArea().stream()
                        .filter(a -> Integer.valueOf(idCard.substring(0, 4) + "00").equals(a.getCode()))
                        .collect(Collectors.toList());
                if (collect.size() > 0) {
                    return collect;
                } else {
                    collect = Constant.areaData2020.getArea().stream()
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
