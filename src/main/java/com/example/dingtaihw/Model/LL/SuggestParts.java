package com.example.dingtaihw.Model.LL;

public class SuggestParts extends Parts{
    private String unit;//单位
    private String cb;//仓别
    private String suggestnum;//建议数量

    public SuggestParts() {
    }

    public SuggestParts(String lh, String info, String location, String pno, String unit, String cb, String suggestnum) {
        super(lh, info, location, pno);
        this.unit = unit;
        this.cb = cb;
        this.suggestnum = suggestnum;
    }

    public SuggestParts(String unit, String cb, String suggestnum) {
        this.unit = unit;
        this.cb = cb;
        this.suggestnum = suggestnum;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCb() {
        return cb;
    }

    public void setCb(String cb) {
        this.cb = cb;
    }

    public String getSuggestnum() {
        return suggestnum;
    }

    public void setSuggestnum(String suggestnum) {
        this.suggestnum = suggestnum;
    }
}
