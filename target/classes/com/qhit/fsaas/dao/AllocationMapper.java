package com.qhit.fsaas.dao;

import java.util.List;
import com.qhit.fsaas.bo.Allocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AllocationMapper {
    List<Allocation> selectAll(@Param("tid") Integer tid);
    int batchInsert(@Param("list") List<Allocation> list);
    int truncateAllocation();
}