package com.example.dingtaihw.Model.Out;

public class Out_Scan {
    private String Lotid;
    private String innerlabel;
    private String outerlable;

    public Out_Scan(String lotid, String innerlabel, String outerlable) {
        Lotid = lotid;
        this.innerlabel = innerlabel;
        this.outerlable = outerlable;
    }

    public Out_Scan() {
    }

    public String getLotid() {
        return Lotid;
    }

    public void setLotid(String lotid) {
        Lotid = lotid;
    }

    public String getInnerlabel() {
        return innerlabel;
    }

    public void setInnerlabel(String innerlabel) {
        this.innerlabel = innerlabel;
    }

    public String getOuterlable() {
        return outerlable;
    }

    public void setOuterlable(String outerlable) {
        this.outerlable = outerlable;
    }
}
