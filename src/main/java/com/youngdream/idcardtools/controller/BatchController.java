package com.youngdream.idcardtools.controller;

import com.youngdream.idcardtools.common.Toast;
import com.youngdream.idcardtools.service.AnalyzerService;
import com.youngdream.idcardtools.utils.FileUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * IdCard批量解析，视图控制器类
 *
 * @author YoungDream
 */
public class BatchController implements Initializable {
    @FXML
    public TextField fileField;

    @FXML
    public Button processBtn;

    public ComboBox<CreatorController> fileTypeCb;

    private final AnalyzerService analyzerService = new AnalyzerService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tooltip downTips = new Tooltip("支持导入csv或txt格式文件" + System.lineSeparator() + "文件内要求一行一个身份证号" + System.lineSeparator() + "解析后原路径生成副本文件");
        processBtn.setTooltip(downTips);
        fileTypeCb.setTooltip(new Tooltip("选择导出文件类型"));
    }

    public void processHandle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择身份证号码文件支持csv/txt（每个号码独占一行）");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
        File inFile = fileChooser.showOpenDialog(processBtn.getScene().getWindow());
        if (inFile == null) {
            Toast.info("放弃选择");
        } else {
            fileField.setText(inFile.getName());
            if (fileTypeCb.getSelectionModel().isSelected(1)) {
                FileUtil.process2Json(inFile, analyzerService);
            } else {
                FileUtil.process2Csv(inFile, analyzerService);
            }
        }
    }

}
