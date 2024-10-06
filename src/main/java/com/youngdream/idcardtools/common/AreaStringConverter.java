package com.youngdream.idcardtools.common;

import com.youngdream.idcardtools.entity.Area;
import javafx.util.StringConverter;

/**
 * 下拉框字符格式转换
 *
 * @author YoungDream
 */
public class AreaStringConverter<T> extends StringConverter<T> {
    @Override
    public String toString(Object object) {
        Area s = (Area) object;
        return s.getName();
    }

    @Override
    public T fromString(String string) {
        return null;
    }
}
