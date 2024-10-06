package com.youngdream.idcardtools.entity.param;

import com.youngdream.idcardtools.entity.Area;

import java.time.LocalDate;

/**
 * 身份证号码构建候选实体, Builder
 *
 * @author YoungDream
 */
public class IdcardCandidate {
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

    private IdcardCandidate(Builder builder) {
        this.province = builder.province;
        this.city = builder.city;
        this.district = builder.district;
        this.birth = builder.birth;
        this.birthFrom = builder.birthFrom;
        this.birthTo = builder.birthTo;
        this.gender = builder.gender;
        this.quantity = builder.quantity;
    }

    // Builder 静态内部类
    public static class Builder {
        private Area province;
        private Area city;
        private Area district;
        private LocalDate birth;
        private LocalDate birthFrom;
        private LocalDate birthTo;
        private Integer gender;
        private Integer quantity;

        public Builder province(Area province) {
            this.province = province;
            return this;
        }

        public Builder city(Area city) {
            this.city = city;
            return this;
        }

        public Builder district(Area district) {
            this.district = district;
            return this;
        }

        public Builder birth(LocalDate birth) {
            this.birth = birth;
            return this;
        }

        public Builder birthFrom(LocalDate birthFrom) {
            this.birthFrom = birthFrom;
            return this;
        }

        public Builder birthTo(LocalDate birthTo) {
            this.birthTo = birthTo;
            return this;
        }

        public Builder gender(Integer gender) {
            this.gender = gender;
            return this;
        }

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public IdcardCandidate build() {
            return new IdcardCandidate(this);
        }

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

}
