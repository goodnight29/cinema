package com.qhit.fsaas.src;

/**
 * Created by Administrator on 2019/4/17.
 */
public class Seat {
    private String col;
    private String row;

    private int isWindows = 0;
    private int isAislel = 0;
    private int isGate = 0;
    private int isCarryBaby = 0;
    private int colum;


    private long flg;

    public Seat() {
    }


    public Seat(String col, String row, int colum) {
        this.col = col;
        this.row = row;
        this.colum = colum;
        this.flg |= Global.SEAT_STATUS_NOASSIGNED;
    }

    public int getColum() {
        return colum;
    }


    public void setIsWindows(int isWindows) {
        this.isWindows = isWindows;
        if (this.isWindows == 1) {
            this.flg |= Global.SEAT_STATUS_WINDOWS;
        }

    }

    public void setIsAislel(int isAislel) {
        this.isAislel = isAislel;
        if (this.isAislel == 1) {
            this.flg |= Global.SEAT_STATUS_AISLEL;
        }
    }

    public void setIsGate(int isGate) {
        this.isGate = isGate;
        if (this.isGate == 1) {
            this.flg |= Global.SEAT_STATUS_GATE;
        }
    }

    public void setIsCarryBaby(int isCarryBaby) {
        this.isCarryBaby = isCarryBaby;

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

    @Override
    public String toString() {
        return "Seat{" +
                "col='" + col + '\'' +
                ", row='" + row + '\'' +
                ", isWindows=" + isWindows +
                ", isAislel=" + isAislel +
                ", isGate=" + isGate +
                ", isCarryBaby=" + isCarryBaby +
                ", colum=" + colum +
                ", flg=" + flg +
                '}';
    }


}
