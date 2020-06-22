package com.qhit.fsaas.service.impl;

import com.qhit.fsaas.bo.Allocation;
import com.qhit.fsaas.dao.AllocationMapper;
import com.qhit.fsaas.service.IAllocationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IAllocationServiceImpl implements IAllocationService {
    @Resource
    private AllocationMapper allocationMapper;

    public List<Allocation> selectAll(int tid){
        return allocationMapper.selectAll(tid);
    }

    public int batchInsert(List<Allocation> list){
        return allocationMapper.batchInsert(list);
    }

    public int truncateAllocation(){ return allocationMapper.truncateAllocation(); }
}
