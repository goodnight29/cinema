package com.qhit.fsaas.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Passengers implements Serializable {
    private Integer passenger_num;
    private List<Passenger> passenger_info;

    private static final long serialVersionUID = 1L;

    public Passengers() {
    }

    public Passengers(Integer passenger_num, List<Passenger> passenger_info) {
        this.passenger_num = passenger_num;
        this.passenger_info = passenger_info;
    }
}