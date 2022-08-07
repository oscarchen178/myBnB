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
            op.register("abc@mail","123453");
            op.commentListing(1,1, "good room, good !!!!", "4");
            op.editProfile(2, "mfmfm", "shanghai", "1987-01-01", "teacher", 2333382);
            op.commentRenter(2,1,"nihao","4");
            op.commentHost(1,2,"good host","4");
            //            HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 5000), 0);
//            server.createContext("/api", new Trip());
//            server.start();
//            System.out.printf("Server started on port %d...\n", 5000);

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
