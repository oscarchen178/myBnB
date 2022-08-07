import java.util.Scanner;

public class Driver {



    public static void main(String[] args) {
        try {

            mysqlDao dao = new mysqlDao();
            dao.resetDB();
            operation op = new operation(dao);

//            op.book(1,1,"2022-8-12", "2022-8-13");
//            op.book(1,1,"2022-8-12", "2022-8-12");
//            op.register("abc@mail","123453");
//            op.commentListing(1,1, "good room, good !!!!", "4");
//            op.editProfile(2, "mfmfm", "shanghai", "1987-01-01", "teacher", 2333382);
//            op.commentRenter(2,1,"nihao","4");
//            op.commentHost(1,2,"good host","4");
//            op.cancelBook(2);
//            op.book(1,1,"2022-8-12", "2022-8-12");
//            op.changeCalendarPrice(1, 180, "2022-8-12");
//            op.changeCalendarPrice(1, 180, "2022-8-13");
//            op.deleteListing(1);
//            dao.rentersRankByPeriod("2000-01-01", "2100-01-10");
//            dao.rentersRankByPeriodAndCity("2000-01-01", "2100-01-10", "Scarborough");
//            dao.rentersLargestCanceled();
//            dao.hostsLargestCanceled();
//            dao.getBookingsNumberCityPeriod("2000-01-01","2030-11-11", "Scarborough");
//            dao.getBookingsNumberCityPostal("2000-01-01","2030-11-11", "Scarborough","M2P 6J4");


            Scanner s = new Scanner(System.in);
            cmdHandler cmd = new cmdHandler(op, s);
            while(true) {
                cmd.exec();
            }


        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
