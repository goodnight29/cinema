package com.qhit.fsaas.src;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/4/17.
 */
public class Person {
    private String name;
    private String sex;
    private int isWindows = 0;
    private int isAislel = 0;
    private int isGate = 0;
    private int isCarryBaby = 0;

    private long flg = 0;



    public Person() {
    }


    public Person(String name, String isWindows, String isAislel, String isGate, String isCarryBaby) {
        this.name = name;
        this.sex = sex;

        //靠窗+考靠过道+是否已经预定
        if (isWindows.equals("windows")) {
            this.isWindows = 1;
        }

        if (isAislel.equals("aislel")) {
            this.isAislel = 1;
        }

        if (isGate.equals("gate")) {
            this.isGate = 1;
        }


        if (isCarryBaby.equals("carryBaby")) {
            this.isCarryBaby = 1;
        }

        this.flg |= Global.SEAT_STATUS_NOASSIGNED;


        if (this.isWindows == 1) {
            this.flg |= Global.SEAT_STATUS_WINDOWS;
        }

        if (this.isAislel == 1) {
            this.flg |= Global.SEAT_STATUS_AISLEL;
        }

        if (this.isGate == 1) {
            this.flg |= Global.SEAT_STATUS_GATE;
        }

        if (this.isCarryBaby == 1) {
            this.flg |= Global.SEAT_STATUS_BABY;
        }
    }

    public long getFlg() {
        return flg;
    }

    public void setFlg(long flg) {
        this.flg |= flg;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", isWindows=" + isWindows +
                ", isAislel=" + isAislel +
                ", isGate=" + isGate +
                ", isCarryBaby=" + isCarryBaby +
                ", flg=" + flg +
                '}';
    }
}



