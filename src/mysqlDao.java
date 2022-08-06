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
        String query4 = "hosts";
        String query5 = "renters";
        dropTables.executeUpdate(query1+query3);
        dropTables.executeUpdate(query1+query4);
        dropTables.executeUpdate(query1+query5);
        dropTables.executeUpdate(query1+query2);
        System.out.println("dropped tables !");
        // create new tables
        createUserTable();
        createListingTable();
        createHostsTable();
        createRentersTable();
        // insert some data
        insertUser("Oscar", "50 Brian Harrison", "2001-2-27", "student", 666666);
        insertListing("full house", "33.33", "22.22", "1809 - 50 brian harrison", "fdfd", "2022-08-24", 299);
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
        String query ="CREATE TABLE listings(" +
                "oid INT, " +
                "Type ENUM('full house','apartment','room'), " +
                "latitude FLOAT(15) DEFAULT 0, " +
                "longitude FLOAT(15) DEFAULT 0, " +
                "`Address` VARCHAR(50), " +
                "`Characteristics` VARCHAR(20), " +
                "`Calender_Availability` DATE, " +
                "`Price` FLOAT(10) UNSIGNED, " +
                "foreign key (oid) references users(uid));";
        stat.executeUpdate(query);
        System.out.println("++ created table: listings");
    }

    private void createHostsTable() throws SQLException {
        Statement stat = conn.createStatement();
        String query ="CREATE TABLE hosts(" +
                "hid INT, " +
                "history TEXT, " +
                "foreign key (hid) references users(uid));";
        stat.executeUpdate(query);
        System.out.println("++ created table: hosts");
    }

    private void createRentersTable() throws SQLException {
        Statement stat = conn.createStatement();
        String query ="CREATE TABLE renters(" +
                "rid INT, " +
                "payment_info INT(16), " +
                "history TEXT, " +
                "foreign key (rid) references users(uid));";
        stat.executeUpdate(query);
        System.out.println("++ created table: renters");
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
    public void insertHost(int hid, String history) throws SQLException {
        Statement stat = conn.createStatement();
        String query = "INSERT INTO hosts " +
                "(hid, history) " +
                "VALUES " +
                "('%d', '%s')";
        query = String.format(query, hid, history);
        stat.executeUpdate(query);
        System.out.println("++ inserted host: "+hid);
    }
//    public void insertRenter(String type, String latitude, String longitude, String Address, String Characteristics, String Calendar,float Price) throws SQLException {
//        Statement stat = conn.createStatement();
//        String query = "INSERT INTO listings " +
//                "(Type, latitude, longitude, Address, Characteristics, Calender_Availability, Price) " +
//                "VALUES " +
//                "('%s', '%s', '%s', '%s', '%s', '%s', '%f')";
//        query = String.format(query, type, latitude, longitude,Address, Characteristics, Calendar, Price);
//        stat.executeUpdate(query);
//        System.out.println("++ inserted user: "+Address);
//    }
}
