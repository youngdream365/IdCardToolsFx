package com.youngdream.idcardtools.controller;

import com.youngdream.idcardtools.common.AreaStringConverter;
import com.youngdream.idcardtools.common.Const;
import com.youngdream.idcardtools.common.DatePickConverter;
import com.youngdream.idcardtools.common.Toast;
import com.youngdream.idcardtools.entity.Area;
import com.youngdream.idcardtools.entity.param.IdcardCandidate;
import com.youngdream.idcardtools.service.CreatorService;
import com.youngdream.idcardtools.utils.IdCardUtil;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * IdCard生成，视图控制器类
 *
 * @author YoungDream
 */
public class CreatorController implements Initializable {
    @FXML
    public ComboBox<Area> provinceCb;

    @FXML
    public ComboBox<Area> cityCb;

    @FXML
    public ComboBox<Area> districtCb;

    @FXML
    public DatePicker birthDpFrom;

    @FXML
    public DatePicker birthDpTo;

    /**
     * 性别单选组
     */
    @FXML
    public ToggleGroup genderGroup;

    @FXML
    public RadioButton genderRb;

    @FXML
    public RadioButton genderMaleRb;

    @FXML
    public RadioButton genderFemaleRb;

    @FXML
    public TextArea resultArea;

    @FXML
    public Slider quantitySlider;

    @FXML
    public Text quantityText;

    @FXML
    public Button creatorBtn;

    @FXML
    public Button resetBtn;

    @FXML
    public Button copyBtn;

    /**
     * 所有行政区划
     * 全国标记为最低级别地区，过滤掉没有code或为0的行政区划(三沙市西沙区南沙区没有code)
     */
    public static List<Area> lowestAreas;

    private static List<Area> cities;

    private static List<Area> districts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Area> provinces = Const.areaData2020.getArea().stream()
                .filter(area -> Const.AREA_PROVINCE == area.getDepth())
                .sorted().collect(Collectors.toList());
        cities = Const.areaData2020.getArea().stream()
                .filter(area -> Const.AREA_CITY == area.getDepth())
                .collect(Collectors.toList());
        districts = Const.areaData2020.getArea().stream()
                .filter(area -> Const.AREA_DISTANCE == area.getDepth())
                .collect(Collectors.toList());

        lowestAreas = Const.areaData2020.getArea().stream()
                .filter(area1 -> Const.LOWEST_DEPTH_FLAG.equals(area1.getLowestdepth()) && area1.getCode() != null && area1.getCode() != 0)
                .collect(Collectors.toList());

        provinceCb.getItems().setAll(provinces);
        provinceCb.converterProperty().set(new AreaStringConverter());

        //滑杆
        quantitySlider.setMin(Const.QUANTITY_MIN);
        quantitySlider.setMax(Const.QUANTITY_MAX);
        quantitySlider.setValue(Const.QUANTITY_DEFAULT);
        //展示标记(数字)
        quantitySlider.setShowTickLabels(true);
        //展示刻度(刻度线)
        quantitySlider.setShowTickMarks(true);
        //主刻度单位（主刻度线距离）
        quantitySlider.setMajorTickUnit(100);
        //次刻度线个数（如：4个刻度线，将每个主刻度单位分为5块）
        quantitySlider.setMinorTickCount(4);
        //步进，块增量
        quantitySlider.setBlockIncrement(Const.QUANTITY_STEP);

        double quantity = quantitySlider.getValue();
        quantitySlider.setValue((int) quantity);
        quantityText.setText(String.valueOf((int) quantity));

        birthDpFrom.setConverter(new DatePickConverter());
        birthDpTo.setConverter(new DatePickConverter());
        
