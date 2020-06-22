package com.qhit.fsaas.util;

import com.qhit.fsaas.bo.*;
import com.qhit.fsaas.service.IAllocationService;
import com.qhit.fsaas.service.ISeatsService;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.*;

@Component
public class MainUtil {
    @Resource
    ISeatsService seatsService;
    @Resource
    IAllocationService allocationService;
    @Resource
    private RedisUtil redisUtil;

    public void init() {
        List<Seat> seatList = new ArrayList<>();
        HashMap<Long, LinkedList<Seat>> seatHash = new HashMap<>();

        redisUtil.setTid(redisUtil.getTid());
        List<Seats> seatsList = seatsService.selectAll(redisUtil.getTid());
        initSeat(seatsList, seatList,seatHash);
        redisUtil.setSeatList(seatList);
        redisUtil.setSeatHash(seatHash);
        redisUtil.setAllocationList(allocationService.selectAll(redisUtil.getTid()));
    }

    private void initSeat(List<Seats> seatsList, List<Seat> seatList, HashMap<Long, LinkedList<Seat>> seatHash) {
        //所有座位
        long[] seatCount = new long[seatsList.size() - 11];

        //飞机实际座位长度 256  - 12 == 244
        int a = seatsList.size() - 11 - 1;

        int num = 1;
        //构建座位信息数组
        for (int i = seatsList.size()-1; i >= 0; i--) {
            Seats seats = seatsList.get(i);
            //判断是否是空位
            if (!seats.getSColumn().equals("")) {
                if(seats.getAssigned()==0){//0 已分配
                    num = 0;
                }
                //seats封装对象  一直在头部添加
                seatList.add(0,new Seat(seats.getSid(),seats.getSColumn(), seats.getSRow(), seats.getV_window(), seats.getV_aisle(), seats.getV_gate(), seats.getV_basket(), a,seats.getTid(), seats.getFlag()));
                seatCount[a--] = num++;
            }
        }
        //控制台打印位置信息
        showSeatCount(seatCount);
        //redis缓存信息
        redisUtil.setSeatCount(seatCount);
        addToSeatHashMap(seatList, seatHash);
    }

    //组装sseatHash
    public void addToSeatHashMap(List<Seat> seatList, HashMap<Long, LinkedList<Seat>> seatHash) {
        for (Seat seat : seatList) {
            //SEAT_STATUS_NOASSIGNED  座位未分配出去  flg座位属性  二进制运算    &两个数都转为二进制，然后从高位开始比较，如果两个数都为1则为1，否则为0。
            //判断该座位是否已分配
            if ((seat.getFlg() & Global.SEAT_STATUS_NOASSIGNED) != Global.SEAT_STATUS_NOASSIGNED) {
                continue;
            }
            //桶中取链表  接着挂值
            LinkedList<Seat> seats = seatHash.get(seat.getFlg());
            //当前未挂链表
            if (seats == null || seats.size() == 0) {
                seats = new LinkedList<>();
                seats.add(seat);
                //flg  座位属性为键
                seatHash.put(seat.getFlg(), seats);
            } else {
                //直接添加
                seats.add(seat);
            }
        }
    }

    public void showSeatCount() {
        long[] seatCount = redisUtil.getSeatCount();
        for (int i = 0; i < seatCount.length; i++) {
            if (i % 20 == 0) System.out.println();
            System.out.print(seatCount[i] + "\t");
        }
        System.out.println("\n---------------------------------------------------------------------------------");
    }

    private void showSeatCount(long[] seatCount) {
        for (int i = 0; i < seatCount.length; i++) {
            if (i % 20 == 0) System.out.println();
            System.out.print(seatCount[i] + "\t");
        }
        System.out.println("\n---------------------------------------------------------------------------------");
    }

    //数据库insert    添加飞机用户
    public boolean batchInsert(List<Passengers> list){
        List<Allocation> allocationList = new ArrayList<>();
        for (Passengers passengers : list) {
            Integer passenger_num = passengers.getPassenger_num();
            List<Passenger> passenger_info = passengers.getPassenger_info();
            for (int j = 0; j < passenger_num; j++) {
                Passenger passenger = passenger_info.get(j);
                Seat seat = passenger.getSeat();
                if(seat!=null&&seat.getSid()>0){
                    allocationList.add(new Allocation(seat.getSid(), passenger.getPName(), passenger.getGroupId()));
                }
            }
        }
        //数据库加入数据    mybatis用list添加
        int i = allocationService.batchInsert(allocationList);
        if (i>0){
            //redis缓存
            List<Allocation> allocationListOfRedis = redisUtil.getAllocationList();
            allocationListOfRedis.addAll(allocationList);
            redisUtil.setAllocationList(allocationListOfRedis);
            return true;
        }else{
            return false;
        }
    }
}