package com.ace.easyteacher.DataBase;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "studentInfo")
public class StudentInfo extends BaseBean {
    @Column(name = "sid")
    private long sid;
    @Column(name = "name")
    private String name;
    @Column(name = "position")
    private String position;
    @Column(name = "sex")
    private String sex;
    @Column(name = "birthday")
    private String birthday;
    @Column(name = "father")
    private String father;
    @Column(name = "mother")
    private String mother;
    @Column(name = "father_tel")
    private String father_tel;
    @Column(name = "mother_tel")
    private String mother_tel;
    @Column(name = "hobby")
    private String hobby;
    @Column(name = "class_name")
    private String class_name;
    @Column(name = "url")
    private String url;

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getFather_tel() {
        return father_tel;
    }

    public void setFather_tel(String father_tel) {
        this.father_tel = father_tel;
    }

    public String getMother_tel() {
        return mother_tel;
    }

    public void setMother_tel(String mother_tel) {
        this.mother_tel = mother_tel;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
