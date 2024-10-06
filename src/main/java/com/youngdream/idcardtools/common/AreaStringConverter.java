package com.youngdream.idcardtools.common;

import com.youngdream.idcardtools.entity.Area;
import javafx.util.StringConverter;

/**
 * 下拉框字符格式转换
 *
 * @author YoungDream
 */
public class AreaStringConverter extends StringConverter<Area> {
    @Override
    public String toString(Area area) {
        return (area == null) ? Const.EMPTY_STR : area.getName();
    }

    @Override
    public Area fromString(String string) {
        throw new UnsupportedOperationException("fromString method is not supported.");
    }
}
