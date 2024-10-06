package com.youngdream.idcardtools;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * 入口类
 *
 * @author YoungDream
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/IdCardToolsMain.fxml"));
        //标题栏
        primaryStage.setTitle("身份证助手");
        //设置窗口大小
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.centerOnScreen();
        primaryStage.initStyle(StageStyle.DECORATED);
        //设置窗口大小是否可调整
        primaryStage.setResizable(false);
        //窗口图标
        primaryStage.getIcons().add(new Image(getClass().getResource("/img/icon.png").toString()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
