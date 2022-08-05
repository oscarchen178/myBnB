import java.sql.*;

public class mysqlDao {

    private static final String dbClassName = "com.mysql.cj.jdbc.Driver";
    private static final String dbURL = "jdbc:mysql://localhost:3306/mydb";
    private Connection conn;

    public mysqlDao() {
        try {
            this.conn = DriverManager.getConnection(dbURL, "root", "f1785678");
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
        dropTables.executeUpdate(query1+query2);
        System.out.println("dropped tables !");

        // create new tables
        createUserTable();

        // insert some data
        insertUser("Oscar", "50 Brian Harrison", "2001-2-27", "student", 666666);
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


}
