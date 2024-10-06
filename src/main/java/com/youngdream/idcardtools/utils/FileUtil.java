package com.youngdream.idcardtools.utils;

import com.youngdream.idcardtools.entity.Constant;
import com.youngdream.idcardtools.service.AnalyzerService;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 文件处理工具
 *
 * @author YoungDream
 **/
public class FileUtil {
    public static void processIdCardTxt(File inFile, Stage parentStage, AnalyzerService service) {
        String outPath = inFile.getParentFile().getAbsolutePath() + File.separator + inFile.getName().replace(".txt", "_")
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) + ".txt";
        // 计数器
        int counter = 0;
        // 源文件
        BufferedReader bfr = null;
        // 目标文件
        BufferedWriter bfw = null;
        try {
            bfr = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "GBK"));
            bfw = new BufferedWriter(new FileWriter(outPath));
            String line;
            while ((line = bfr.readLine()) != null) {
                String curLine = line.trim();
                // 长度满足身份证号才处理，其他跳过
                if (curLine.length() == 15 || curLine.length() == 18) {
                    bfw.write(line + " --> " + service.getIdCardInfoLine(curLine));
                } else {
                    bfw.write(line);
                }
                bfw.newLine();
                counter++;
            }
            bfw.write(new StringBuilder()
                    .append(System.lineSeparator()).append("_______________________________________________________")
                    .append(System.lineSeparator()).append("解析时间：")
                    .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                    .append(System.lineSeparator()).append("行政区划版本：")
                    .append(Constant.areaData2020.getComment().getSource()).toString());
        } catch (Exception e) {
            Toast.error(parentStage, "写入文件失败");
            return;
        } finally {
            try {
                bfr.close();
                bfw.close();
            } catch (IOException e) {
                Toast.error(parentStage, "关闭文件失败");
                return;
            }
        }
        Toast.success(parentStage, "共处理 " + counter + " 行", 5000);
    }

}
