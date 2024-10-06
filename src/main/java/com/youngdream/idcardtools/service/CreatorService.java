package com.youngdream.idcardtools.service;

import com.youngdream.idcardtools.common.Const;
import com.youngdream.idcardtools.controller.CreatorController;
import com.youngdream.idcardtools.entity.Area;
import com.youngdream.idcardtools.entity.param.IdcardCandidate;
import com.youngdream.idcardtools.utils.ComUtil;
import com.youngdream.idcardtools.utils.IdCardUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * IdCard生成，业务类
 *
 * @author YoungDream
 */
public class CreatorService {
    /**
     * 生成一个身份证号码
     */
    public static String getOneIdCardWithTimeInterval(IdcardCandidate build) {
        //出生地
        Integer areaCode = getLowestAreaCode(build);
        //性别
        Integer gender = Const.GENDER_RANDOM;
        if (build.getGender() != null) {
            gender = build.getGender();
        }
        //出生日期
        String birth = Const.LOWEST_IDCARD_DATE;
        if (build.getBirthFrom() != null) {
            // from存在to不存在，或者 from和to相等，直接from赋值
            if (build.getBirthTo() == null || (build.getBirthFrom() == build.getBirthTo())) {
                birth = build.getBirth().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            } else {
                // to存在，且from和to不相等
                birth = ComUtil.randomDate(build.getBirthFrom(), build.getBirthTo());
            }
            // 不相等，取区间随机日期
        } else {
            birth = ComUtil.randomDate(LocalDate.of(1900, 1, 1), LocalDate.now());
        }
        //根据男女性别，取三位数序列号（男奇女偶）
        int genderNum;
        switch (gender) {
            case Const.GENDER_FEMALE:
                genderNum = new Random().nextInt(500) * 2;
                break;
            case Const.GENDER_MALE:
                genderNum = new Random().nextInt(500) * 2 + 1;
                break;
            default:
                genderNum = new Random().nextInt(1000);
        }
        NumberFormat nf = new DecimalFormat("000");
        String random3NumFormat = nf.format(genderNum);
        String baseIdCard = areaCode + birth + random3NumFormat;
        return areaCode + birth + random3NumFormat + IdCardUtil.getCheckBit(baseIdCard);
    }

    /**
     * 生成多个身份证号码
     * 一批最多500个
     */
    public static Set<String> getIdCards(IdcardCandidate build) {
        Integer quantity = Const.QUANTITY_DEFAULT;
        if (build.getQuantity() != null) {
            quantity = build.getQuantity();
        }
        Set<String> idCards = new HashSet<>(quantity);
        while (idCards.size() < Math.min(quantity, Const.QUANTITY_MAX)) {
            String idCard = getOneIdCardWithTimeInterval(build);
            if (IdCardUtil.isNotEmpty(idCard)) {
                idCards.add(idCard);
            }
        }
        return idCards;
    }

    /**
     * 拿到选中的最小的一个地区code
     */
    private static Integer getLowestAreaCode(IdcardCandidate build) {
        Integer areaCode = null;
        //区|县|镇
        if (build.getDistrict() != null) {
            if (build.getDistrict().getCode() != null) {
                areaCode = build.getDistrict().getCode();
            } else if (build.getDistrict().getPcode() != null) {
                areaCode = build.getDistrict().getPcode();
            }
        } else if (build.getCity() != null) {
            //市
            if (build.getCity().getCode() != null) {
                areaCode = build.getCity().getCode();
            } else if (build.getCity().getPcode() != null) {
                areaCode = build.getCity().getPcode();
            }
        } else if (build.getProvince() != null) {
            //省|直辖市|自治区
            if (build.getProvince().getCode() != null) {
                areaCode = build.getProvince().getCode();
            } else if (build.getProvince().getPcode() != null) {
                areaCode = build.getProvince().getPcode();
            }
        } else {
            //没有选择，随机拿一个最低级别地区
            List<Area> lowestAreas = CreatorController.lowestAreas;
            int index = new Random().nextInt(lowestAreas.size());
            areaCode = lowestAreas.get(index).getCode();
        }
        return areaCode;
    }
}
