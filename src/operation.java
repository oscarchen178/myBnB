import java.sql.SQLException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class operation {

    private mysqlDao dao;

    public operation(mysqlDao dao) {
        this.dao = dao;
    }

    //create a new listing and associate it with their account, cancel a booking or remove a listing. change price
    public void createListing(int oid, String type, String lat, String lon, String addr, String city,
                              String country, String post, String chara) {
        try {
//            if (dao.checkAddrExist(addr, city, country)) {
//                System.out.println("Address already exist!");
//                return;
//            }
            dao.insertListing(oid, type, lat, lon, addr, city, country, post, chara);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertNewDate(int lid, String date, int price, String available) {
        try {
            if (dao.checkCalendarExist(lid, date)) {
                System.out.println("This date of listing already exist!");
                return;
            }
            dao.insertCalender(lid, date, price, available);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void book(int uid, int lid, String start, String end) {
        // find in calender if date available
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        boolean available = false;
        try {
            Date firstDate = sdf.parse(start);
            Date secondDate = sdf.parse(end);
            long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1;
//            System.out.println ("Days: " + diff);

            available = dao.checkBookAvailable(lid, start, end, diff);
            System.out.println("book check: " + available);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!available) {
            System.out.println("Listing not available :(");
            return;
        }
        // place booking
        try {
            dao.insertBooking(uid, lid, start, end, "booked");
            dao.updateAvailable(lid, start, end, "false");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelBook(int bid) {
        // update booking status to canceled, get lid, start, end using bid, update available
        try {
            dao.updateBookingStatus(bid, -1, "canceled");;
            ResultSet rs = dao.getBooking(bid);
            if (!rs.next()) {
                System.out.println("This booking not exist!");
            }
            int lid = rs.getInt("lid");
            String start = rs.getString("start");
            String end = rs.getString("end");
            dao.updateAvailable(lid, start, end, "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeCalendarPrice(int lid, int price, String date) {
        try {
            boolean canModify = dao.canModifyListing(lid, date);
            if (!canModify) {
                System.out.println("not allowed to change price. plz cancel bookings.");
                return;
            }
            dao.updateCalendarPrice(lid, price, date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeCalendarAvailable(int lid, String ava, String date) {
        try {
            boolean canModify = dao.canModifyListing(lid, date);
            if (!canModify) {
                System.out.println("not allowed to change availability. plz cancel bookings.");
                return;
            }
            dao.updateCalendarAvailable(lid, ava, date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteListing(int lid) {
        // cancel all bookings and delete its calendar
        try {
            dao.updateBookingStatus(-1, lid, "canceled");
            dao.deleteCalendar(lid);
            System.out.println("deleted listing: " + lid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkEmail(String email) {
        try {
            boolean exist = dao.checkEmailExist(email);
            return exist;
        } catch (Exception e) {
            return false;
        }
    }

    public int register(String email, String pswd, String name, String addr, String date, String occup, int sin) {
        try {
            boolean exist = dao.checkEmailExist(email);
            if (exist) {
                System.out.println("Email used, Cannot register!");
                return -1;
            }
            dao.insertAccount(email, pswd);
            int uid = dao.getUid(email, pswd);
            dao.insertUser(uid, name, addr, date, occup, sin);
            dao.insertRenter(uid);
      //      System.out.println("register:"+email +",:" +psw);
            return uid;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int login(String email, String pswd) {
        try {
            int uid = dao.getUid(email, pswd);
            if (uid == -1) {
                System.out.println("User don't exist!");
                return -1;
            }
            return uid;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    public void editProfile(int uid, String name, String address, String birthday, String occu, String sin) {
        try {
            dao.updateUser(uid,name,address, birthday, occu, sin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getProfile(int uid) {
        try {
            ResultSet rs = dao.getUser(uid);
            if (!rs.next()) return null;
            String[] res = new String[5];
            res[0] = rs.getString("name");
            res[1] = rs.getString("address");
            res[2] = rs.getString("birthday");
            res[3] = rs.getString("occupation");
            res[4] = rs.getString("sin");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] getPayment(int uid) {
        try {
            ResultSet rs = dao.getRenter(uid);
            if (!rs.next()) return null;
            String[] res = new String[4];
            res[0] = rs.getString("method");
            res[1] = rs.getString("card_num");
            res[2] = rs.getString("expire");
            res[3] = rs.getString("cvv");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void commentListing(int rid, int bid, String comment, String rate) {
        try {
            int lid = dao.getLidByBid(bid);
            dao.insertListingComment(rid, lid, comment, rate);
  //          System.out.println("comment about"+lid +":" +comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void commentRenter(int hid, int bid, String comment, String rate) {
        try {
            int rid = dao.getRidByBid(bid);
            dao.insertRenterComment(hid, rid, comment, rate);
     //       System.out.println("comment from host"+ hid +" to renter "+ rid +":" +comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void commentHost(int rid, int bid, String comment, String rate) {
        try {
            int hid = dao.getHidByBid(bid);
            dao.insertHostComment(rid, hid, comment, rate);
            //       System.out.println("comment from host"+ hid +" to renter "+ rid +":" +comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editPayment(int rid, String method, String card_num, String expire, String cvv) {
        try {
            dao.updatePayment(rid, method, card_num, expire, cvv);
            //       System.out.println("comment from host"+ hid +" to renter "+ rid +":" +comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getAllListings(int oid) {
        try {
            ArrayList<String> arr = new ArrayList<String>();
            ResultSet rs = dao.getListings(oid);
            while (rs.next()) {
                String alist = rs.getString("lid") + ", " + rs.getString("oid") + ", " +
                        rs.getString("type") + ", " + rs.getString("latitude") + ", " +
                        rs.getString("longitude") + ", " + rs.getString("address") + ", " +
                        rs.getString("city") + ", " + rs.getString("country") + ", " +
                        rs.getString("postal_code") + ", " + rs.getString("characteristics");
                arr.add(alist);
            }
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getAllHostBookings(int oid, boolean done) {
        try {
            ArrayList<String> arr = new ArrayList<String>();
            ResultSet rs = dao.getHostBookings(oid, done);
            while (rs.next()) {
                String aBook = rs.getString("bid") + ", " + rs.getString("rid") + ", " +
                        rs.getString("lid") + ", " + rs.getString("start") + ", " +
                        rs.getString("end") + ", " + rs.getString("status");
                arr.add(aBook);
            }
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getAllBookings(int rid, boolean done) {
        try {
            ArrayList<String> arr = new ArrayList<String>();
            ResultSet rs = dao.getRenterBookings(rid, done);
            while (rs.next()) {
                String aBook = rs.getString("bid") + ", " + rs.getString("rid") + ", " +
                        rs.getString("lid") + ", " + rs.getString("start") + ", " +
                        rs.getString("end") + ", " + rs.getString("status");
                arr.add(aBook);
            }
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getFilteredListing(String[] args) {
        try {
            ArrayList<String> arr = new ArrayList<String>();
            ResultSet rs = dao.queryListing(args);
            while (rs.next()) {
                String alist = rs.getString("lid") + ", " + rs.getString("oid") + ", " +
                        rs.getString("date") + ", " + rs.getString("price") + ", " +
                        rs.getString("type") + ", " + rs.getString("latitude") + ", " +
                        rs.getString("longitude") + ", " + rs.getString("address") + ", " +
                        rs.getString("city") + ", " + rs.getString("country") + ", " +
                        rs.getString("postal_code") + ", " + rs.getString("characteristics");
                arr.add(alist);
            }
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getSuggestedPrice(int lid) {
        try {
            String city = dao.getCityByLid(lid);
            if (city == null) return 0;
            return dao.getCityAvg(city);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
