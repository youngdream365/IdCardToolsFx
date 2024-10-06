package com.youngdream.idcardtools.controller;

import com.youngdream.idcardtools.common.Const;
import com.youngdream.idcardtools.common.Toast;
import com.youngdream.idcardtools.service.AnalyzerService;
import com.youngdream.idcardtools.utils.IdCardUtil;
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
 * IdCard解析，视图控制器类
 *
 * @author YoungDream
 */
public class AnalyzerController implements Initializable {
    @FXML
    public TextField idCardField;

    @FXML
    public Button analyzerBtn;

    @FXML
    public TextArea resultArea;

    @FXML
    public Button copyBtn;

    @FXML
    public Button resetBtn;

    private final AnalyzerService analyzerService = new AnalyzerService();

    /**
     * 初始化
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCardField.setText(Const.EMPTY_STR);
        resultArea.setText(Const.EMPTY_STR);
    }

    /**
     * 响应“解析”按钮事件
     */
    public void analyzerHandle(ActionEvent event) {
        resultArea.clear();
        String idCard = idCardField.getText().toUpperCase();
        if (IdCardUtil.isEmpty(idCard)) {
            Toast.warning("请填写身份证");
        } else {
            //解析身份证
            if (IdCardUtil.checkIdCard(idCard)) {
                Toast.success("验证通过");
                resultArea.setText(analyzerService.getIdCardInfoStr(idCard));
            } else {
                Toast.error("验证未通过，此身份证号不符合标准");
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
            Toast.warning("长度不能超过18位");
            idCardField.setText(temp);
            idCardField.positionCaret(temp.length());
            return;
        }
        //回车提交，校验内容
        if (keyEvent.getCode() == KeyCode.ENTER) {
            analyzerHandle(null);
        }
    }

    public void copyHandle(ActionEvent event) {
        if (IdCardUtil.isNotEmpty(resultArea.getText())) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(resultArea.getText());
            clipboard.setContent(content);
            Toast.success("已复制到剪切板");
        } else {
            Toast.warning("没有解析的数据");
        }
    }

    public void resetHandle(ActionEvent event) {
        idCardField.setText(Const.EMPTY_STR);
        resultArea.setText(Const.EMPTY_STR);
        Toast.success("重置成功");
    }

}
