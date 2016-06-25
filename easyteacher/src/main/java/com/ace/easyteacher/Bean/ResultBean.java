package com.ace.easyteacher.Bean;


public class ResultBean {

    private String info;
    private String status;
    private String data;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return status.equals("success");
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

