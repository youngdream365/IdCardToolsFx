package com.youngdream.idcardtools.common;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * 气泡消息
 *
 * @author YoungDream
 **/
public final class Toast {
    /**
     * Toast 气泡透明度
     */
    private static final double TOAST_OPACITY = 0.8D;

    /**
     * 默认消息最大长度（超出部分会被省略）
     */
    private static final int DEFAULT_MSG_MAX_LENGTH = 200;

    /**
     * 默认展示和淡入淡出动画时间；
     * 三者取值范围在：[100,10000] ms
     */
    private static final int DEFAULT_TOAST_DELAY = 1500;
    private static final int DEFAULT_FADE_IN_DELAY = 250;
    private static final int DEFAULT_FADE_OUT_DELAY = 250;

    /**
     * Toast 依赖的主窗口
     */
    private static Stage ownerStage;

    /**
     * Toast 偏移量，X横轴方向，Y纵轴方向，正数向右/下，负数反之，
     * (0,0) 表示相对定位在 ownerStage 窗口的正中间
     */
    private static int offsetX = 0;
    private static int offsetY = 0;

    /**
     * Toast 类型：INFO 默认消息；SUCCESS 成功消息；WARNING 警告消息；ERROR 错误消息；
     */
    private enum MsgType {INFO, SUCCESS, WARNING, ERROR}

    /**
     * Toast 初始化设置
     */
    public static void initialize(Stage stage) {
        ownerStage = stage;
    }

    /**
     * Toast 初始化设置
     *
     * @param stage   Toast 相对定位的 Stage
     * @param xOffset 相对 OwnerStage 窗口中心的横轴偏移像素量，默认为0，正数向右，负数反之
     * @param yOffset 相对 OwnerStage 窗口中心的纵轴偏移像素量，默认为0，正数向下，负数反之
     */
    public static void initialize(Stage stage, int xOffset, int yOffset) {
        ownerStage = stage;
        offsetX = xOffset;
        offsetY = yOffset;
    }

    /**
     * 展示消息气泡
     *
     * @param msgType      消息类型
     * @param msg          消息文本
     * @param delay        延迟展示时间（单位：ms）
     * @param fadeInDelay  淡入动画时间（单位：ms）
     * @param fadeOutDelay 淡出动画时间（单位：ms）
     */
    private static void newToastPop(MsgType msgType, String msg, int delay, int fadeInDelay, int fadeOutDelay) {
        // 参数校验，主窗口配置
        if (ownerStage == null) {
            throw new IllegalStateException("Toast's ownerStage is not initialized!");
        }
        // 三者取值范围在：[100,10000]
        if (delay < 100 || delay > 10000
                || fadeInDelay < 100 || fadeInDelay > 10000
                || fadeOutDelay < 100 || fadeOutDelay > 10000) {
            throw new IllegalArgumentException("delay fadeInDelay and fadeOutDelay should be between 100 and 10000!");
        }

        // JavaFXUI操作只能在JavaFXUI线程中执行，其他线程调用会抛错，可使用Platform.runLater()
        Platform.runLater(() -> {
            Stage toastStage = new Stage();
            toastStage.initStyle(StageStyle.TRANSPARENT);
            toastStage.setAlwaysOnTop(true);
            toastStage.initOwner(ownerStage);
            toastStage.setResizable(false);

            // 禁用 toastStage 的所有输入事件（防止 Toast 气泡弹出时 primaryStage 失去焦点）
            toastStage.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    Platform.runLater(() -> ownerStage.requestFocus());
                }
            });

            HBox root = getMsgBox(msg, msgType);

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            toastStage.setScene(scene);
            toastStage.show();

            // show()未完成时宽高可能为0，延迟执行位置计算确保宽高已初始化。
            Platform.runLater(() -> calcToastPosition(toastStage));

            // 淡入动画
            Timeline fadeInTimeline = new Timeline(
                    // 关键帧，持续时间，透明度变化，cur 0-->1
                    new KeyFrame(
                            Duration.millis(fadeInDelay),
                            new KeyValue(root.opacityProperty(), TOAST_OPACITY)
                    )
            );
            fadeInTimeline.play();

