package com.example.dingtaihw.Model;

import java.time.LocalDateTime;
import java.util.Date;

public class Goods {
    private String soNumber;
    private String Status;
    private String scandate;
    private String number;
    private String lh;

    public String getSoNumber() {
        return soNumber;
    }

    public void setSoNumber(String soNumber) {
        this.soNumber = soNumber;
    }

    private String unit;
    private String pno;

    public Goods(String sonumber, String status, String scandate, String number, String unit, String pno,String lh) {
        soNumber = sonumber;
        Status = status;
        this.scandate = scandate;
        this.number = number;
        this.unit = unit;
        this.pno = pno;
        this.lh=lh;
    }

    public String getLh() {
        return lh;
    }

    public void setLh(String lh) {
        this.lh = lh;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getScandate() {
        return scandate;
    }

    public void setScandate(String scandate) {
        this.scandate = scandate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        number = number;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


}

