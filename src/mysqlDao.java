import java.sql.*;

public class mysqlDao {
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

    public void dropAllTables() throws SQLException {
        Statement dropTables = conn.createStatement();
        String query1 = "DROP TABLE IF EXISTS ";
        String query2 = "users";
        String query3 = "listings";
//        String query4 = "hosts";
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
//        dropTables.executeUpdate(query1+query4);
        dropTables.executeUpdate(query1+query3);
        dropTables.executeUpdate(query1+query2);
        dropTables.executeUpdate("DROP TABLE IF EXISTS accounts");

    }

    public void resetDB() throws SQLException {
        // drop current tables
        dropAllTables();
        System.out.println("dropped tables !");
        createAccountTable();
        // create new tables
        createUserTable();
        createListingTable();
  //      pushData();
        createCalendarTable();
        createRentersTable();
        createRenterCommentTable();
        createListingCommentTable();
        createHostCommentTable();
        createBookingsTable();
        System.out.println("Tables create complete ~");
        // insert some data
        insertAccount("aaa@mail.com", "123456");
        insertUser(1,"Oscar", "50 Brian Harrison", "2001-2-27", "student", 666666);
        insertListing(1, "full house", "33.33", "22.22", "1809, 50 brian harrison",
                "Scarborough", "Canada", "M2P 6J4", "('Shampoo,Dishwasher')");
        insertRenter(1, "credit", 88888888, "25/07", 183);
        insertCalender(1, "2022-8-12", 200, "true");
        insertCalender(1, "2022-8-13", 200, "true");
        insertBooking(1, 1, "2022-7-6", "2022-7-11", "booked");
        insertListingComment(1, 1, "Hello", "1");
        insertRenterComment(1, 1, "Hello", "1");
        insertHostComment(1, 1, "Hello", "1");
        editUserProfile(1, "qiqiqiqiqiqi", "wenzhou", "1997-01-01", "musician", 193382);
        editPayment(1,"credit", 38838,"11/28", 123);
    }
//    public void pushData() throws SQLException {
//        Statement stat = conn.createStatement();
//        String query = "LOAD DATA INFILE 'C:\\github\\myBnB\\src\\listings.csv'" +
//                "INTO TABLE listings" +
//                "FIELDS TERMINATED BY ',' " +
//                "ENCLOSED BY '\"'" +
//                "LINES TERMINATED BY '\\n'" +
//                "IGNORE 1 ROWS;";
//        stat.executeUpdate(query);
//        System.out.println("pushdata");
//    }
    private void createListingCommentTable() throws SQLException {
        Statement stat = conn.createStatement();
        String query = "CREATE TABLE listing_comments(" +
                "rid INT, " +
                "lid INT, " +
                "comment TEXT, " +
                "rate ENUM('1', '2', '3', '4', '5'), " +
                "foreign key (lid) references listings(lid), " +
                "foreign key (rid) references renters(rid));";
        stat.executeUpdate(query);
        System.out.println("++ created table: listing_comments");
    }

    private void createRenterCommentTable() throws SQLException {
        Statement stat = conn.createStatement();
        String query = "CREATE TABLE renter_comments(" +
                "hid INT, " +
                "rid INT, " +
                "comment TEXT, " +
                "rate ENUM('1', '2', '3', '4', '5'), " +
                "foreign key (hid) references users(uid), " +
                "foreign key (rid) references renters(rid));";
        stat.executeUpdate(query);
        System.out.println("++ created table: renter_comments");
    }

    private void createHostCommentTable() throws SQLException {
        Statement stat = conn.createStatement();
        String query = "CREATE TABLE host_comments(" +
                "rid INT, " +
                "hid INT, " +
                "comment TEXT, " +
                "rate ENUM('1', '2', '3', '4', '5'), " +
                "foreign key (hid) references users(uid), " +
                "foreign key (rid) references renters(rid));";
        stat.executeUpdate(query);
        System.out.println("++ created table: host_comments");
    }

    private void createUserTable() throws SQLException {
        Statement stat = conn.createStatement();
        String query = "CREATE TABLE users(" +
                "uid INT, " +
                "name VARCHAR(30) NOT NULL, " +
                "address VARCHAR(100), " +
                "birthday DATE, " +
                "occupation VARCHAR(30), " +
                "sin INT, " +
                "foreign key (uid) references accounts(aid));";
        stat.executeUpdate(query);
        System.out.println("++ created table: users");
    }
    public void editUserProfile(int uid, String name, String address, String birthday, String occu, int sin) throws SQLException {
        Statement stat = conn.createStatement();
        String query = " UPDATE users set name='%s', address='%s', birthday='%s', occupation='%s', sin=%d where uid=%d";
        query = String.format(query, name,address,birthday,occu,sin, uid);
        stat.executeUpdate(query);
        System.out.println("++ edit profile uid:" + uid);
    }

