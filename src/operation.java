import java.sql.SQLException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class operation {

    private mysqlDao dao;

    public operation(mysqlDao dao) {
        this.dao = dao;
    }

    //create a new listing and associate it with their account, cancel a booking or remove a listing. change price
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
            dao.updateCalendarPrice(lid, price, date);
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
    public void register(String email, String psw) {
        try {
            dao.register(email, psw);
      //      System.out.println("register:"+email +",:" +psw);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void editProfile(int uid, String name, String address, String birthday, String occu, int sin) {
        try {
            dao.insertUser(uid,name,address, birthday, occu, sin);
    //        System.out.println("register:"+email +",:" +psw);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void commentListing(int rid, int lid, String comment, String rate) {
        try {
            dao.insertListingComment(rid, lid, comment, rate);
  //          System.out.println("comment about"+lid +":" +comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void commentRenter(int hid, int rid, String comment, String rate) {
        try {
            dao.insertRenterComment(hid, rid, comment, rate);
     //       System.out.println("comment from host"+ hid +" to renter "+ rid +":" +comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void commentHost(int rid, int hid, String comment, String rate) {
        try {
            dao.insertHostComment(rid, hid, comment, rate);
            //       System.out.println("comment from host"+ hid +" to renter "+ rid +":" +comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void editPayment(int rid, String method, int card_num, String expire, int cvv) throws SQLException {
        try {
            dao.editPayment(rid, method, card_num, expire, cvv);
            //       System.out.println("comment from host"+ hid +" to renter "+ rid +":" +comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
