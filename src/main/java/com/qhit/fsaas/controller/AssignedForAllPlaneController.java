package com.qhit.fsaas.controller;

import com.alibaba.fastjson.JSONObject;
import com.qhit.fsaas.bo.*;
import com.qhit.fsaas.util.MainUtil;
import com.qhit.fsaas.util.RedisUtil;
import com.qhit.fsaas.util.excelUtils.excel.ExportExcel;
import com.qhit.fsaas.util.excelUtils.excel.ImportExcel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author jieyue-mac
 */
@RestController
@RequestMapping(value = "template")
public class AssignedForAllPlaneController {
    @Resource
    RedisUtil redisUtil;
    @Resource
    MainUtil mainUtil;

    /**
     * 下载空模板
     */
    @RequestMapping(value = "/nullTemplate", method = RequestMethod.GET)
    public void downTemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            String fileName = "影院座位信息导入模板.xlsx";
            new ExportExcel("影院座位信息导入模板", PersonTemplate.class, 2).write(request, response, fileName).dispose();
        } catch (Exception ignored) {
        }
    }

    /**
     * 导出
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void exportFile(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String fileName = "影院座位分配信息.xlsx";

        List<PersonTemplate> list = new ArrayList<>();
        PersonTemplate personTemplate = new PersonTemplate("高洁", "windows");
        list.add(personTemplate);

        new ExportExcel("影院座位分配信息", PersonTemplate.class).setDataList(list).write(request, response, fileName).dispose();

    }

    /**
     * 导入
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public JSONObject importFile(@RequestParam(value = "file", required = false) MultipartFile file) {
        JSONObject jsonObject = new JSONObject();
        boolean ok = false;

        try {
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<PersonTemplate> list = ei.getDataList(PersonTemplate.class);
            List<Passengers> passengersList = new ArrayList<>();

            for (PersonTemplate personTemplate : list) {
                List<Passenger> passenger_info = new ArrayList<>();
                //根据时间创建groupid
                long groupId = new Date().getTime();
                //封装对象添入passenger_info    list
                passenger_info.add(new Passenger(groupId + "", personTemplate.getName(), personTemplate.getPreferred()));
                if (passenger_info.size() != 0) {
                    //构建passengers对象
                    Passengers passengers = new Passengers(passenger_info.size(), passenger_info);
                    passengersList.add(passengers);
                }
                //线程
                Thread.sleep(1);
            }

            List<Passengers> passengers = assignedSeatForAllPlane(passengersList);
            if (passengers.size() > 0) {
                ok = true;
            }

            if (ok) {
                jsonObject.put("status", "1");
                jsonObject.put("message", passengers);
            } else {
                jsonObject.put("status", "4");
                jsonObject.put("message", "分配座位失败");
            }
        } catch (IllegalAccessException | InvalidFormatException | IOException | InstantiationException | InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private Seat updateSeatForOne(LinkedList<Seat> seatHashList, long flag) {
        Seat seat = null;
        List<Seat> seatList = redisUtil.getSeatList();
        long[] seatCount = redisUtil.getSeatCount();

        Iterator iterator = seatHashList.iterator();
        while (iterator.hasNext()) {
            seat = (Seat) iterator.next();
            //已经被分出去的座位删除
            if ((seat.getFlg() & Global.SEAT_STATUS_NOASSIGNED) != Global.SEAT_STATUS_NOASSIGNED) {
                iterator.remove();
                continue;
            }

            //更新座位状态
            seat.setAssigningFlg(Global.SEAT_STATUS_ASSIGNING);

            //更新列表信息
            seatList.get(seat.getColumn()).setAssigningFlg(Global.SEAT_STATUS_ASSIGNING);

            //更新seatCount信息
            long oldCount = seatCount[seat.getColumn()];
            seatCount[seat.getColumn()] = Global.SEAT_STATUS_ASSIGNED;

            for (int i = seat.getColumn() - 1; i >= 0; i--) {
                if (seatCount[i] == 0) break;
                if (seatCount[i] >= oldCount) {
                    seatCount[i] = seatCount[i] - oldCount;
                }
            }

            iterator.remove();
            break;
        }

        HashMap<Long, LinkedList<Seat>> seatHash = redisUtil.getSeatHash();
        seatHash.remove(flag);
        seatHash.put(flag, seatHashList);
        redisUtil.setSeatHash(seatHash);
        redisUtil.setSeatCount(seatCount);
        redisUtil.setSeatList(seatList);
        return seat;
    }

    private List<Passengers> assignedSeatForAllPlane(List<Passengers> passengersList) {
        HashMap<Long,List<Person>> personHash = new HashMap<>();
        for (Passengers passengers : passengersList) {
            for (int i = 0; i < passengers.getPassenger_num(); i++) {
                //取到用户详细信息
                Passenger p = passengers.getPassenger_info().get(i);
                //构造flg  座位需求
                Person person = new Person(p.getPName(), p.getPreferred(), p.getGroupId());
                person.setPassenger(p);
                List<Person> persons = personHash.get(person.getFlg());
                if (persons == null || persons.size() == 0) {
                    persons = new LinkedList<>();
                    persons.add(person);
                    personHash.put(person.getFlg(), persons);
                } else {
                    persons.add(person);
                }

            }
        }
        HashMap<Long, LinkedList<Seat>> seatHash = redisUtil.getSeatHash();

        //遍历用户座位需求
        for (Long aLong : personHash.keySet()) {
            LinkedList<Seat> seatlist = null;

            Long flag = -1L;
            for (Long seatFlag : seatHash.keySet()) {
                if ((seatFlag & aLong) == aLong) {
                    //找到满足当前用户的所有座位
                    seatlist = seatHash.get(seatFlag);
                    if (seatlist != null && seatlist.size() != 0) {
                        flag = seatFlag;
                        break;
                    }
                }
            }

            if (seatlist == null || seatlist.size() == 0) {
                System.out.println("没有合适的座位");
            } else {
                //取到同一需求的用户  分配座位
                Iterator<Person> personIterator = (personHash.get(aLong)).iterator();
                while (true) {
                    if (personIterator.hasNext()) {
                        Person person = personIterator.next();
                        //更新座位状态
                        Seat seat = updateSeatForOne(seatlist, flag);
                        if (seat != null) {
                            Passenger p1 = person.getPassenger();
                            //分配座位
                            p1.setSeat(seat);
                            person.setFlg(Global.SEAT_STATUS_ASSIGNING);
                            personIterator.remove();
                        } else {
                            System.out.println("座位不够");
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        mainUtil.batchInsert(passengersList);
        mainUtil.showSeatCount();
        return passengersList;
    }
}
