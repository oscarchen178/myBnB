//import java.net.InetSocketAddress;
//import com.sun.net.httpserver.HttpServer;

public class Driver {



    public static void main(String[] args) {
        try {
            mysqlDao dao = new mysqlDao();
            dao.resetDB();

            operation op = new operation();
            op.book(1,1,"2022-8-12", "2022-8-13");
            op.book(1,1,"2022-8-12", "2022-8-12");
//            HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 5000), 0);
//            server.createContext("/api", new Trip());
//            server.start();
//            System.out.printf("Server started on port %d...\n", 5000);

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
