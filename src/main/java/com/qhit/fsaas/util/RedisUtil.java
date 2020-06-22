package com.qhit.fsaas.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qhit.fsaas.bo.Allocation;
import com.qhit.fsaas.bo.Seat;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class RedisUtil {
    @Resource
    RedisTemplate redisTemplate;

    public Integer getTid() {
        // 获取key  tid(机型) 键对应的值。
        Integer tid = (Integer) redisTemplate.opsForValue().get("tid");
        if (tid == null || tid <= 0) {
            tid = 1;
        }
        return tid;
    }

    //redis 缓存存值
    public void setTid(Integer tid) {
        redisTemplate.opsForValue().set("tid", tid);
    }

    public ArrayList<Seat> getSeatList() {
        String seatList = redisTemplate.opsForValue().get("seatList").toString();
        ArrayList<Seat> seats = JSON.parseObject(seatList, new TypeReference<ArrayList<Seat>>(){});
        return seats;
    }

    public void setSeatList(List<Seat> seatList) {
        redisTemplate.opsForValue().set("seatList", JSON.toJSONString(seatList,SerializerFeature.WriteClassName));
    }

    public HashMap<Long, LinkedList<Seat>> getSeatHash() {
        Object seatHash = redisTemplate.opsForValue().get("seatHash");
        return JSON.parseObject(seatHash.toString(),new TypeReference<HashMap<Long, LinkedList<Seat>>>(){});
    }

    public void setSeatHash(HashMap<Long, LinkedList<Seat>> seatHash) {
        redisTemplate.opsForValue().set("seatHash", JSON.toJSONString(seatHash,SerializerFeature.WriteClassName));
    }

    public long[] getSeatCount() {
        Object seatCount = redisTemplate.opsForValue().get("seatCount");
        if (seatCount == null) {
            return null;
        }
        List<Long> seatCountOfList = JSON.parseArray(seatCount.toString(), long.class);
        return seatCountOfList.stream().mapToLong(t -> t).toArray();
    }
    public void setSeatCount(long[] seatCount) {
        redisTemplate.opsForValue().set("seatCount", JSON.toJSONString(seatCount,SerializerFeature.WriteClassName));
    }

    public List<Allocation> getAllocationList() {
        return JSON.parseObject(redisTemplate.opsForValue().get("allocationList").toString(), new TypeReference<ArrayList<Allocation>>(){});
    }
    public void setAllocationList(List<Allocation> allocationList) {
        redisTemplate.opsForValue().set("allocationList", JSON.toJSONString(allocationList,SerializerFeature.WriteClassName));
    }
}
