package com.qhit.fsaas.bo;

public class Global {
    public final static long SEAT_STATUS_ASSIGNED = 0;     //座位已经分配出去   00000
    public final static long SEAT_STATUS_NOASSIGNED = 1;   //座位未被分配出去   00001
    public final static long SEAT_STATUS_BABY = 2;         // 婴儿摇篮          00010
    public final static long SEAT_STATUS_GATE = 4;         //登机口座位         00100
    public final static long SEAT_STATUS_AISLE = 8;        //靠过道             01000
    public final static long SEAT_STATUS_WINDOWS = 16;     //靠窗               10000
    public final static long SEAT_STATUS_ASSIGNING = 30;   //座位分配出去       11110

    //
}