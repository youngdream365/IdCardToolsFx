package com.youngdream.idcardtools.controller;

import com.youngdream.idcardtools.common.Const;
import com.youngdream.idcardtools.common.Toast;
import com.youngdream.idcardtools.utils.ImgUtil;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 主界面控制器
 *
 * @author YoungDream
 */
public class MainController implements Initializable {
    @FXML
    public BorderPane mainWindow;

    @FXML
    public TabPane rootTabPane;

    @FXML
    public ImageView windowIconImg;

    @FXML
    public Label titleLabel;

    @FXML
    public Button onTopBtn;

    @FXML
    public ImageView onTopImg;

    @FXML
    public Button onMinimizeBtn;

    @FXML
    public ImageView onMinimizeImg;

    @FXML
    public Button onCloseBtn;

    @FXML
    public ImageView onCloseImg;

    @FXML
    private GridPane creatorTabContent;

    @FXML
    private GridPane analyzerTabContent;

    @FXML
    private GridPane batchAnalyzerTabContent;

    private boolean isOnTop = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 初始化标题栏按钮图片
        windowIconImg.setImage(ImgUtil.WINDOW_ICON);
        onTopImg.setImage(ImgUtil.UNPIN_ICON);
        onMinimizeImg.setImage(ImgUtil.MINIMIZE_ICON);
        onCloseImg.setImage(ImgUtil.CLOSE_ICON);
        Tooltip.install(onTopBtn, new Tooltip("置顶窗口"));

        // 监听窗口置顶按钮
        onTopBtn.setOnAction((event) -> {
            Stage stage = (Stage) onTopBtn.getScene().getWindow();
            isOnTop = !isOnTop;
            stage.setAlwaysOnTop(isOnTop);
            onTopImg.setImage(isOnTop ? ImgUtil.PIN_ICON : ImgUtil.UNPIN_ICON);
        });

        // 监听最小化按钮
        onMinimizeBtn.setOnAction((e) -> {
            Stage stage = (Stage) onMinimizeBtn.getScene().getWindow();
            stage.setIconified(true);
        });

        // 监听关闭按钮
        onCloseBtn.setOnAction((e) -> {
            Stage stage = (Stage) onCloseBtn.getScene().getWindow();
            // 不支持系统托盘直接退出
            if (SystemTray.isSupported()) {
                Toast.info("隐藏到系统托盘");
                stage.hide();
            } else {
                stage.close();
                Platform.exit();
            }
        });

        // 彩蛋，双击标题栏显示版本
        titleLabel.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Toast.info(Const.APP_ABOUT, 5000);
            }
        });

    }

}

