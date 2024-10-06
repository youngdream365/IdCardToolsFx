package com.youngdream.idcardtools.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 主界面
 *
 * @author YoungDream
 */
public class MainController implements Initializable {
    @FXML
    public TabPane rootTabPane;
    /**
     * analyzer 解析器Tab页控件
     */
    @FXML
    private GridPane analyzerTabContent;

    /**
     * creator 生成器Tab页控件
     */
    @FXML
    private GridPane creatorTabContent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
