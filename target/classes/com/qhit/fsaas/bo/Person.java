package com.qhit.fsaas.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Person implements Serializable {
    private String pName;
    private int isWindows = 0;
    private int isAisle = 0;
    private int isGate = 0;
    private int isCarryBaby = 0;
    private String group;
    private Passenger passenger;
    private long flg = 0;

    private static final long serialVersionUID = 1L;

    //构建flg
    public Person(String pName, String preferred,String group) {
        this.pName = pName;
        this.flg |= Global.SEAT_STATUS_NOASSIGNED;
        switch (preferred) {
            case "windows":
                this.isWindows = 1;
                this.flg |= Global.SEAT_STATUS_WINDOWS;
                break;
            case "aisle":
                this.isAisle = 1;
                this.flg |= Global.SEAT_STATUS_AISLE;
                break;
       /*     case "null":
                this.isGate = 1;
                this.flg |= Global.SEAT_STATUS_GATE;
                break;*/
            case "basket":
                this.isCarryBaby = 1;
                this.flg |= Global.SEAT_STATUS_BABY;
                break;
            default:
                this.isGate = 1;
                this.flg |= Global.SEAT_STATUS_GATE;
                break;
        }
        this.group=group;
    }

    public void setFlg(long flg) {
        this.flg &= flg;
    }
}



