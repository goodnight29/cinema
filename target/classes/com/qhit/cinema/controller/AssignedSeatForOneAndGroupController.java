package com.qhit.fsaas.controller;

import com.alibaba.fastjson.JSONObject;
import com.qhit.fsaas.bo.*;
import com.qhit.fsaas.service.IAllocationService;
import com.qhit.fsaas.util.*;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.*;

@RestController
public class AssignedSeatForOneAndGroupController {
    @Resource
    RedisUtil redisUtil;
    @Resource
    MainUtil mainUtil;
    @Resource
    IAllocationService allocationService;

    @RequestMapping("/assignedJsonTest")
    public String assigned(@RequestBody List<Passengers> passengers) {
        if (passengers.isEmpty()){
            return "List is empty!";
        }else if (passengers.size()==1){
            if (passengers.get(0).getPassenger_num()==1){
                return "List.size is one!\nPassenger is one!";
            }else{
                return "List.size is one!\nPassenger is more!";
            }
        }else{
            return "List.size is more!";
        }
    }

    @RequestMapping("/selectAll")
    public JSONObject selectAll() {
        JSONObject jsonObject = new JSONObject();
        ArrayList<Seat> seatList = redisUtil.getSeatList();
        jsonObject.put("allSeats",seatList);
        jsonObject.put("allAllocations",redisUtil.getAllocationList());
        return jsonObject;
    }

    @RequestMapping("/truncateAllocation")
    public JSONObject truncateAllocation() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("truncateNum",allocationService.truncateAllocation());
        return jsonObject;
    }

    @RequestMapping("/assignedSeatForOne")
    public JSONObject assignedSeatForOne(@RequestBody List<Passengers> passengers) {
        //解析界面参数
        JSONObject jsonObject = new JSONObject();
        Passenger passenger = passengers.get(0).getPassenger_info().get(0);
        Person person = new Person(passenger.getPName(),passenger.getPreferred(),passenger.getGroupId());

        //获取座位hash
        HashMap<Long, LinkedList<Seat>> seatHash = redisUtil.getSeatHash();

        Set<Long> seatKeySet = seatHash.keySet();
        LinkedList<Seat> seathashlist = null;
        long flag = -1;
        //遍历所有键
        for (Long seatFlag : seatKeySet) {
            if ((seatFlag & person.getFlg()) == person.getFlg()) {
                seathashlist = seatHash.get(seatFlag);
                //找到用户需要的座位信息链表
                if (seathashlist != null && seathashlist.size() != 0) {
                    flag = seatFlag;
                    break;
                }
            }
        }

        //判断分配情况
        if (seathashlist == null || seathashlist.size() == 0) {
            jsonObject.put("status","4");
            jsonObject.put("message","没有合适的座位");
        } else {
            //更新座位状态     寻找合适座位
            Seat seat = updateSeatForOne(seathashlist,flag);
            person.setFlg(Global.SEAT_STATUS_ASSIGNING);
            jsonObject.put("status","1");
            //为该用户分配座位
            passengers.get(0).getPassenger_info().get(0).setSeat(seat);
            jsonObject.put("message", passengers);
            boolean b = mainUtil.batchInsert(passengers);
            if (b){
                jsonObject.put("insertStatus","true");
            }else{
                jsonObject.put("insertStatus","false");
            }
        }
        mainUtil.showSeatCount();
        return jsonObject;
    }
    //小组添加座位
    @RequestMapping("/assignedSeatForGroup")
    public JSONObject assignedSeatForGroup(@RequestBody List<Passengers> passengers) {
        //解析界面参数    封装json
        JSONObject jsonObject = new JSONObject();
        //passenger_num添加的乘客数量
        Integer passenger_num = passengers.get(0).getPassenger_num();

        //获取座位redis信息
        long[] seatCount = redisUtil.getSeatCount();
        List<Seat> seatList = redisUtil.getSeatList();

        int index = 0;
        boolean isFound = false;

        for (int i = 0; i < seatCount.length; i++) {
            if (seatCount[i] >= passenger_num) {
                isFound = true;
                //取到值---->更新
                long oldCount = seatCount[i];
                //(i + passenger_num)一次分配passenger_num个座位
                for (int k = i; k < (i + passenger_num); k++) {
                    seatCount[k] = 0;
                    Seat seat = seatList.get(k);
                    seat.setAssigningFlg(Global.SEAT_STATUS_ASSIGNING);
                    //分配座位
                    passengers.get(0).getPassenger_info().get(index++).setSeat(seat);
                }
                /*for (int z = 0; z < i; z++) {
                    if (seatCount[z] >= oldCount) {
                        seatCount[z] = seatCount[i] - oldCount;
                    }
                }*/
                for (int z = i-1; z >= 0; z--) {
                    //为0不用更新
                    if(seatCount[z] == 0) break;
                    if (seatCount[z] >= oldCount) {
                        
                        seatCount[z] = seatCount[z] - oldCount;
                    }
                }

                HashMap<Long, LinkedList<Seat>> seatHash = new HashMap<>();
                mainUtil.addToSeatHashMap(seatList,seatHash);
                redisUtil.setSeatHash(seatHash);
                redisUtil.setSeatCount(seatCount);
                redisUtil.setSeatList(seatList);
                break;
            }
        }
        if (isFound) {
            //分配到座位
            jsonObject.put("status","1");
            jsonObject.put("message", passengers);
            boolean b = mainUtil.batchInsert(passengers);
            if (b){
                jsonObject.put("insertStatus","true");
            }else{
                jsonObject.put("insertStatus","false");
            }
        } else {
            jsonObject.put("status","4");
            jsonObject.put("message","没有合适的座位");
        }
        mainUtil.showSeatCount();
        return jsonObject;
    }

    //为用户寻找合适座位
    private Seat updateSeatForOne(LinkedList<Seat> seatHashList,long flag) {
        Seat seat = null;
        List<Seat> seatList = redisUtil.getSeatList();
        long[] seatCount = redisUtil.getSeatCount();

        //迭代器
        Iterator iterator = seatHashList.iterator();
        while (iterator.hasNext()) {
            seat = (Seat)iterator.next();
            //已经被分出去的座位
            if ((seat.getFlg() & Global.SEAT_STATUS_NOASSIGNED) != Global.SEAT_STATUS_NOASSIGNED) {
                iterator.remove();
                continue;
            }

            //更新座位状态
            seat.setAssigningFlg(Global.SEAT_STATUS_ASSIGNING);

            //更新列表信息
            seatList.get(seat.getColumn()).setAssigningFlg(Global.SEAT_STATUS_ASSIGNING);

            //更新seatcount信息
            long oldCount = seatCount[seat.getColumn()];
            seatCount[seat.getColumn()] = Global.SEAT_STATUS_ASSIGNED;

         /*   for (int i = 0; i < seat.getColumn(); i++) {
                if (seatCount[i] >= oldCount) {
                    seatCount[i] = seatCount[i] - oldCount;
                }
            }*/

            for (int i = seat.getColumn()-1; i >= 0; i--) {
                if(seatCount[i] == 0) break;
                if (seatCount[i] >= oldCount) {
                    seatCount[i] = seatCount[i] - oldCount;
                }
            }

            iterator.remove();
            break;
        }

        HashMap<Long, LinkedList<Seat>> seatHash = redisUtil.getSeatHash();
        //先删后存
        seatHash.remove(flag);
        seatHash.put(flag,seatHashList);

        redisUtil.setSeatHash(seatHash);
        redisUtil.setSeatCount(seatCount);
        redisUtil.setSeatList(seatList);
        return seat;
    }
}
