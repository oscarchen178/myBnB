import java.sql.*;
import java.util.Calendar;

public class mysqlDao {

    private static final String dbClassName = "com.mysql.cj.jdbc.Driver";
    private static final String dbURL = "jdbc:mysql://localhost:3306/mydb";
    private Connection conn;

    public mysqlDao() {
        try {
            this.conn = DriverManager.getConnection(dbURL, "root", "123456");
            System.out.println("Mysql connected ~");
        } catch (Exception e) {
            System.out.println("Connection failed");
        }

    }

    public void resetDB() throws SQLException {
        // drop current tables
        Statement dropTables = conn.createStatement();
        String query1 = "DROP TABLE IF EXISTS ";
        String query2 = "users";
        String query3 = "listings";
    //    String query4 = "hosts";
        String query5 = "renters";
        String query6 = "bookings";
        String query7 = "calendars";
        String query8 = "listing_comments";
        String query9 = "renter_comments";
        String query10 = "host_comments";
        dropTables.executeUpdate(query1+query10);

        dropTables.executeUpdate(query1+query9);

        dropTables.executeUpdate(query1+query8);

        dropTables.executeUpdate(query1+query7);

        dropTables.executeUpdate(query1+query6);
        dropTables.executeUpdate(query1+query5);

        dropTables.executeUpdate(query1+query3);
        dropTables.executeUpdate(query1+query2);

        System.out.println("dropped tables !");
        // create new tables
        createUserTable();
        createListingTable();
        createCalendarTable();
        createRentersTable();
        createRenterCommentTable();
        createListingCommentTable();
        createHostCommentTable();
        createBookingsTable();
        // insert some data
        insertUser("Oscar", "50 Brian Harrison", "2001-2-27", "student", 666666);
        insertListing("full house", "33.33", "22.22", "1809 - 50 brian harrison", "fdfd", "2022-08-24", 299);

       // insertRenter(1, "fjdijeeeeenn", 39472074);
    }
    private void createListingCommentTable() throws SQLException {
        Statement stat = conn.createStatement();
        String query = "CREATE TABLE listing_comments(" +
                "rid INT," +
                "lid INT," +
                "comment VARCHAR(100), " +
                "rate INT," +
                "foreign key (lid) references listings(lid)," +
                "foreign key (rid) references renters(rid));";
        stat.executeUpdate(query);
        System.out.println("++ created table: listing_comments");
    }
    private void createRenterCommentTable() throws SQLException {
        Statement stat = conn.createStatement();
        String query = "CREATE TABLE renter_comments(" +
                "rid INT," +
                "hid INT," +
                "comment VARCHAR(100), " +
                "rate INT," +
                "foreign key (hid) references users(uid)," +
                "foreign key (rid) references renters(rid));";
        stat.executeUpdate(query);
        System.out.println("++ created table: listing_comments");
    }
    private void createHostCommentTable() throws SQLException {
        Statement stat = conn.createStatement();
        String query = "CREATE TABLE host_comments(" +
                "rid INT," +
                "hid INT," +
                "comment VARCHAR(100), " +
                "rate INT," +
                "foreign key (hid) references users(uid)," +
                "foreign key (rid) references renters(rid));";
        stat.executeUpdate(query);
        System.out.println("++ created table: listing_comments");
    }
    private void createUserTable() throws SQLException {
        Statement stat = conn.createStatement();
        String query = "CREATE TABLE users(" +
                "uid INT NOT NULL AUTO_INCREMENT, " +
                "name VARCHAR(30) NOT NULL, " +
                "address VARCHAR(100), " +
                "birthday DATE, " +
                "occupation VARCHAR(30), " +
                "sin INT, " +
                "PRIMARY KEY (uid));";
        stat.executeUpdate(query);
        System.out.println("++ created table: users");
    }