        // 时间值更新事件监听
        birthDpFrom.valueProperty().addListener((observable, oldValue, newValue) -> {
            birthDpTo.setValue(newValue);
        });
        birthDpTo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (birthDpFrom.getValue() == null) {
                birthDpFrom.setValue(newValue);
            }
            if (newValue.compareTo(birthDpFrom.getValue()) < 0) {
                Toast.warning("不能小于开始时间");
                return;
            }
        });
        resultArea.setText(Const.EMPTY_STR);
    }

    /**
     * 选择省多选框
     */
    public void pickProvinceHandle(ActionEvent event) {
        cityCb.getItems().clear();
        districtCb.getItems().clear();
        Area province = provinceCb.getSelectionModel().getSelectedItem();

        if (province != null) {
            List<Area> provinceCities = cities.stream()
                    .filter(cities -> cities.getPcode().equals(province.getCode()))
                    .sorted().collect(Collectors.toList());
            cityCb.getItems().setAll(provinceCities);
            cityCb.setConverter(new AreaStringConverter());
        }
    }

    public void pickCityHandle(ActionEvent event) {
        districtCb.getItems().clear();
        Area city = cityCb.getSelectionModel().getSelectedItem();
        if (city != null) {
            List<Area> cityDistricts = districts.stream()
                    .filter(districts -> districts.getPcode().equals(city.getCode()))
                    .sorted().collect(Collectors.toList());
            districtCb.getItems().setAll(cityDistricts);
            districtCb.setConverter(new AreaStringConverter());
        }
    }

    /**
     * 数量改动处理
     */
    public void changeQuantityHandle(Event event) {
        //鼠标滚轮事件
        if (event instanceof ScrollEvent) {
            ScrollEvent scrollEvent = (ScrollEvent) event;
            if (scrollEvent.getDeltaY() > 0) {
                quantitySlider.increment();
            } else {
                quantitySlider.decrement();
            }
        }
        //其他事件
        double quantity = quantitySlider.getValue();
        quantitySlider.setValue((int) quantity);
        quantityText.setText(String.valueOf((int) quantity));
    }

    /**
     * 响应点击【生成身份证按钮】事件
     */
    public void createIdCardsHandle(ActionEvent event) {
        //清空结果域
        resultArea.clear();
        Area province = provinceCb.getSelectionModel().getSelectedItem();
        Area city = cityCb.getSelectionModel().getSelectedItem();
        Area district = districtCb.getSelectionModel().getSelectedItem();

        Object gender = genderGroup.getSelectedToggle().getUserData();

        int quantity = new Double(quantitySlider.getValue()).intValue();
        if (quantity <= 0) {
            Toast.warning("请设置数量");
            return;
        }
        //行政区划三级，年龄，出生日期，性别
        IdcardCandidate candidate = new IdcardCandidate.Builder()
                .province(province).city(city).district(district)
                .gender(Integer.parseInt(gender.toString()))
                .birth(birthDpFrom.getValue()).quantity(quantity)
                .birthFrom(birthDpFrom.getValue())
                .birthTo(birthDpTo.getValue()).build();
        Set<String> idCards = CreatorService.getIdCards(candidate);
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        idCards.forEach(joiner::add);
        resultArea.setText(joiner.toString());
        Toast.success("成功生成 " + idCards.size() + " 条");
    }

    /**
     * 重置
     */
    public void resetHandle(ActionEvent event) {
        provinceCb.getSelectionModel().clearSelection();
        cityCb.getItems().clear();
        districtCb.getItems().clear();
        genderRb.setSelected(true);
        quantitySlider.setValue(0);
        quantityText.setText("0");
        birthDpFrom.setValue(null);
        birthDpTo.setValue(null);
        resultArea.setText(Const.EMPTY_STR);
        Toast.success("重置成功");
    }

    /**
     * 复制
     */
    public void copyHandle(ActionEvent event) {
        String areaText = resultArea.getText();
        if (IdCardUtil.isNotEmpty(areaText)) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(areaText);
            clipboard.setContent(content);
            Toast.success("已复制到剪切板");
        } else {
            Toast.warning("没有生成的数据");
        }
    }

}
