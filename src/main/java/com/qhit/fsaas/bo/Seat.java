package com.qhit.fsaas.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Seat implements Serializable {
    private int sid;
    private String col;
    private int row;
    private int isWindows;
    private int isAisle;
    private int isGate;
    private int isCarryBaby;
    private int column;
    private int tid;
    private long flg;

    private static final long serialVersionUID = 1L;

    public Seat() {
    }

    public Seat(int sid, String col, int row, int isWindows, int isAisle, int isGate, int isCarryBaby, int column, int tid, long flg) {
        this.sid = sid;
        this.col = col;
        this.row = row;
        this.isWindows = isWindows;
        this.isAisle = isAisle;
        this.isGate = isGate;
        this.isCarryBaby = isCarryBaby;
        this.column = column;
        this.tid = tid;
        this.flg = flg;
    }

    public Seat(int sid, String col, int row, int tid) {
        this.sid = sid;
        this.col = col;
        this.row = row;
        this.tid = tid;
    }

    public void setAssigningFlg(long flg) {
        this.flg &= flg;
    }
}
