package com.qhit.fsaas.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Seats implements Serializable {
    private Integer sid;
    private Integer sRow;
    private String sColumn;
    private Integer v_gate;
    private Integer v_aisle;
    private Integer v_window;
    private Integer v_basket;
    private Integer assigned;
    private Integer v_exit;

    private Integer aid;
    private String pName;

    private Integer tid;

    private Long flag;

    private static final long serialVersionUID = 1L;
}