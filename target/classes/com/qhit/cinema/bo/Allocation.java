package com.qhit.fsaas.bo;

import lombok.Data;
import java.io.Serializable;

@Data
public class Allocation implements Serializable {
    private Integer aid;
    private Integer sid;
    private String pName;
    private String groupId;
    private Seat seat;

    private static final long serialVersionUID = 1L;

    public Allocation() {
    }

    public Allocation(Integer sid, String pName, String groupId) {
        this.sid = sid;
        this.pName = pName;
        this.groupId = groupId;
    }

    public Allocation(Integer aid, Integer sid, String pName, String groupId) {
        this.aid = aid;
        this.sid = sid;
        this.pName = pName;
        this.groupId = groupId;
    }

    public Allocation(Integer aid, Integer sid, String pName, String groupId, Seat seat) {
        this.aid = aid;
        this.sid = sid;
        this.pName = pName;
        this.groupId = groupId;
        this.seat = seat;
    }
}