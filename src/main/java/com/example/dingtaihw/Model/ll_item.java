package com.example.dingtaihw.Model;

public class ll_item {
    private String serialno;
    private String user;
    private String type;
    private String requestid;

    public ll_item(String serialno, String user, String type,String rid) {
        this.serialno = serialno;
        this.user = user;
        this.type = type;
        this.requestid=rid;
    }

    public ll_item() {

    }

    @Override
    public String toString() {
        return "ll_item{" +
                "serialno='" + serialno + '\'' +
                ", user='" + user + '\'' +
                ", type='" + type + '\'' +
                ", requestid='" + requestid + '\'' +
                '}';
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
