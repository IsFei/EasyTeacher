package com.ace.easyteacher.DataBase;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

@Table(name = "teacherInfo")
public class TeacherInfo extends BaseBean{
    @Column(name = "name")
    private String name;
    @Column(name = "tel")
    private String tel;
    @Column(name = "job_number")
    private String job_number;
    @Column(name = "adr")
    private String adr;
    @Column(name = "password")
    private String password;
    @Column(name = "url")
    private String url;
    @Column(name = "class_name")
    private String class_name;

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getJob_number() {
        return job_number;
    }

    public void setJob_number(String job_number) {
        this.job_number = job_number;
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
