package com.example.dingtaihw.Model.Out;

public class Out_LotDetail {
    private String MaterialNo;
    private String Lotid;
    private String Lotnum;
    private String Productid;
    private String WuliuNo;

    public String getWuliuNo() {
        return WuliuNo;
    }

    public void setWuliuNo(String wuliuNo) {
        WuliuNo = wuliuNo;
    }

    public Out_LotDetail(String materialNo, String lotid, String lotnum, String productid, String wuliuNo) {
        MaterialNo = materialNo;
        Lotid = lotid;
        Lotnum = lotnum;
        Productid = productid;
        WuliuNo = wuliuNo;
    }

    public Out_LotDetail(String materialNo, String lotid, String lotnum, String productid) {
        MaterialNo = materialNo;
        Lotid = lotid;
        Lotnum = lotnum;
        Productid = productid;
    }

    public Out_LotDetail() {
    }

    public String getMaterialNo() {
        return MaterialNo;
    }

    public void setMaterialNo(String materialNo) {
        MaterialNo = materialNo;
    }

    public String getLotid() {
        return Lotid;
    }

    public void setLotid(String lotid) {
        Lotid = lotid;
    }

    public String getLotnum() {
        return Lotnum;
    }

    public void setLotnum(String lotnum) {
        Lotnum = lotnum;
    }

    public String getProductid() {
        return Productid;
    }

    public void setProductid(String productid) {
        Productid = productid;
    }
}
