package com.qhit.fsaas.service;

import com.qhit.fsaas.bo.Allocation;

import java.util.List;

public interface IAllocationService {
    List<Allocation> selectAll(int tid);
    int batchInsert(List<Allocation> list);
    int truncateAllocation();
}
