package com.youngdream.idcardtools.common;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * xml整型和字符串的适配器
 *
 * @author YoungDream
 */
public class XmlIntegerAdapter extends XmlAdapter<String, Integer> {

    @Override
    public Integer unmarshal(String v) {
        if (v == null || v.length() == 0) {
            return null;
        }
        return Integer.parseInt(v);
    }

    @Override
    public String marshal(Integer v) {
        return v == null ? "" : String.valueOf(v);
    }
}
