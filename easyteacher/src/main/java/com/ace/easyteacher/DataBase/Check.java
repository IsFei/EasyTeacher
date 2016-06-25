package com.ace.easyteacher.DataBase;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "check")
public class Check extends BaseBean{
    @Column(name = "sid")
    private long sid;
    @Column(name = "content")
    private String content;
    @Column(name = "time")
    private String time;

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
