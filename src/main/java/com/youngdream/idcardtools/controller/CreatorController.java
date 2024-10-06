package com.youngdream.idcardtools.controller;

import com.youngdream.idcardtools.common.AreaStringConverter;
import com.youngdream.idcardtools.common.DatePickConverter;
import com.youngdream.idcardtools.entity.Area;
import com.youngdream.idcardtools.entity.Constant;
import com.youngdream.idcardtools.entity.param.ParamBuilder;
import com.youngdream.idcardtools.service.CreatorService;
import com.youngdream.idcardtools.utils.IdCardUtil;
import com.youngdream.idcardtools.utils.Toast;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * 生成器控制器
 *
 * @author YoungDream
 */
public class CreatorController implements Initializable {
    /**
     * 省选择框
     */
    @FXML
    public ComboBox provinceCb;

    /**
     * 市选择框
     */
    @FXML
    public ComboBox cityCb;

    /**
     * 区选择框
     */
    @FXML
    public ComboBox districtCb;

    /**
     * 生日日期选择器,起始时间
     */
    @FXML
    public DatePicker birthDpFrom;

    /**
     * 生日日期选择器，结束时间
     */
    @FXML
    public DatePicker birthDpTo;

    /**
     * 性别单选项
     */
    @FXML
    public RadioButton genderRb;
    /**
     * 单选组
     */
    @FXML
    public ToggleGroup genderGroup;
    /**
     * 性别男性单选项
     */
    @FXML
    public RadioButton genderMaleRb;
    /**
     * 性别女性单选项
     */
    @FXML
    public RadioButton genderFemaleRb;
    /**
     * 结果文本域
     */
    @FXML
    public TextArea resultArea;
    /**
     * 候选数量滑杆
     */
    @FXML
    public Slider quantitySlider;
    /**
     * 候选数量文本
     */
    @FXML
    public Text quantityText;
    /**
     * “生成”按钮
     */
    @FXML
    public Button creatorBtn;

    /**
     * “重置”按钮
     */
    @FXML
    public Button resetBtn;
    /**
     * “复制”按钮
     */
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
        List<Area> provinces = Constant.areaData2020.getArea().stream().filter(area -> Constant.AREA_PROVINCE == area.getDepth()).sorted().collect(Collectors.toList());
        cities = Constant.areaData2020.getArea().stream().filter(area -> Constant.AREA_CITY == area.getDepth()).collect(Collectors.toList());
        districts = Constant.areaData2020.getArea().stream().filter(area -> Constant.AREA_DISTANCE == area.getDepth()).collect(Collectors.toList());

        lowestAreas = Constant.areaData2020.getArea().stream()
                .filter(area1 -> Constant.LOWEST_DEPTH_FLAG.equals(area1.getLowestdepth()) && area1.getCode() != null && area1.getCode() != 0)
                .collect(Collectors.toList());

        provinceCb.getItems().setAll(provinces);
        provinceCb.converterProperty().set(new AreaStringConverter());

        //滑杆
        quantitySlider.setMin(Constant.QUANTITY_MIN);
        quantitySlider.setMax(Constant.QUANTITY_MAX);
        quantitySlider.setValue(Constant.QUANTITY_DEFAULT);
        //展示标记(数字)
        quantitySlider.setShowTickLabels(true);
        //展示刻度(刻度线)
        quantitySlider.setShowTickMarks(true);
        //主刻度单位（主刻度线距离）
        quantitySlider.setMajorTickUnit(100);
        //次刻度线个数（如：4个刻度线，将每个主刻度单位分为5块）
        quantitySlider.setMinorTickCount(4);
        //步进，块增量
        quantitySlider.setBlockIncrement(Constant.QUANTITY_STEP);

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
                Toast.warning((Stage) creatorBtn.getScene().getWindow(), "不能小于开始时间");
                return;
            }
        });

        resultArea.setText("");
    }


    /**
     * 选择省多选框
     */
    public void pickProvinceHandle(ActionEvent actionEvent) {
        cityCb.getItems().clear();
        districtCb.getItems().clear();
        Area province = (Area) provinceCb.getSelectionModel().getSelectedItem();

        if (province != null) {
            List<Area> provinceCities = cities.stream().filter(cities -> cities.getPcode().equals(province.getCode())).sorted().collect(Collectors.toList());
            cityCb.getItems().setAll(provinceCities);
            cityCb.setConverter(new AreaStringConverter());
        }
    }

    public void pickCityHandle(ActionEvent actionEvent) {
        districtCb.getItems().clear();
        Area city = (Area) cityCb.getSelectionModel().getSelectedItem();
        if (city != null) {
            List<Area> cityDistricts = districts.stream().filter(districts -> districts.getPcode().equals(city.getCode())).sorted().collect(Collectors.toList());
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
    public void createIdCardsHandle(ActionEvent actionEvent) {
        //清空结果域
        resultArea.clear();
        Area province = (Area) provinceCb.getSelectionModel().getSelectedItem();
        Area city = (Area) cityCb.getSelectionModel().getSelectedItem();
        Area district = (Area) districtCb.getSelectionModel().getSelectedItem();

        Object gender = genderGroup.getSelectedToggle().getUserData();

        int quantity = new Double(quantitySlider.getValue()).intValue();
        if (quantity <= 0) {
            Toast.warning((Stage) creatorBtn.getScene().getWindow(), "请设置数量");
            return;
        }

        //行政区划三级，年龄，出生日期，性别
        ParamBuilder build = new ParamBuilder()
                .province(province).city(city).district(district)
                .gender(Integer.parseInt(gender.toString()))
                .birth(birthDpFrom.getValue()).quantity(quantity)
                .birthFrom(birthDpFrom.getValue())
                .birthTo(birthDpTo.getValue());

        Set<String> idCards = CreatorService.getIdCards(build);

        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        idCards.forEach(idCard -> joiner.add(idCard));
        resultArea.setText(joiner.toString());
        Toast.success((Stage) creatorBtn.getScene().getWindow(), "成功生成" + idCards.size() + "条");
    }

    /**
     * @desc 重置
     * @author YoungDream
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
        resultArea.setText("");
        Toast.success((Stage) genderFemaleRb.getScene().getWindow(), "重置成功");
    }

    public void copyHandle(ActionEvent actionEvent) {
        String areaText = resultArea.getText();
        if (IdCardUtil.isNotEmpty(areaText)) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(areaText);
            clipboard.setContent(content);
            Toast.success((Stage) genderFemaleRb.getScene().getWindow(), "已复制到剪切板");
        } else {
            Toast.warning((Stage) genderFemaleRb.getScene().getWindow(), "没有生成的数据");
        }
    }
}