    private void createAccountTable() throws SQLException {
        Statement stat = conn.createStatement();
        String query = "CREATE TABLE accounts(" +
                "aid INT NOT NULL AUTO_INCREMENT, " +
                "email VARCHAR(30) NOT NULL, " +
                "password VARCHAR(100), " +
                "PRIMARY KEY (aid));";
        stat.executeUpdate(query);
        System.out.println("++ created table: accounts");
    }
    private void createListingTable() throws SQLException {
        String amenities = "'Luggage drop-off allowed', 'Kitchen', 'Free washer – In building', 'Hair dryer', " +
                "'Shampoo', 'Ethernet connection', 'Air conditioning', 'Indoor fireplace: gas', " +
                "'Security cameras on property', 'Dishwasher', 'Barbecue utensils'";

        Statement stat = conn.createStatement();
        String query ="CREATE TABLE listings(" +
                "lid INT NOT NULL AUTO_INCREMENT, " +
                "oid INT, " +
                "type ENUM('full house','apartment','room'), " +
                "latitude FLOAT(15) DEFAULT 0, " +
                "longitude FLOAT(15) DEFAULT 0, " +
                "address VARCHAR(50), " +
                "city VARCHAR(20), " +
                "country VARCHAR(20), " +
                "postal_code VARCHAR(10), " +
                "characteristics SET(" + amenities + "), " +
                "PRIMARY KEY (lid), " +
                "foreign key (oid) references users(uid));";
        stat.executeUpdate(query);
        System.out.println("++ created table: listings");
    }

    private void createRentersTable() throws SQLException {
        Statement stat = conn.createStatement();
        String query ="CREATE TABLE renters(" +
                "rid INT NOT NULL," +
                "method ENUM('debit','credit'), " +
                "card_num INT(16), " +
                "expire VARCHAR(5), " +
                "cvv INT(3), " +
                "foreign key (rid) references users(uid));";
        stat.executeUpdate(query);
        System.out.println("++ created table: renters");
    }

    public void editPayment(int rid, String method, int card_num, String expire, int cvv) throws SQLException {
        Statement stat = conn.createStatement();
        String query = " UPDATE renters set method='%s', card_num=%d, expire='%s', cvv='%d'where rid=%d";
        query = String.format(query, method,card_num,expire,cvv,rid);
        stat.executeUpdate(query);
        System.out.println("++ edit payment rid:" + rid);
    }

    private void createCalendarTable() throws SQLException {
        Statement stat = conn.createStatement();
        String query ="CREATE TABLE calendars(" +
                "lid INT NOT NULL, " +
                "date DATE NOT NULL, " +
                "price INT(5) NOT NULL, " +
                "available BOOL DEFAULT false, " +
                "foreign key (lid) references listings(lid) ON DELETE CASCADE);";
        stat.executeUpdate(query);
        System.out.println("++ created table: calenders");
    }
    private void createBookingsTable() throws SQLException {
        Statement stat = conn.createStatement();

        String query ="CREATE TABLE bookings(" +
                "bid INT NOT NULL AUTO_INCREMENT," +
                "rid INT NOT NULL, " +
//                "hid INT NOT NULL, " +
                "lid INT NOT NULL, " +
                "start DATE NOT NULL, " +
                "end DATE NOT NULL, " +
                "status ENUM('done','ongoing','booked','canceled') DEFAULT 'booked', " +
                "foreign key (rid) references renters(rid) ON DELETE CASCADE, " +
//                "foreign key (hid) references users(uid), " +
                "foreign key (lid) references listings(lid) ON DELETE CASCADE, " +
                "PRIMARY KEY (bid));";
        stat.executeUpdate(query);
        System.out.println("++ created table: bookings");
    }

    public void insertAccount(String email, String psw) throws SQLException {
        Statement stat = conn.createStatement();
        String query = "INSERT INTO accounts " +
                "(email, password) " +
                "VALUES " +
                "('%s', '%s')";
        query = String.format(query, email, psw);
        stat.executeUpdate(query);
        System.out.println("++ register email: "+ email);
    }


    public void insertUser(int aid, String name, String addr, String date, String occup, int sin) throws SQLException {
        Statement stat = conn.createStatement();
        String query = "INSERT INTO users " +
                "(uid, name, address, birthday, occupation, sin) " +
                "VALUES " +
                "(%d, '%s', '%s', '%s', '%s', %d)";
        query = String.format(query, aid, name, addr, date, occup, sin);
        stat.executeUpdate(query);
        System.out.println("++ inserted user: "+name);
    }

    public void insertListing(int oid, String type, String latitude, String longitude, String address,
                              String city, String country, String postal_code, String characteristics) throws SQLException {
        Statement stat = conn.createStatement();
        String query = "INSERT INTO listings " +
                "(oid, type, latitude, longitude, address, city, country, postal_code, characteristics) " +
                "VALUES " +
                "(%s, '%s', %s, %s, '%s', '%s', '%s', '%s', %s)";
        query = String.format(query, oid, type, latitude, longitude, address, city, country, postal_code, characteristics);
        stat.executeUpdate(query);
        System.out.println("++ inserted listing: "+address);
    }



