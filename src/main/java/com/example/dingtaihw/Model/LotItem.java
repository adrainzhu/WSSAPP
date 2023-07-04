package com.example.dingtaihw.Model;

public class LotItem {
    private String Lotid;
    private String lotstatus;
    private String lotsdate;

    public LotItem(String lotid, String lotstatus, String lotsdate) {
        Lotid = lotid;
        this.lotstatus = lotstatus;
        this.lotsdate = lotsdate;
    }

    public LotItem() {
    }

    public String getLotid() {
        return Lotid;
    }

    public void setLotid(String lotid) {
        Lotid = lotid;
    }

    public String getLotstatus() {
        return lotstatus;
    }

    public void setLotstatus(String lotstatus) {
        this.lotstatus = lotstatus;
    }

    public String getLotsdate() {
        return lotsdate;
    }

    public void setLotsdate(String lotsdate) {
        this.lotsdate = lotsdate;
    }
}
