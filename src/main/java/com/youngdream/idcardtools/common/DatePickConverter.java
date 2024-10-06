package com.youngdream.idcardtools.common;

import com.youngdream.idcardtools.entity.Constant;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 时间选择器格式化
 *
 * @author YoungDream
 **/
public class DatePickConverter extends StringConverter<LocalDate> {
    final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constant.YYYYMMDD_PATTREN);

    @Override
    public String toString(LocalDate date) {
        return date != null ? dateFormatter.format(date) : "";
    }

    @Override
    public LocalDate fromString(String string) {
        return (string != null && !string.isEmpty()) ? LocalDate.parse(string, dateFormatter) : null;
    }
}
