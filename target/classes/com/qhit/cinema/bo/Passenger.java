package com.qhit.fsaas.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Passenger implements Serializable {
    private String groupId;
    private String pName;
    private String preferred;
    private Seat seat;

    public Passenger() {
    }

    public Passenger(String groupId, String pName, String preferred) {
        this.groupId = groupId;
        this.pName = pName;
        this.preferred = preferred;
    }

    private static final long serialVersionUID = 1L;
}