    private void createListingTable() throws SQLException {
        Statement stat = conn.createStatement();

        String query ="CREATE TABLE listings(oid INT, " +
                "Type ENUM('full house','apartment','room'), " +
                "latitude FLOAT(15) DEFAULT 0, " +
                "longitude FLOAT(15) DEFAULT 0, " +
                "Address VARCHAR(50), " +
                "Characteristics VARCHAR(20), " +
                "Calender_Availability DATE, " +
                "Price FLOAT(10) UNSIGNED, " +
                "lid INT NOT NULL AUTO_INCREMENT, " +
                "PRIMARY KEY (lid), " +
                "foreign key (oid) references users(uid));";
                 stat.executeUpdate(query);

        System.out.println("++ created table: listings");
    }

//    private void createHostsTable() throws SQLException {
//        Statement stat = conn.createStatement();
//
//        String query ="CREATE TABLE hosts(" +
//                "hid INT, " +
//                "history TEXT, " +
//                "foreign key (hid) references users(uid));";
//        stat.executeUpdate(query);
//        System.out.println("++ created table: hosts");
//    }

    private void createRentersTable() throws SQLException {
        Statement stat = conn.createStatement();

        String query ="CREATE TABLE renters(" +
                "rid INT," +
                "method ENUM('debit','credit')," +
                "card_num INT(16), " +
                "expire VARCHAR(5), " +
                "cvv INT(3)," +
                "foreign key (rid) references users(uid));";
        stat.executeUpdate(query);
        System.out.println("++ created table: renters");
    }
    private void createCalendarTable() throws SQLException {
        Statement stat = conn.createStatement();

        String query ="CREATE TABLE Calendars(" +
                "lid INT," +
                "date DATE," +
                "price INT(5), " +
                "available BOOL," +
                "foreign key (lid) references listings(lid));";
        stat.executeUpdate(query);
        System.out.println("++ created table: calenders");
    }
    private void createBookingsTable() throws SQLException {
        Statement stat = conn.createStatement();

        String query ="CREATE TABLE bookings(" +
                "rid INT, " +
                "hid INT, " +
                "lid INT, " +
                "startdate DATE," +
                "enddate Date," +
                "status ENUM('done','ongoing','booked','canceled')," +
                "foreign key (rid) references renters(rid)," +
                "foreign key (hid) references users(uid)," +
                "foreign key (lid) references listings(lid));";
        stat.executeUpdate(query);
        System.out.println("++ created table: bookings");
    }
    public void insertUser(String name, String addr, String date, String occup, int sin) throws SQLException {
        Statement stat = conn.createStatement();
        String query = "INSERT INTO users " +
                "(name, address, birthday, occupation, sin) " +
                "VALUES " +
                "('%s', '%s', '%s', '%s', %d)";
        query = String.format(query, name, addr, date, occup, sin);
        stat.executeUpdate(query);
        System.out.println("++ inserted user: "+name);
    }
    public void insertListing(String type, String latitude, String longitude, String Address, String Characteristics, String Calendar,float Price) throws SQLException {
        Statement stat = conn.createStatement();
        String query = "INSERT INTO listings " +
                "(Type, latitude, longitude, Address, Characteristics, Calender_Availability, Price) " +
                "VALUES " +
                "('%s', '%s', '%s', '%s', '%s', '%s', '%f')";
        query = String.format(query, type, latitude, longitude,Address, Characteristics, Calendar, Price);
        stat.executeUpdate(query);
        System.out.println("++ inserted user: "+Address);
    }
//    public void insertHost(int hid, String history) throws SQLException {
//        Statement stat = conn.createStatement();
//        String query = "INSERT INTO hosts " +
//                "(hid, history) " +
//                "VALUES " +
//                "('%d', '%s')";
//        query = String.format(query, hid, history);
//        stat.executeUpdate(query);
//        System.out.println("++ inserted host: "+hid);
//    }
    public void insertRenter(int hid, String history, int payment_info) throws SQLException {
        Statement stat = conn.createStatement();
        String query = "INSERT INTO renters " +
                "(rid, history, payment_info) " +
                "VALUES " +
                "('%d', '%s', '%d')";
        query = String.format(query, hid, history, payment_info);
        stat.executeUpdate(query);
        System.out.println("++ inserted host: "+hid);
    }
}
