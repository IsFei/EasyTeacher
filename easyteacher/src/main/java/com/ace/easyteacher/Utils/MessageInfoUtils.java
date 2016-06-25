package com.ace.easyteacher.Utils;

import java.io.Serializable;

/**
 * Created by lenovo on 2016-4-8.
 */
public class MessageInfoUtils implements Serializable {
    private String name;
    private String num;
    private boolean isCheck=false;

    public MessageInfoUtils(String name, String num) {
        this.name = name;
        this.num = num;
    }

    public boolean getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
