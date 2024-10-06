package com.youngdream.idcardtools.utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * 气泡消息
 *
 * @author YoungDream
 **/
public final class Toast {
    private static final int DEFAULT_FADE_IN_DELAY = 250;
    private static final int DEFAULT_FADE_OUT_DELAY = 250;
    private static final int DEFAULT_TOAST_DELAY = 1500;

    private enum MsgType {
        //消息
        INFO,
        //成功
        SUCCESS,
        //警告
        WARNING,
        //错误
        ERROR
    }

    private static void newToastPop(Stage ownerStage, String msg, int delay, int fadeInDelay, int fadeOutDelay, MsgType msgType) {
        // 参数校验
        if (delay <= 100 || fadeInDelay <= 100 || fadeOutDelay <= 100
                || delay >= 10000 || fadeInDelay >= 10000 || fadeOutDelay >= 10000) {
            throw new IllegalArgumentException("delay fadeInDelay or fadeOutDelay should be between 100 and 10000!");
        }

        Stage toastStage = new Stage();
        toastStage.setAlwaysOnTop(true);
        toastStage.initOwner(ownerStage);
        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        HBox root = new HBox();
        Text text = new Text(msg);
        text.setStyle("-fx-font-weight:bold;-fx-font-size: 18px");

        String commonHboxStyle = "-fx-border-radius: 10; -fx-background-radius: 10; -fx-border-style: solid inside;";
        switch (msgType) {
            case WARNING:
                root.setStyle(commonHboxStyle + "-fx-background-color:  rgba(253,246,236,1); -fx-border-color: rgb(230,162,60,1)");
                text.setFill(Color.rgb(230, 162, 60, 1));
                break;
            case ERROR:
                root.setStyle(commonHboxStyle + "-fx-background-color: rgba(254,240,240,1); -fx-border-color: rgba(245,108,108,1)");
                text.setFill(Color.rgb(245, 108, 108, 1));
                break;
            case SUCCESS:
                root.setStyle(commonHboxStyle + "-fx-background-color: rgba(240,249,235,1);-fx-border-color: rgba(103,194,58,1)");
                text.setFill(Color.rgb(103, 194, 58, 1));
                break;
            default:
                root.setStyle(commonHboxStyle + "-fx-background-color:  rgba(237,242,252,1);-fx-border-color: rgb(144,147,153,1)");
                text.setFill(Color.rgb(144, 147, 153, 1));
                break;
        }

        root.getChildren().addAll(text);
        root.setPadding(new Insets(10, 20, 10, 20));
        root.setAlignment(Pos.BASELINE_CENTER);
        // hbox元素间隔
        root.setSpacing(10);
        // 设置透明度
        root.setOpacity(0);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        toastStage.setScene(scene);
        toastStage.show();

        // 关键帧
        KeyFrame fadeInKey = new KeyFrame(
                //持续时间
                Duration.millis(fadeInDelay),
                // 透明的属性，cur 0--> 1
                new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 1)
        );
        Timeline fadeInTimeline = new Timeline();
        fadeInTimeline.getKeyFrames().add(fadeInKey);
        // 注册了一个延迟事件
        fadeInTimeline.setOnFinished(event -> new Thread(() -> {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Timeline fadeOutTimeline = new Timeline();
            KeyFrame fadeOutKey = new KeyFrame(
                    Duration.millis(fadeOutDelay),
                    // 透明的属性，cur 1 --> 0
                    new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 0)
            );
            fadeOutTimeline.getKeyFrames().add(fadeOutKey);
            fadeOutTimeline.setOnFinished((aeb) -> toastStage.close());
            fadeOutTimeline.play();
        }).start());
        fadeInTimeline.play();
    }

    // 成功消息
    public static void success(Stage ownerStage, String msg) {
        newToastPop(ownerStage, msg, DEFAULT_TOAST_DELAY, DEFAULT_FADE_IN_DELAY, DEFAULT_FADE_OUT_DELAY, MsgType.SUCCESS);
    }

    public static void success(Stage ownerStage, String msg, int delay) {
        newToastPop(ownerStage, msg, delay, DEFAULT_FADE_IN_DELAY, DEFAULT_FADE_OUT_DELAY, MsgType.SUCCESS);
    }

    public static void success(Stage ownerStage, String msg, int delay, int fadeInDelay, int fadeOutDelay) {
        newToastPop(ownerStage, msg, delay, fadeInDelay, fadeOutDelay, MsgType.SUCCESS);
    }

    // 警告消息
    public static void warning(Stage ownerStage, String msg) {
        newToastPop(ownerStage, msg, DEFAULT_TOAST_DELAY, DEFAULT_FADE_IN_DELAY, DEFAULT_FADE_OUT_DELAY, MsgType.WARNING);
    }

    public static void warning(Stage ownerStage, String msg, int delay) {
        newToastPop(ownerStage, msg, delay, DEFAULT_FADE_IN_DELAY, DEFAULT_FADE_OUT_DELAY, MsgType.WARNING);
    }

    public static void warning(Stage ownerStage, String msg, int delay, int fadeInDelay, int fadeOutDelay) {
        newToastPop(ownerStage, msg, delay, fadeInDelay, fadeOutDelay, MsgType.WARNING);
    }

    // 错误消息
    public static void error(Stage ownerStage, String msg) {
        newToastPop(ownerStage, msg, DEFAULT_TOAST_DELAY, DEFAULT_FADE_IN_DELAY, DEFAULT_FADE_OUT_DELAY, MsgType.ERROR);
    }

    public static void error(Stage ownerStage, String msg, int delay) {
        newToastPop(ownerStage, msg, delay, DEFAULT_FADE_IN_DELAY, DEFAULT_FADE_OUT_DELAY, MsgType.ERROR);
    }

    public static void error(Stage ownerStage, String msg, int delay, int fadeInDelay, int fadeOutDelay) {
        newToastPop(ownerStage, msg, delay, fadeInDelay, fadeOutDelay, MsgType.ERROR);
    }

    // 提示消息
    public static void info(Stage ownerStage, String msg) {
        newToastPop(ownerStage, msg, DEFAULT_TOAST_DELAY, DEFAULT_FADE_IN_DELAY, DEFAULT_FADE_OUT_DELAY, MsgType.INFO);
    }

    public static void info(Stage ownerStage, String msg, int delay) {
        newToastPop(ownerStage, msg, delay, DEFAULT_FADE_IN_DELAY, DEFAULT_FADE_OUT_DELAY, MsgType.INFO);
    }

    public static void info(Stage ownerStage, String msg, int delay, int fadeInDelay, int fadeOutDelay) {
        newToastPop(ownerStage, msg, delay, fadeInDelay, fadeOutDelay, MsgType.INFO);
    }
} 