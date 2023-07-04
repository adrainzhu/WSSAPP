package com.example.dingtaihw.Model.LL;

public class SendParts extends Parts{
    private String unit;
    private String requestnum;
    private String sendnum;

    public SendParts() {
    }

    public SendParts(String lh, String info, String location, String pno, String unit, String requestnum, String sendnum) {
        super(lh, info, location, pno);
        this.unit = unit;
        this.requestnum = requestnum;
        this.sendnum = sendnum;
    }

    public SendParts(String unit, String requestnum, String sendnum) {
        this.unit = unit;
        this.requestnum = requestnum;
        this.sendnum = sendnum;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRequestnum() {
        return requestnum;
    }

    public void setRequestnum(String requestnum) {
        this.requestnum = requestnum;
    }

    public String getSendnum() {
        return sendnum;
    }

    public void setSendnum(String sendnum) {
        this.sendnum = sendnum;
    }
}
