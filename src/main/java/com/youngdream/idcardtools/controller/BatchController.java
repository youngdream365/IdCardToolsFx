package com.youngdream.idcardtools.controller;

import com.youngdream.idcardtools.service.AnalyzerService;
import com.youngdream.idcardtools.utils.FileUtil;
import com.youngdream.idcardtools.utils.Toast;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 批量解析控制器
 *
 * @author YoungDream
 **/
public class BatchController implements Initializable {
    @FXML
    public TextField fileField;
    @FXML
    public Button processBtn;

    private AnalyzerService analyzerService = new AnalyzerService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tooltip downTips = new Tooltip("支持txt文件"+System.lineSeparator()+"每行一个身份证号"+System.lineSeparator()+"解析后原路径生成副本文件");
        processBtn.setTooltip(downTips);
    }

    public void processHandle(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择身份证txt文本文件");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));

        File inFile = fileChooser.showOpenDialog(processBtn.getScene().getWindow());
        // 此处阻塞等待选择，没选择或类型不匹配，直接退出方法
        if (inFile == null) {
            Toast.warning((Stage) processBtn.getScene().getWindow(), "放弃选择");
            return;
        }
        if (!inFile.getName().toLowerCase().endsWith(".txt")) {
            Toast.error((Stage) processBtn.getScene().getWindow(), "文件格式不匹配");
            return;
        }

        fileField.setText(inFile.getName());
        FileUtil.processIdCardTxt(inFile, (Stage) processBtn.getScene().getWindow(), analyzerService);
    }
}
