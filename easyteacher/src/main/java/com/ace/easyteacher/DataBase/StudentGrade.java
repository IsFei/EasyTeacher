package com.ace.easyteacher.DataBase;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "studentGrade")
public class StudentGrade extends BaseBean {
    @Column(name = "sid")
    private long sid;
    @Column(name = "yw")
    private float yw;
    @Column(name = "sx")
    private float sx;
    @Column(name = "wy")
    private float wy;
    @Column(name = "zz")
    private float zz;
    @Column(name = "ls")
    private float ls;
    @Column(name = "dl")
    private float dl;
    @Column(name = "wl")
    private float wl;
    @Column(name = "hx")
    private float hx;
    @Column(name = "sw")
    private float sw;
    @Column(name = "ty")
    private float ty;
    @Column(name = "rank_class")
    private String rank_class;
    @Column(name = "rank_school")
    private String rank_school;
    @Column(name = "class_name")
    private String class_name;
    @Column(name = "type_id")
    private String type_id;

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public float getYw() {
        return yw;
    }

    public void setYw(float yw) {
        this.yw = yw;
    }

    public float getSx() {
        return sx;
    }

    public void setSx(float sx) {
        this.sx = sx;
    }

    public float getWy() {
        return wy;
    }

    public void setWy(float wy) {
        this.wy = wy;
    }

    public float getZz() {
        return zz;
    }

    public void setZz(float zz) {
        this.zz = zz;
    }

    public float getLs() {
        return ls;
    }

    public void setLs(float ls) {
        this.ls = ls;
    }

    public float getDl() {
        return dl;
    }

    public void setDl(float dl) {
        this.dl = dl;
    }

    public float getWl() {
        return wl;
    }

    public void setWl(float wl) {
        this.wl = wl;
    }

    public float getHx() {
        return hx;
    }

    public void setHx(float hx) {
        this.hx = hx;
    }

    public float getSw() {
        return sw;
    }

    public void setSw(float sw) {
        this.sw = sw;
    }

    public float getTy() {
        return ty;
    }

    public void setTy(float ty) {
        this.ty = ty;
    }

    public String getRank_class() {
        return rank_class;
    }

    public void setRank_class(String rank_class) {
        this.rank_class = rank_class;
    }

    public String getRank_school() {
        return rank_school;
    }

    public void setRank_school(String rank_school) {
        this.rank_school = rank_school;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }
}
