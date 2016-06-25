package com.ace.easyteacher.DataBase;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "note")
public class Note extends BaseBean {
    @Column(name = "job_number")
    private String job_number;

    public String getJob_number() {
        return job_number;
    }

    public void setJob_number(String job_number) {
        this.job_number = job_number;
    }

    @Column(name = "create_time")

    private String create_time;
    @Column(name = "content")
    private String content;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