    public void insertRenter(int hid, String method, int card_num, String expire, int cvv) throws SQLException {
        Statement stat = conn.createStatement();
        String query = "INSERT INTO renters " +
                "(rid, method, card_num, expire, cvv) " +
                "VALUES " +
                "(%d, '%s', %d, '%s', %d)";
        query = String.format(query, hid, method, card_num, expire, cvv);
        stat.executeUpdate(query);
        System.out.println("++ inserted host: "+hid);
    }

    public void insertCalender(int lid, String date, int price, String available) throws SQLException {
        Statement stat = conn.createStatement();
        String query = "INSERT INTO calendars " +
                "(lid, date, price, available) " +
                "VALUES " +
                "(%d, '%s', %d, %s)";
        query = String.format(query, lid, date, price, available);
        stat.executeUpdate(query);
        System.out.println("++ inserted calender: " + lid + " " + date);
    }

    public void insertBooking(int rid, int lid, String start, String end, String status) throws SQLException {
        Statement stat = conn.createStatement();
        String query = "INSERT INTO bookings " +
                "(rid, lid, start, end, status) " +
                "VALUES " +
                "(%d, %d, '%s', '%s', '%s')";
        query = String.format(query, rid, lid, start, end, status);
        stat.executeUpdate(query);
        System.out.println("++ inserted booking: " + rid + " " + lid);
    }

    public void insertListingComment(int rid, int lid, String comment, String rate) throws SQLException {
        Statement stat = conn.createStatement();
        String query = "INSERT INTO listing_comments " +
                "(rid, lid, comment, rate) " +
                "VALUES " +
                "(%d, %d, '%s', '%s')";
        query = String.format(query, rid, lid, comment, rate);
        stat.executeUpdate(query);
        System.out.println("++ inserted listing comment: " + rid + "->" + lid + " " + comment);
    }

    public void insertRenterComment(int hid, int rid, String comment, String rate) throws SQLException {
        Statement stat = conn.createStatement();
        String query = "INSERT INTO renter_comments " +
                "(hid, rid, comment, rate) " +
                "VALUES " +
                "(%d, %d, '%s', '%s')";
        query = String.format(query, hid, rid, comment, rate);
        stat.executeUpdate(query);
        System.out.println("++ inserted renter comment: " + hid + "->" + rid + " " + comment);
    }

    public void insertHostComment(int rid, int hid, String comment, String rate) throws SQLException {
        Statement stat = conn.createStatement();
        String query = "INSERT INTO host_comments " +
                "(rid, hid, comment, rate) " +
                "VALUES " +
                "(%d, %d, '%s', '%s')";
        query = String.format(query, rid, hid, comment, rate);
        stat.executeUpdate(query);
        System.out.println("++ inserted host comment: " + rid + "->" + hid + " " + comment);
    }

    public boolean checkBookAvailable(int lid, String start, String end, long length) throws SQLException {
        Statement stat = conn.createStatement();
        String query = "SELECT count(*) AS COUNT FROM calendars " +
                "WHERE lid=%d AND available=true AND DATE(date) " +
                "BETWEEN '%s' AND '%s'";
        query = String.format(query, lid, start, end);
        ResultSet res = stat.executeQuery(query);
        if (res.next()) {
            long count = res.getLong("COUNT");
//            System.out.println("count: " + count);
            return count == length;
        }
        return false;
    }

    public void updateAvailable(int lid, String start, String end, String available) throws SQLException {
        Statement stat = conn.createStatement();
        String query = "UPDATE calendars " +
                "SET available = %s " +
                "WHERE lid=%d AND DATE(date) " +
                "BETWEEN '%s' AND '%s'";
        query = String.format(query, available, lid, start, end);
        stat.executeUpdate(query);
    }

    public void updateBookingStatus(int bid, int lid, String status) throws SQLException {
        Statement stat = conn.createStatement();
        if (lid == -1) {
            String query = "UPDATE bookings " +
                    "SET status='%s' " +
                    "WHERE bid=%d";
            query = String.format(query, status, bid);
            stat.executeUpdate(query);
        } else {
            String query = "UPDATE bookings " +
                    "SET status='%s' " +
                    "WHERE lid=%d AND status!='done'";
            query = String.format(query, status, lid);
            stat.executeUpdate(query);
        }
    }

    public ResultSet getBooking(int bid) throws SQLException {
        Statement stat = conn.createStatement();
        String query = "SELECT * FROM bookings WHERE bid=%d";
        query = String.format(query, bid);
        return stat.executeQuery(query);
    }

    public void updateCalendarPrice(int lid, int price, String date) throws SQLException {
        Statement stat = conn.createStatement();
        String query = "UPDATE calendars SET price = %d WHERE date = '%s' AND lid = %d AND available = true";
        query = String.format(query, price, date, lid);
        stat.executeUpdate(query);
    }

    public void deleteCalendar(int lid) throws SQLException {
        Statement stat = conn.createStatement();
        String query = "DELETE FROM calendars WHERE lid = %d";
        query = String.format(query, lid);
        stat.executeUpdate(query);
    }

}
