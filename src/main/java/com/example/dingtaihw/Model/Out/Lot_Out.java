package com.example.dingtaihw.Model.Out;

public class Lot_Out {
    private  String lcbh;
    private String sqr;
    private String dn;
    private String requestid;

    public Lot_Out(String lcbh, String sqr, String dn, String requestid) {
        this.lcbh = lcbh;
        this.sqr = sqr;
        this.dn = dn;
        this.requestid = requestid;
    }
    public String getLcbh() {
        return lcbh;
    }

    public void setLcbh(String lcbh) {
        this.lcbh = lcbh;
    }

    public String getSqr() {
        return sqr;
    }

    public void setSqr(String sqr) {
        this.sqr = sqr;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }
}
