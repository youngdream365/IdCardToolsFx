package com.youngdream.idcardtools.controller;

import com.youngdream.idcardtools.service.AnalyzerService;
import com.youngdream.idcardtools.utils.IdCardUtil;
import com.youngdream.idcardtools.utils.Toast;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 分析器，视图控制器类
 *
 * @author YoungDream
 */
public class AnalyzerController implements Initializable {

    /**
     * 身份证输入框
     */
    @FXML
    public TextField idCardField;

    /**
     * 解析按钮
     */
    @FXML
    public Button analyzerBtn;

    /**
     * 响应结果文本域
     */
    @FXML
    public TextArea resultArea;

    /**
     * 复制按钮
     */
    @FXML
    public Button copyBtn;
    /**
     * 重置按钮
     */
    @FXML
    public Button resetBtn;

    private AnalyzerService analyzerService = new AnalyzerService();

    /**
     * 初始化
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCardField.setText("");
        resultArea.setText("");
    }

    /**
     * 响应“解析”按钮事件
     */
    public void analyzerHandle(ActionEvent event) {
        resultArea.clear();
        String idCard = idCardField.getText().toUpperCase();
        if (IdCardUtil.isEmpty(idCard)) {
            Toast.warning((Stage) idCardField.getScene().getWindow(),"请填写身份证");
        } else {
            //解析身份证
            if (IdCardUtil.checkIdCard(idCard)) {
                Toast.success((Stage) idCardField.getScene().getWindow(),"验证通过");
                resultArea.setText(analyzerService.getIdCardInfo(idCard));
            } else {
                Toast.error((Stage) idCardField.getScene().getWindow(),"验证失败");
            }
        }
    }

    /**
     * 回车键即“解析”
     */
    public void analyzerInputHandle(KeyEvent keyEvent) {
        resultArea.clear();
        //全部转为大写
        String idCard = idCardField.getText().toUpperCase();
        //长度校验18位，超过自动截取
        if (idCard.length() > 18) {
            String temp = idCard.substring(0, 18);
            Toast.warning((Stage) idCardField.getScene().getWindow(),"长度不能超过18位");
            idCardField.setText(temp);
            idCardField.positionCaret(temp.length());
            return;
        }
        //回车提交，校验内容
        if (keyEvent.getCode() == KeyCode.ENTER) {
            analyzerHandle(null);
        }
    }

    public void copyHandle(ActionEvent actionEvent) {
        if (IdCardUtil.isNotEmpty(resultArea.getText())){
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(resultArea.getText());
            clipboard.setContent(content);
            Toast.success((Stage) idCardField.getScene().getWindow(),"已复制到剪切板");
        } else {
            Toast.warning((Stage) idCardField.getScene().getWindow(),"没有解析的数据");
        }
    }

    public void resetHandle(ActionEvent actionEvent) {
        idCardField.setText("");
        resultArea.setText("");
        Toast.success((Stage) idCardField.getScene().getWindow(),"重置成功");
    }
}
