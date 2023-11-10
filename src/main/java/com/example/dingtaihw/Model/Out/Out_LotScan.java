package com.example.dingtaihw.Model.Out;

public class Out_LotScan {
    private String Lotid;
    private String check1;
    private String check2;

    public Out_LotScan(String lotid, String check1, String check2) {
        Lotid = lotid;
        this.check1 = check1;
        this.check2 = check2;
    }

    public Out_LotScan() {
    }

    public String getLotid() {
        return Lotid;
    }

    public void setLotid(String lotid) {
        Lotid = lotid;
    }

    public String getCheck1() {
        return check1;
    }

    public void setCheck1(String check1) {
        this.check1 = check1;
    }

    public String getCheck2() {
        return check2;
    }

    public void setCheck2(String check2) {
        this.check2 = check2;
    }
}
