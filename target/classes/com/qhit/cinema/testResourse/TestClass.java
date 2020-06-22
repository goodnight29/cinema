package com.qhit.fsaas.src;

import javax.sound.midi.Soundbank;
import java.util.*;

/**
 * Created by Administrator on 2019/4/17.
 */
public class TestClass {

    private HashMap<Long, Object> seatHash = new HashMap();
    private HashMap<Long, Object> personHash = new HashMap();
    private long[] seatcount;
//    private long[] personcount;
    private List<Seat> seatList = new ArrayList<Seat>();
    private List<Person> personList = new ArrayList<Person>();

    public static void main(String[] args) {
        TestClass t = new TestClass();
        t.initSeat();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("请输入");
            System.out.println("********************************");
            System.out.println("1、一个带个人偏好属性的旅客自动分配相应属性座位");
            System.out.println("2、一组旅客自动分配相近的座位");
            System.out.println("3、输入整机旅客信息，按照最大满足旅客需求的算法");
            System.out.println("********************************");
            int menu = sc.nextInt();

            switch (menu) {
                case 1:
                    Person p = t.showPersonMenu(sc);
                    System.out.println(p.toString());
                    t.assignedSeat(p);
                    break;
                case 2:
                    System.out.println("输入订票人数");
                    int pnum = sc.nextInt();
                    t.assignedGroupSeat(pnum);
                    break;
                case 3:
                    System.out.println("输入整机信息人数");

                    int num = sc.nextInt();
                    for (int i = 0; i < num; i++) {
                        Person p1 = t.showPersonMenu(sc);

                        t.initPerson(p1);
                    }
                    t.addToPersonHashMap(t.personList);
                    t.assignedAllSeat();
                    break;

            }


        }

    }

    public Person showPersonMenu(Scanner sc) {
        System.out.println("请输入你的名字");
        String name = sc.next();

        System.out.println("请输入你喜好");
        System.out.println("********************************");
        System.out.println("1、靠窗");
        System.out.println("2、靠过道");
        System.out.println("3、登机口座位");
        System.out.println("4、婴儿摇篮");
        System.out.println("5、无要求");
        System.out.println("********************************");

        String isWindows = "";
        String isAislel = "";
        String isGate = "";
        String isCarryBaby = "";

        int like = sc.nextInt();

        switch (like) {
            case 1:
                isWindows = "windows";
                break;

            case 2:
                isAislel = "aislel";
                break;
            case 3:
                isGate = "gate";
                break;
            case 4:
                isCarryBaby = "carryBaby";
                break;
            default:
                break;
        }

        return new Person(name, isWindows, isAislel, isGate, isCarryBaby);
    }


    public void initSeat() {
        String[][] str = new String[][]{
                {"A", "31", "windows", "gate", ""}, {"C", "31", "aislel", "gate", ""}, {"D", "31", "aislel", "", "baby"}, {"E", "31", "", "", "baby"}, {"F", "31", "", "", "baby"}, {"G", "31", "aislel", "", ""}, {"H", "31", "aislel", "", ""}, {"K", "31", "windows", "", ""},
                {"A", "32", "windows", "", ""}, {"C", "32", "aislel", "", ""}, {"D", "32", "aislel", "", ""}, {"E", "32", "", "", ""}, {"F", "32", "", "", ""}, {"G", "32", "aislel", "", ""}, {"H", "32", "aislel", "", ""}, {"K", "32", "windows", "", ""},
                {"A", "33", "windows", "", ""}, {"C", "33", "aislel", "", ""}, {"D", "33", "aislel", "", ""}, {"E", "33", "", "", ""}, {"F", "33", "", "", ""}, {"G", "33", "aislel", "", ""}, {"H", "33", "aislel", "", ""}, {"K", "33", "windows", "", ""},
                {"A", "34", "windows", "", ""}, {"C", "34", "aislel", "", ""}, {"D", "34", "aislel", "", ""}, {"E", "34", "", "", ""}, {"F", "34", "", "", ""}, {"G", "34", "aislel", "", ""}, {"H", "34", "aislel", "", ""}, {"K", "34", "windows", "", ""},

        };

        int k = 0;
        int seatSize = Global.MAX_SEAT_NUMBER;
        seatcount = new long[str.length];

        int z = 1;
        for (int i = 0; i < str.length; i++) {
            if ((i / seatSize) % 2 == 0) {
                k = i;
                z = 1;
            } else {
                k = (i / seatSize + 1) * seatSize - z;
                z++;
            }

            if (str[k][0].equals("")) {
                continue;
            }
            Seat seat = new Seat(str[k][0], str[k][1], i);
            if (str[k][2].equals("windows")) {
                seat.setIsWindows(1);
            } else {
                seat.setIsWindows(0);
            }

            if (str[k][2].equals("aislel")) {
                seat.setIsAislel(1);
            } else {
                seat.setIsAislel(0);
            }

            if (str[k][3].equals("gate")) {
                seat.setIsGate(1);
            } else {
                seat.setIsGate(0);
            }

            if (str[k][4].equals("baby")) {
                seat.setIsCarryBaby(1);
            } else {
                seat.setIsCarryBaby(0);
            }

            seatList.add(seat);

            seatcount[i] = str.length - i;

        }

        showSeatList(seatList);

        addToSeatHashMap(seatList);

        showSeatcount();

    }

    public void initPerson(Person p) {
        personList.add(p);
    }

    public void addToPersonHashMap(List<Person> personList) {
        for (Person person : personList) {
            List persions = (List) personHash.get(person.getFlg());
            if (persions == null || persions.size() == 0) {
                persions = new LinkedList<Seat>();
                persions.add(person);
                personHash.put(person.getFlg(), persions);
            } else {
                persions.add(person);
            }
        }
    }


    public void showSeatList(List<Seat> seatList) {
        for (Seat s : seatList) {
            System.out.println(s.toString());
        }

    }

    public void showSeatcount() {
        for (int i = 0; i < seatcount.length; i++) {
            System.out.print(seatcount[i] + "\t");
        }

    }

    public void addToSeatHashMap(List<Seat> seatList) {
        for (Seat seat : seatList) {
            List seats = (List) seatHash.get(seat.getFlg());
            if (seats == null || seats.size() == 0) {
                seats = new LinkedList<Seat>();
                seats.add(seat);
                seatHash.put(seat.getFlg(), seats);
            } else {
                seats.add(seat);
            }
        }
    }


    public void assignedSeat(Person p) {

        Set<Long> seatKeySet = seatHash.keySet();
        List seatlist = null;

        for (Long seatflg : seatKeySet) {
            if ((seatflg & p.getFlg()) == p.getFlg()) {
                seatlist = (List) seatHash.get(seatflg);
                if (seatlist == null || seatlist.size() == 0) {
                    continue;
                } else {
                    break;
                }
            }
        }

        if (seatlist == null || seatlist.size() == 0) {
            System.out.println("没有合适的座位");
        } else {
            updateSeatState(seatlist);
            p.setFlg(Global.SEAT_STATUS_ASSIGNED);
        }

        showSeatcount();
    }

    public boolean updateSeatState(List seatlist) {
        Iterator<Seat> iterator = seatlist.iterator();
        while (iterator.hasNext()) {
            Seat seat = iterator.next();
            //已经被分出去的座位删除
            if ((seat.getFlg() & Global.SEAT_STATUS_NOASSIGNED) != Global.SEAT_STATUS_NOASSIGNED) {
                iterator.remove();
                continue;
            }
            seat.setFlg(Global.SEAT_STATUS_ASSIGNED);
            System.out.println(seat.toString());

            long oldcount = seatcount[seat.getColum()];

            seatcount[seat.getColum()] = Global.SEAT_STATUS_ASSIGNED;

            for (int i = 0; i < seat.getColum(); i++) {
                if (seatcount[i] >= oldcount) {
                    seatcount[i] = seatcount[i] - oldcount;
                }
            }
            iterator.remove();
            return true;
        }
        return false;
    }

    public void assignedGroupSeat(int num) {
        for (int i = 0; i < seatcount.length; i++) {
            if (seatcount[i] >= num) {
                long oldcount = seatcount[i];
                for (int k = i; k < (i + num); k++) {
                    seatcount[k] = 0;
                    Seat seat = seatList.get(k);
                    seat.setFlg(Global.SEAT_STATUS_ASSIGNED);
                    System.out.println(seat.toString());
                }
                for (int z = 0; z < i; z++) {
                    if (seatcount[z] >= oldcount) {
                        seatcount[z] = seatcount[i] - oldcount;
                    }
                }
                break;
            }
        }
        showSeatcount();
    }


    public void assignedAllSeat() {
        Set<Long> personKeySet = personHash.keySet();
        Iterator<Long> ipersonKey = personKeySet.iterator();
        Set<Long> seatKeySet = seatHash.keySet();
        while (ipersonKey.hasNext()) {
            List seatlist = null;
            Long personflg = ipersonKey.next();
            for (Long seatflg : seatKeySet) {
                if ((seatflg & personflg) == personflg) {
                    seatlist = (List) seatHash.get(seatflg);
                    if (seatlist == null || seatlist.size() == 0) {
                        continue;
                    } else {
                        break;
                    }
                }
            }
            if (seatlist == null || seatlist.size() == 0) {
                System.out.println("没有合适的座位");
            } else {
                List<Person> persons = (List) personHash.get(personflg);
                Iterator<Person> personIterator = persons.iterator();
                while (true) {
                    if (personIterator.hasNext()) {
                        Person person = personIterator.next();
                        boolean ret = updateSeatState(seatlist);
                        if (ret == true) {
                            person.setFlg(Global.SEAT_STATUS_ASSIGNED);
                            personIterator.remove();
                        } else {
                            System.out.println("座位不够");
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        showSeatcount();
    }
}
