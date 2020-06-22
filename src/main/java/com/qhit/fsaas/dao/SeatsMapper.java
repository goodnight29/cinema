package com.qhit.fsaas.dao;

import java.util.List;
import com.qhit.fsaas.bo.Seats;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface SeatsMapper {
    List<Seats> selectAllSeatsWithFlag(@Param("tid") Integer tid);
}