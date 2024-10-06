package com.youngdream.idcardtools.entity.param;

import com.youngdream.idcardtools.entity.Area;

import java.time.LocalDate;

/**
 * 身份证构建, Builder
 *
 * @author YoungDream
 */
public class ParamBuilder {
    /**
     * 省/自治区/直辖市
     */
    private Area province;

    /**
     * 市/区
     */
    private Area city;

    /**
     * 县/镇/乡
     */
    private Area district;

    /**
     * 出生日期(yyyy/MM/dd)
     */
    private LocalDate birth;

    /**
     * 出生日期开始(yyyy/MM/dd)
     */
    private LocalDate birthFrom;

    /**
     * 出生日期截止(yyyy/MM/dd)
     */
    private LocalDate birthTo;

    /**
     * 性别(-1随机，0女性，1男性)
     */
    private Integer gender;

    /**
     * 生成数量
     */
    private Integer quantity;

    @Override
    public String toString() {
        return "ParamBuilder{" +
                "province=" + province +
                ", city=" + city +
                ", district=" + district +
                ", birth=" + birth +
                ", birthFrom=" + birthFrom +
                ", birthTo=" + birthTo +
                ", gender=" + gender +
                ", quantity=" + quantity +
                '}';
    }

    public Area getProvince() {
        return province;
    }

    public void setProvince(Area province) {
        this.province = province;
    }

    public Area getCity() {
        return city;
    }

    public void setCity(Area city) {
        this.city = city;
    }

    public Area getDistrict() {
        return district;
    }

    public void setDistrict(Area district) {
        this.district = district;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public LocalDate getBirthFrom() {
        return birthFrom;
    }

    public void setBirthFrom(LocalDate birthFrom) {
        this.birthFrom = birthFrom;
    }

    public LocalDate getBirthTo() {
        return birthTo;
    }

    public void setBirthTo(LocalDate birthTo) {
        this.birthTo = birthTo;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ParamBuilder province(Area province) {
        this.province = province;
        return this;
    }

    public ParamBuilder city(Area city) {
        this.city = city;
        return this;
    }

    public ParamBuilder district(Area district) {
        this.district = district;
        return this;
    }

    public ParamBuilder birth(LocalDate birth) {
        this.birth = birth;
        return this;
    }

    public ParamBuilder gender(Integer gender) {
        this.gender = gender;
        return this;
    }

    public ParamBuilder quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public ParamBuilder birthFrom(LocalDate birthFrom) {
        this.birthFrom = birthFrom;
        return this;
    }

    public ParamBuilder birthTo(LocalDate birthTo) {
        this.birthTo = birthTo;
        return this;
    }
}
