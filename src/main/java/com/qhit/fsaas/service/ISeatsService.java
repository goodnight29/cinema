package com.qhit.fsaas.service;

import com.qhit.fsaas.bo.Seats;
import java.util.List;

public interface ISeatsService {
    List<Seats> selectAll(int tid);
}
