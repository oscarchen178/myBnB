import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class operation {

    private mysqlDao dao;

    public operation() {
        this.dao = new mysqlDao();
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
    }
}