            // 延迟后淡出
            Timeline delayTimeline = new Timeline(
                    // 关键帧，持续时间，透明度变化，cur 1-->0
                    new KeyFrame(Duration.millis(delay), e -> {
                        Timeline fadeOutTimeline = new Timeline();
                        fadeOutTimeline.getKeyFrames().add(new KeyFrame(
                                Duration.millis(fadeOutDelay),
                                new KeyValue(root.opacityProperty(), 0)
                        ));
                        // 注册结束关闭气泡事件
                        fadeOutTimeline.setOnFinished(event -> toastStage.close());
                        fadeOutTimeline.play();
                    })
            );
            delayTimeline.play();
        });
    }

    /**
     * 根据不同消息类型构建样式
     *
     * @param msg     消息文本
     * @param msgType 消息类型
     * @return 气泡样式内容
     */
    private static HBox getMsgBox(String msg, MsgType msgType) {
        // 获取屏幕的尺寸信息
        Screen screen = Screen.getScreensForRectangle(
                ownerStage.getX(), ownerStage.getY(),
                ownerStage.getWidth(), ownerStage.getHeight()
        ).get(0);
        double screenWidth = screen.getVisualBounds().getWidth();
        double screenHeight = screen.getVisualBounds().getHeight();
        double maxWidth = screenWidth / 4;
        double maxHeight = screenHeight / 4;

        // 使用 getTruncatedText() 对文本进行截断
        String wrappedText = getWrappedText(msg);

        HBox root = new HBox();
        Text text = new Text();
        text.setStyle("-fx-font-weight:bold;-fx-font-size: 18px");
        text.setText(wrappedText);

        // 使用 TextFlow 控制换行
        TextFlow textFlow = new TextFlow(text);
        textFlow.setMaxWidth(maxWidth);
        textFlow.setMaxHeight(maxHeight);

        String commonStyle = "-fx-border-radius: 10; -fx-background-radius: 10; -fx-border-style: solid inside; -fx-background-color: %s; -fx-border-color: %s;";
        String bgColor, borderColor;
        Color textColor;
        switch (msgType) {
            case WARNING:
                bgColor = "rgba(253,246,236,1)";
                borderColor = "rgba(230,162,60,1)";
                textColor = Color.rgb(230, 162, 60, 1);
                break;
            case ERROR:
                bgColor = "rgba(254,240,240,1)";
                borderColor = "rgba(245,108,108,1)";
                textColor = Color.rgb(245, 108, 108, 1);
                break;
            case SUCCESS:
                bgColor = "rgba(240,249,235,1)";
                borderColor = "rgba(103,194,58,1)";
                textColor = Color.rgb(103, 194, 58, 1);
                break;
            case INFO:
            default:
                bgColor = "rgba(237,242,252,1)";
                borderColor = "rgba(144,147,153,1)";
                textColor = Color.rgb(144, 147, 153, 1);
                break;
        }
        root.setStyle(String.format(commonStyle, bgColor, borderColor));
        text.setFill(textColor);
        root.getChildren().addAll(textFlow);
        root.setPadding(new Insets(10, 20, 10, 20));
        root.setAlignment(Pos.BASELINE_CENTER);
        root.setSpacing(10);
        root.setOpacity(0);
        return root;
    }

    /**
     * 消息字符串压缩裁剪
     */
    public static String getWrappedText(String text) {
        if (text == null || text.isEmpty()) {
            return Const.EMPTY_STR;
        }
        text = text.replaceAll("[\\r\\n\\t]+", " ").replaceAll("\\s+", " ").trim();
        if (text.length() > DEFAULT_MSG_MAX_LENGTH) {
            return text.substring(0, DEFAULT_MSG_MAX_LENGTH) + "...";
        }
        return text;
    }

    /**
     * 计算 Toast 出现的中心位置
     */
    private static void calcToastPosition(Stage toastStage) {
        double toastWidth = toastStage.getWidth();
        double toastHeight = toastStage.getHeight();
        double x = Toast.offsetX + ownerStage.getX() + (ownerStage.getWidth() - toastWidth) / 2;
        double y = Toast.offsetY + ownerStage.getY() + (ownerStage.getHeight() - toastHeight) / 2;

        // 获取 OwnerStage 当前所在的屏幕（适配多块拓展屏情况）
        Screen screen = Screen.getScreensForRectangle(
                ownerStage.getX(), ownerStage.getY(),
                ownerStage.getWidth(), ownerStage.getHeight()
        ).get(0);
        double screenMinX = screen.getVisualBounds().getMinX();
        double screenMinY = screen.getVisualBounds().getMinY();
        double screenWidth = screen.getVisualBounds().getWidth();
        double screenHeight = screen.getVisualBounds().getHeight();

        // 保证气泡显示在屏幕范围内
        x = Math.max(screenMinX, Math.min(x, screenMinX + screenWidth - toastWidth));
        y = Math.max(screenMinY, Math.min(y, screenMinY + screenHeight - toastHeight));

        toastStage.setX(x);
        toastStage.setY(y);
    }

    /* 下面都是四个级别消息的重载方法。（单位皆为：ms） **/

    /**
     * info 默认消息
     */
    public static void info(String msg, int delay, int fadeInDelay, int fadeOutDelay) {
        newToastPop(MsgType.INFO, msg, delay, fadeInDelay, fadeOutDelay);
    }

    public static void info(String msg, int delay) {
        newToastPop(MsgType.INFO, msg, delay, DEFAULT_FADE_IN_DELAY, DEFAULT_FADE_OUT_DELAY);
    }

    public static void info(String msg) {
        newToastPop(MsgType.INFO, msg, DEFAULT_TOAST_DELAY, DEFAULT_FADE_IN_DELAY, DEFAULT_FADE_OUT_DELAY);
    }

    /**
     * success 成功消息
     */
    public static void success(String msg, int delay, int fadeInDelay, int fadeOutDelay) {
        newToastPop(MsgType.SUCCESS, msg, delay, fadeInDelay, fadeOutDelay);
    }

    public static void success(String msg, int delay) {
        newToastPop(MsgType.SUCCESS, msg, delay, DEFAULT_FADE_IN_DELAY, DEFAULT_FADE_OUT_DELAY);
    }

    public static void success(String msg) {
        newToastPop(MsgType.SUCCESS, msg, DEFAULT_TOAST_DELAY, DEFAULT_FADE_IN_DELAY, DEFAULT_FADE_OUT_DELAY);
    }

    /**
     * 警告消息
     */
    public static void warning(String msg, int delay, int fadeInDelay, int fadeOutDelay) {
        newToastPop(MsgType.WARNING, msg, delay, fadeInDelay, fadeOutDelay);
    }

    public static void warning(String msg, int delay) {
        newToastPop(MsgType.WARNING, msg, delay, DEFAULT_FADE_IN_DELAY, DEFAULT_FADE_OUT_DELAY);
    }

    public static void warning(String msg) {
        newToastPop(MsgType.WARNING, msg, DEFAULT_TOAST_DELAY, DEFAULT_FADE_IN_DELAY, DEFAULT_FADE_OUT_DELAY);
    }

    /**
     * 错误消息
     */
    public static void error(String msg, int delay, int fadeInDelay, int fadeOutDelay) {
        newToastPop(MsgType.ERROR, msg, delay, fadeInDelay, fadeOutDelay);
    }

    public static void error(String msg, int delay) {
        newToastPop(MsgType.ERROR, msg, delay, DEFAULT_FADE_IN_DELAY, DEFAULT_FADE_OUT_DELAY);
    }

    public static void error(String msg) {
        newToastPop(MsgType.ERROR, msg, DEFAULT_TOAST_DELAY, DEFAULT_FADE_IN_DELAY, DEFAULT_FADE_OUT_DELAY);
    }

}
