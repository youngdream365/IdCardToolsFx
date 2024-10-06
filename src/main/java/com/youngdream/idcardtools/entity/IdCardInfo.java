package com.youngdream.idcardtools.entity;

import com.youngdream.idcardtools.utils.ComUtil;

/**
 * 身份证解析详情信息
 *
 * @author YoungDream
 **/
public class IdCardInfo {
    private String originalId;

    private String currentId;

    private String izG1OrG2;

    private String areaInfo;

    private String gender;

    private String birthday;

    private String currentAge;

    private String chineseZodiac;

    private String zodiac;

    private String areaVersion;

    private String analyzerResult;

    private String analyzerTime;

    private IdCardInfo(Builder builder) {
        this.originalId = builder.originalId;
        this.currentId = builder.currentId;
        this.izG1OrG2 = builder.izG1OrG2;
        this.areaInfo = builder.areaInfo;
        this.gender = builder.gender;
        this.birthday = builder.birthday;
        this.currentAge = builder.currentAge;
        this.chineseZodiac = builder.chineseZodiac;
        this.zodiac = builder.zodiac;
        this.areaVersion = builder.areaVersion;
        this.analyzerResult = builder.analyzerResult;
        this.analyzerTime = builder.analyzerTime;
    }

    public static class Builder {
        private String originalId;
        private String currentId;
        private String izG1OrG2;
        private String areaInfo;
        private String gender;
        private String birthday;
        private String currentAge;
        private String chineseZodiac;
        private String zodiac;
        private String areaVersion;
        private String analyzerResult;
        private String analyzerTime;

        public IdCardInfo build() {
            return new IdCardInfo(this);
        }

        public IdCardInfo.Builder originalId(String originalId) {
            this.originalId = originalId;
            return this;
        }


        public IdCardInfo.Builder currentId(String currentId) {
            this.currentId = currentId;
            return this;
        }

        public IdCardInfo.Builder izG1OrG2(String izG1OrG2) {
            this.izG1OrG2 = izG1OrG2;
            return this;
        }

        public IdCardInfo.Builder areaInfo(String areaInfo) {
            this.areaInfo = areaInfo;
            return this;
        }

        public IdCardInfo.Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public IdCardInfo.Builder birthday(String birthday) {
            this.birthday = birthday;
            return this;
        }

        public IdCardInfo.Builder currentAge(String currentAge) {
            this.currentAge = currentAge;
            return this;
        }

        public IdCardInfo.Builder chineseZodiac(String chineseZodiac) {
            this.chineseZodiac = chineseZodiac;
            return this;
        }

        public IdCardInfo.Builder zodiac(String zodiac) {
            this.zodiac = zodiac;
            return this;
        }

        public IdCardInfo.Builder areaVersion(String areaVersion) {
            this.areaVersion = areaVersion;
            return this;
        }

        public IdCardInfo.Builder analyzerResult(String analyzerResult) {
            this.analyzerResult = analyzerResult;
            return this;
        }

        public IdCardInfo.Builder analyzerTime(String analyzerTime) {
            this.analyzerTime = analyzerTime;
            return this;
        }
    }

    public String toCSV() {
        return String.join(",",
                ComUtil.escapeCsv(this.originalId),
                ComUtil.escapeCsv(this.currentId),
                ComUtil.escapeCsv(this.izG1OrG2),
                ComUtil.escapeCsv(this.areaInfo),
                ComUtil.escapeCsv(this.gender),
                ComUtil.escapeCsv(this.birthday),
                ComUtil.escapeCsv(this.currentAge),
                ComUtil.escapeCsv(this.chineseZodiac),
                ComUtil.escapeCsv(this.zodiac),
                ComUtil.escapeCsv(this.areaVersion),
                ComUtil.escapeCsv(this.analyzerResult),
                ComUtil.escapeCsv(this.analyzerTime)
        ).replaceAll("null","");
    }

    public String toJson() {
        return ("{\"originalId\":\"" + this.originalId +
                "\",\"currentId\":\"" + this.currentId +
                "\",\"izG1OrG2\":\"" + this.izG1OrG2 +
                "\",\"areaInfo\":\"" + this.areaInfo +
                "\",\"gender\":\"" + this.gender +
                "\",\"birthday\":\"" + this.birthday +
                "\",\"currentAge\":\"" + this.currentAge +
                "\",\"chineseZodiac\":\"" + this.chineseZodiac +
                "\",\"zodiac\":\"" + this.zodiac +
                "\",\"areaVersion\":\"" + this.areaVersion +
                "\",\"analyzerResult\":\"" + this.analyzerResult +
                "\",\"analyzerTime\":\"" + this.analyzerTime +
                "\"}").replaceAll("\"null\"","null");
    }

    public String getOriginalId() {
        return originalId;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }

    public String getIzG1OrG2() {
        return izG1OrG2;
    }

    public void setIzG1OrG2(String izG1OrG2) {
        this.izG1OrG2 = izG1OrG2;
    }

    public String getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCurrentAge() {
        return currentAge;
    }

    public void setCurrentAge(String currentAge) {
        this.currentAge = currentAge;
    }

    public String getChineseZodiac() {
        return chineseZodiac;
    }

    public void setChineseZodiac(String chineseZodiac) {
        this.chineseZodiac = chineseZodiac;
    }

    public String getZodiac() {
        return zodiac;
    }

    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }

    public String getAreaVersion() {
        return areaVersion;
    }

    public void setAreaVersion(String areaVersion) {
        this.areaVersion = areaVersion;
    }

    public String getAnalyzerResult() {
        return analyzerResult;
    }

    public void setAnalyzerResult(String analyzerResult) {
        this.analyzerResult = analyzerResult;
    }

    public String getAnalyzerTime() {
        return analyzerTime;
    }

    public void setAnalyzerTime(String analyzerTime) {
        this.analyzerTime = analyzerTime;
    }

}
