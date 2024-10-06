package com.youngdream.idcardtools.entity;

import com.youngdream.idcardtools.common.XmlIntegerAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 行政区划节点
 *
 * @author YoungDream
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Area implements Comparable<Area> {
    @XmlAttribute
    @XmlJavaTypeAdapter(XmlIntegerAdapter.class)
    private Integer id;

    @XmlAttribute
    @XmlJavaTypeAdapter(XmlIntegerAdapter.class)
    private Integer pid;

    @XmlAttribute
    private String idpath;

    @XmlAttribute
    @XmlJavaTypeAdapter(XmlIntegerAdapter.class)
    private Integer code;

    @XmlAttribute
    private Integer pcode;

    @XmlAttribute
    private String codepath;

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String fullname;

    @XmlAttribute
    private String namepath;

    @XmlAttribute
    @XmlJavaTypeAdapter(XmlIntegerAdapter.class)
    private Integer depth;

    @XmlAttribute
    private String lowestdepth;

    @Override
    public String toString() {
        return "Area{" +
                "id=" + id +
                ", pid=" + pid +
                ", idpath='" + idpath + '\'' +
                ", code=" + code +
                ", pcode=" + pcode +
                ", codepath='" + codepath + '\'' +
                ", name='" + name + '\'' +
                ", fullname='" + fullname + '\'' +
                ", namepath='" + namepath + '\'' +
                ", depth=" + depth +
                ", lowestdepth='" + lowestdepth + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getIdpath() {
        return idpath;
    }

    public void setIdpath(String idpath) {
        this.idpath = idpath;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getPcode() {
        return pcode;
    }

    public void setPcode(Integer pcode) {
        this.pcode = pcode;
    }

    public String getCodepath() {
        return codepath;
    }

    public void setCodepath(String codepath) {
        this.codepath = codepath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getNamepath() {
        return namepath;
    }

    public void setNamepath(String namepath) {
        this.namepath = namepath;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public String getLowestdepth() {
        return lowestdepth;
    }

    public void setLowestdepth(String lowestdepth) {
        this.lowestdepth = lowestdepth;
    }

    /**
     * 两种比较器，一种是实现Comparable接口，实现compareTo方法（直接实现方法）
     * 另一种是新建一个类继承函数式Comparator接口实现compare()（单独类重写方法）
     * 算法：https://blog.csdn.net/hanshileiai/article/details/6706187
     * https://www.cnblogs.com/gmq-sh/p/4928721.html
     * <p>
     * 北京 1 ，澳门 34 -> 升序
     * x.compareTo(y);
     * (x < y) ? -1 : ((x == y) ? 0 : 1);
     * 1：表示大于,-1：表示小于,0：表示相等
     */
    @Override
    public int compareTo(Area o) {
        return this.id.compareTo(o.getId());
    }
}
