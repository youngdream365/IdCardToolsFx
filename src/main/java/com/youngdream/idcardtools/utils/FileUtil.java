package com.youngdream.idcardtools.utils;

import com.youngdream.idcardtools.common.Toast;
import com.youngdream.idcardtools.entity.IdCardInfo;
import com.youngdream.idcardtools.service.AnalyzerService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 文件处理工具类
 *
 * @author YoungDream
 **/
public class FileUtil {
    public static void process2Csv(File inFile, AnalyzerService service) {
        String outPath = inFile.getParentFile().getAbsolutePath() + File.separator + inFile.getName().replaceFirst("\\.(txt|csv)$", "_" + ComUtil.simpleUUID() + ".$1");
        AtomicInteger counter = new AtomicInteger(0);
        try (BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), StandardCharsets.UTF_8));
             BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPath), StandardCharsets.UTF_8))) {
            bfw.write("导入身份证号,转换后身份证号,是否二代证,行政区划,性别,出生日期,当前年龄（周岁）,属相,星座,行政区划版本,解析结果,解析时间");
            bfw.newLine();
            String line;
            while ((line = bfr.readLine()) != null) {
                String idcard = line.trim();
                IdCardInfo csvLine = service.getIdCardInfo(idcard);
                bfw.write(csvLine.toCSV());
                bfw.newLine();
                counter.incrementAndGet();
            }
        } catch (Exception e) {
            Toast.error("处理文件时出错");
        }
        Toast.success("共处理了 " + counter.get() + " 行", 2000);
    }

    public static void process2Json(File inFile, AnalyzerService service) {
        String outPath = inFile.getParentFile().getAbsolutePath() + File.separator
                + inFile.getName().replaceFirst("\\.(txt|csv)$", "_" + ComUtil.simpleUUID() + ".json");
        AtomicInteger counter = new AtomicInteger(0);
        try(BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), StandardCharsets.UTF_8));
            BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPath), StandardCharsets.UTF_8))) {
            boolean first = true;
            bfw.write("[");
            bfw.newLine();
            String line;
            while ((line = bfr.readLine()) != null) {
                String idcard = line.trim();
                IdCardInfo csvLine = service.getIdCardInfo(idcard);
                if (first) {
                    first = false;
                } else {
                    bfw.write(",");
                }
                bfw.write(csvLine.toJson());
                bfw.newLine();
                counter.incrementAndGet();
            }
            bfw.write("]");
        } catch (Exception e) {
            Toast.error("处理文件时出错");
        }
        Toast.success("共处理了 " + counter.get() + " 行", 2000);
    }

}
