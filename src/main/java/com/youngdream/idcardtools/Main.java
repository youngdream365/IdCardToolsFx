package com.youngdream.idcardtools;

import com.youngdream.idcardtools.common.Const;
import com.youngdream.idcardtools.common.Toast;
import com.youngdream.idcardtools.utils.ImgUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;


/**
 * 入口类
 *
 * @author YoungDream
 */
public class Main extends Application {
    private double xOffset = 0.0D;
    private double yOffset = 0.0D;
    private TrayIcon trayIcon;
    private static Stage primaryStage;

    public Main() {
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public void start(Stage stage) {
        primaryStage = stage;
        Toast.initialize(primaryStage, 0, 100);

        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/Main.fxml"));
            Parent root = loader.load();
            primaryStage.getIcons().add(ImgUtil.SYS_BAR_ICON);
            primaryStage.setTitle("身份证助手");
            primaryStage.centerOnScreen();
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setResizable(false);
            primaryStage.setOnCloseRequest((event) -> {
                Platform.runLater(() -> {
                    Toast.info("隐藏到系统托盘");
                });
            });
            root.setOnMousePressed((event) -> {
                this.xOffset = event.getSceneX();
                this.yOffset = event.getSceneY();
            });
            root.setOnMouseDragged((event) -> {
                primaryStage.setX(event.getScreenX() - this.xOffset);
                primaryStage.setY(event.getScreenY() - this.yOffset);
            });
            primaryStage.setScene(new Scene(root, 502.0D, 540.0D));
            Platform.setImplicitExit(false);
            primaryStage.show();
            primaryStage.toFront();
            primaryStage.requestFocus();
            this.setTray(primaryStage);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    private void setTray(Stage primaryStage) throws AWTException {
        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            Image awtImage = ImgUtil.convertFXImage2AWTImage(ImgUtil.SYS_BAR_ICON);
            PopupMenu menu = new PopupMenu();
            MenuItem showItem = new MenuItem("show");
            MenuItem exitItem = new MenuItem("exit");
            menu.add(showItem);
            menu.add(exitItem);
            this.trayIcon = new TrayIcon(awtImage, "身份证助手", menu);
            this.trayIcon.setImageAutoSize(true);
            systemTray.add(this.trayIcon);
            this.trayIcon.addActionListener((e) -> {
                Platform.runLater(() -> {
                    primaryStage.setIconified(false);
                    primaryStage.show();
                    primaryStage.toFront();
                    primaryStage.requestFocus();
                });
            });
            showItem.addActionListener((e) -> {
                Platform.runLater(() -> {
                    primaryStage.setIconified(false);
                    primaryStage.show();
                    primaryStage.toFront();
                    primaryStage.requestFocus();
                });
            });
            exitItem.addActionListener((e) -> {
                Platform.setImplicitExit(true);
                Platform.runLater(() -> {
                    primaryStage.close();
                    systemTray.remove(this.trayIcon);
                    Platform.exit();
                });
            });
        } else {
            Toast.error("系统不支持托盘");
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}