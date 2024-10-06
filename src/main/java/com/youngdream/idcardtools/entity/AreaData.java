package com.youngdream.idcardtools.entity;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 行政区划数据包
 *
 * @author YoungDream
 **/
@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class AreaData {
    private Comment comment;

    @XmlElementWrapper(name = "areas")
    private List<Area> area;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public List<Area> getArea() {
        return area;
    }

    public void setArea(List<Area> area) {
        this.area = area;
    }
}
