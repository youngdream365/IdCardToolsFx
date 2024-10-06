package com.youngdream.idcardtools.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * 数据描述
 *
 * @author YoungDream
 **/
@XmlAccessorType(XmlAccessType.FIELD)
public class Comment {
    @XmlAttribute
    private String source;
    @XmlAttribute
    private String pubdate;
    @XmlAttribute(name = "statistical_deadline")
    private String statisticalDeadline;

    @Override
    public String toString() {
        return "Comment{" +
                "source='" + source + '\'' +
                ", pubdate='" + pubdate + '\'' +
                ", statisticalDeadline='" + statisticalDeadline + '\'' +
                '}';
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getStatisticalDeadline() {
        return statisticalDeadline;
    }

    public void setStatisticalDeadline(String statisticalDeadline) {
        this.statisticalDeadline = statisticalDeadline;
    }
}
