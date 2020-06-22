package com.qhit.fsaas.service.impl;

import com.qhit.fsaas.bo.Seats;
import com.qhit.fsaas.dao.SeatsMapper;
import com.qhit.fsaas.service.ISeatsService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class ISeatsServiceImpl implements ISeatsService {
    @Resource
    private SeatsMapper seatsMapper;

    public List<Seats> selectAll(int tid){
        return seatsMapper.selectAllSeatsWithFlag(tid);
    }
}
