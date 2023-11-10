package com.example.dingtaihw.Model.Out;

public class Out_Wuliu {
    private String Wuliuno;
    private String sqr;
    private String lcbh;
    private String requesid;



    public Out_Wuliu() {
    }

    public Out_Wuliu(String wuliuno, String sqr, String lcbh,String requesid) {
        Wuliuno = wuliuno;
        this.sqr = sqr;
        this.lcbh = lcbh;
        this.requesid=requesid;
    }

    public String getWuliuno() {
        return Wuliuno;
    }

    public void setWuliuno(String wuliuno) {
        Wuliuno = wuliuno;
    }

    public String getSqr() {
        return sqr;
    }

    public void setSqr(String sqr) {
        this.sqr = sqr;
    }

    public String getLcbh() {
        return lcbh;
    }

    public void setLcbh(String lcbh) {
        this.lcbh = lcbh;
    }
    public String getRequesid() {
        return requesid;
    }

    public void setRequesid(String requesid) {
        this.requesid = requesid;
    }
}
