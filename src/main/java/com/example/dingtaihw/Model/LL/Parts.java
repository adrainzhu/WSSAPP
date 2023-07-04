package com.example.dingtaihw.Model.LL;

public class Parts {
    private String lh;//料号
    private String info;//描述
    private String location;//架位
    private String pno;//批号

    public Parts(String lh, String info, String location, String pno) {
        this.lh = lh;
        this.info = info;
        this.location = location;
        this.pno = pno;
    }

    public Parts() {
    }

    public String getLh() {
        return lh;
    }

    public void setLh(String lh) {
        this.lh = lh;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }
}
