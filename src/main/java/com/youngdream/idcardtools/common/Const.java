package com.youngdream.idcardtools.common;

import com.youngdream.idcardtools.entity.AreaData;
import com.youngdream.idcardtools.utils.XmlUtil;

/**
 * 通用静态常量
 *
 * @author YoungDream
 */
public class Const {
    public static final String APP_TITLE = "身份证助手";
    public static final String APP_VERSION = "v0.2.0";
    public static final String APP_AUTHOR = "@YoungDream";

    public static final String EMPTY_STR = "";
    public static final String BLANK_STR = " ";

    public static final String APP_ABOUT = APP_TITLE + BLANK_STR + APP_VERSION + BLANK_STR + APP_AUTHOR;

    public static class FxmlPath {
        public static final String MAIN = "/fxml/Main.fxml";
        public static final String ANALYZER_TAB = "/fxml/AnalyzerTab.fxml";
        public static final String BATCH_TAB = "/fxml/BatchTab.fxml";
        public static final String CREATOR_TAB = "/fxml/CreatorTab.fxml";
    }

    /**
     * 导出文件类型
     */
    public enum OutputFileType {
        CSV, TXT, JSON;
    }

    /**
     * 所有行政区划
     */
    public static AreaData areaData2020 = XmlUtil.xml2Entity(XmlUtil.loadXml("xzqh2020.xml"), AreaData.class);

    /**
     * 性别
     */
    public static final int GENDER_RANDOM = -1;
    public static final int GENDER_FEMALE = 0;
    public static final int GENDER_MALE = 1;

    /**
     * 行政区划等级
     */
    public static final int AREA_PROVINCE = 1;
    public static final int AREA_CITY = 2;
    public static final int AREA_DISTANCE = 3;

    /**
     * 默认行政区划代码，北京市东城区
     */
    public static final int AREA_CODE_DEFAULT = 110101;

    /**
     * 默认数量
     */
    public static final int QUANTITY_DEFAULT = 0;

    /**
     * 最小值
     */
    public static final int QUANTITY_MIN = 0;

    /**
     * 最大值
     */
    public static final int QUANTITY_MAX = 500;

    /**
     * 步长
     */
    public static final int QUANTITY_STEP = 50;

    /**
     * 最小年份
     */
    public static final int LOWEST_IDCARD_YEAR = 1900;

    /**
     * 最小日期
     */
    public static final String LOWEST_IDCARD_DATE = "19000101";

    /**
     * 最低行政区划
     */
    public static final String LOWEST_DEPTH_FLAG = "Y";

    public static final String YYYYMMDD_PATTREN = "yyyy/MM/dd";

    public static final AreaStringConverter AREA_STRING_CONVERTER = new AreaStringConverter();

    public static final DatePickConverter DATE_PICK_CONVERTER = new DatePickConverter();

}
