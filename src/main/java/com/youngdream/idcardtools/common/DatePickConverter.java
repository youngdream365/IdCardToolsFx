package com.youngdream.idcardtools.common;

import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 时间选择器格式化
 *
 * @author YoungDream
 **/
public class DatePickConverter extends StringConverter<LocalDate> {
    final DateTimeFormatter df = DateTimeFormatter.ofPattern(Const.YYYYMMDD_PATTREN);

    @Override
    public String toString(LocalDate date) {
        return date != null ? df.format(date) : Const.EMPTY_STR;
    }

    @Override
    public LocalDate fromString(String string) {
        return (string != null && !string.isEmpty()) ? LocalDate.parse(string, df) : null;
    }

}