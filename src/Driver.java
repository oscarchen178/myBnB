

public class Driver {



    public static void main(String[] args) {
        try {
            mysqlDao dao = new mysqlDao();
            dao.resetDB();

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
