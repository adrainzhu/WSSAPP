package com.example.dingtaihw.Model.LL;

public class RequestParts extends Parts{
    private String requestnum;//需求数量
    private String orderwh;//指定仓别
    private String bz;//备注


    private String requesttype;//类别

    public RequestParts() {
    }

    public RequestParts(String lh, String info, String location, String pno, String requestnum, String orderwh, String bz,String requesttype) {
        super(lh, info, location, pno);
        this.requestnum = requestnum;
        this.orderwh = orderwh;
        this.bz = bz;
        this.requesttype=requesttype;
    }

    public RequestParts(String requestnum, String orderwh, String bz) {
        this.requestnum = requestnum;
        this.orderwh = orderwh;
        this.bz = bz;
    }

    public String getRequestnum() {
        return requestnum;
    }

    public void setRequestnum(String requestnum) {
        this.requestnum = requestnum;
    }

    public String getOrderwh() {
        return orderwh;
    }

    public void setOrderwh(String orderwh) {
        this.orderwh = orderwh;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }
    public String getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(String requesttype) {
        this.requesttype = requesttype;
    }

    @Override
    public String toString() {
        return "RequestParts{" +
                "requestnum='" + requestnum + '\'' +
                ", orderwh='" + orderwh + '\'' +
                ", bz='" + bz + '\'' +
                ", requesttype='" + requesttype + '\'' +
                '}';
    }
}

