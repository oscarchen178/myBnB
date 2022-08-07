import java.util.ArrayList;
import java.util.Scanner;

public class cmdHandler {

    private operation op;
    private Scanner s;
    private int uid;
    private int state;

    public cmdHandler(operation op, Scanner s) {
        this.op = op;
        this.state = 0;
        this.s = s;
    }

    public void exec() {
        System.out.println("------------------------------------------");
        if (state == 0) {
            init();
        } else if (state == 1) {
            login();
        } else if (state == 2) {
            register();
        } else if (state == 3) {
            home();
        } else if (state == 4) {
            profile();
        } else if (state == 5) {
            renter();
        } else if (state == 6) {
            payment();
        } else if (state == 7) {
            booklisting();
        } else if (state == 8) {
            managebooking();
        } else if (state == 9) {
            cmtlisting();
        } else if (state == 10) {
            cmthost();
        } else if (state == 11) {
            host();
        } else if (state == 12) {
            createlisting();
        } else if (state == 13) {
            managelisting();
        } else if (state == 14) {
            managehostbooking();
        } else if (state == 15) {
            cmtrenter();
        }
    }

    public String[] getArguments() {
        String line = s.nextLine();
        String[] arguments = line.split(" ");
        if (arguments[0].equals("exit")) {
            System.exit(0);
            return null;
        }
        return arguments;
    }

    public String getArgLine() {
        return s.nextLine();
    }

    public void init() {
        System.out.println("Welcome to MyBnB ~");
        System.out.println("1, Login");
        System.out.println("2, Register");
        System.out.print("Your choice: ");
        String[] arg = getArguments();
        if (arg[0].equals("1")) this.state = 1;
        else if (arg[0].equals("2")) this.state = 2;
        else System.out.println("Incorrect input!");
    }

    public void login() {
        System.out.println("Login ...");
        System.out.print("Email: ");
        String[] arg = getArguments();
        boolean exist = op.checkEmail(arg[0]);
        if (!exist) {
            System.out.println("This user does not exist!");
            this.state = 0;
            return;
        }
        String email = arg[0];
        System.out.print("Password: ");
        arg = getArguments();
        int uid = op.login(email, arg[0]);
        if (uid == -1) {
            System.out.println("Login failed!");
            this.state = 0;
            return;
        }
        this.uid = uid;
        System.out.println("Your uid: " + uid);
        this.state = 3;
    }

    public void register() {
        System.out.println("Login ...");
        System.out.print("Email: ");
        String[] arg = getArguments();
        boolean exist = op.checkEmail(arg[0]);
        if (exist) {
            System.out.println("User already exist!");
            this.state = 0;
            return;
        }
        String email = arg[0];
        System.out.print("Password: ");
        arg = getArguments();
        String pswd = arg[0];
        System.out.print("Name: ");
        arg = getArguments();
        String name = arg[0];
        System.out.print("Address: ");
        String addr = getArgLine();
        System.out.print("Birthday: ");
        arg = getArguments();
        String date = arg[0];
        // check date format
        System.out.print("Occupation: ");
        arg = getArguments();
        String occup = arg[0];
        System.out.print("SIN number: ");
        arg = getArguments();
        String sin = arg[0];
        // check is int
        int sinNum = Integer.parseInt(sin);
        int uid = op.register(email, pswd, name, addr, date, occup, sinNum);
        this.uid = uid;
        System.out.println("Your uid: " + uid);
        this.state = 3;
    }

    public void home() {
        System.out.println("Home Page");
        System.out.println("1, Renter");
        System.out.println("2, Host");
        System.out.println("3, Profile");
        System.out.println("4, Logout");
        System.out.print("Your choice: ");
        String[] arg = getArguments();
        if (arg[0].equals("4")) {
            System.out.println("Logout ...");
            this.state = 0;
        }
        if (arg[0].equals("1")) {
            System.out.println("Go to Renter ...");
            this.state = 5;
        }
        if (arg[0].equals("2")) {
            System.out.println("Go to Host ...");
            this.state = 11;
        }
        if (arg[0].equals("3")) {
            System.out.println("Go to Profile ...");
            this.state = 4;
        }
    }

    public void profile() {
        System.out.println("Profile");
        String[] profile = op.getProfile(this.uid);
        if (profile == null) {
            System.out.println("No profile!");
            this.state = 3;
            return;
        }
        System.out.println("Name: " + profile[0] + " Address: " + profile[1] + " Birthday: " + profile[2] +
                " Occupation: " + profile[3] + " SIN: " + profile[4]);
        System.out.print("Edit Profile (yes/no)? ");
        String[] arg = getArguments();
        if (arg[0].equals("yes")) {
            System.out.println("New Profile ~");
            System.out.print("Name: ");
            arg = getArguments();
            String name = arg[0];
            System.out.print("Address: ");
            String addr = getArgLine();
            System.out.print("Birthday: ");
            arg = getArguments();
            String date = arg[0];
            // check date format
            System.out.print("Occupation: ");
            arg = getArguments();
            String occup = arg[0];
            System.out.print("SIN number: ");
            arg = getArguments();
            String sin = arg[0];
            op.editProfile(this.uid, name, addr, date, occup, sin);
            System.out.println("Updated ~");
        }
        this.state = 3;
    }

    public void renter() {
        System.out.println("Renter");
        System.out.println("1, Payment");
        System.out.println("2, Book a Listing");
        System.out.println("3, Manage Booking");
        System.out.println("4, Comment to Listing");
        System.out.println("5, Comment to Host");
        System.out.println("6, Back to Home");
        System.out.println("7, Logout");
        System.out.print("Your choice: ");
        String input = getArgLine();
        if (input.equals("1")) this.state = 6;
        if (input.equals("2")) this.state = 7;
        if (input.equals("3")) this.state = 8;
        if (input.equals("4")) this.state = 9;
        if (input.equals("5")) this.state = 10;
        if (input.equals("6")) this.state = 3;
        if (input.equals("7")) this.state = 0;
    }

    public void payment() {
        System.out.println("Payment");
        String[] payment = op.getPayment(this.uid);
        System.out.println("Method: " + payment[0] + " Card#: " + payment[1] + " Expire: " + payment[2] +
                " CVV: " + payment[3]);
        System.out.print("Edit Payment (yes/no)? ");
        String input = getArgLine();
        if (input.equals("yes")) {
            System.out.println("New Payment Method");
            System.out.print("Method: ");
            String method = getArgLine();
            System.out.print("Card#: ");
            String card = getArgLine();
            System.out.print("Expire: ");
            String expire = getArgLine();
            System.out.print("CVV: ");
            String cvv = getArgLine();
            op.editPayment(this.uid, method, card, expire, cvv);
        }
        this.state = 5;
    }

    public void booklisting() {

    }

    public void managebooking() {

    }

    public void cmtlisting() {

    }

    public void cmthost() {

    }

    public void host() {
        System.out.println("Host");
        System.out.println("1, Create Listing");
        System.out.println("2, Manage Listing");
        System.out.println("3, Manage Booking");
        System.out.println("4, Comment to Renter");
        System.out.println("5, Back to Home");
        System.out.println("6, Logout");
        System.out.print("Your choice: ");
        String input = getArgLine();
        if (input.equals("1")) this.state = 12;
        if (input.equals("2")) this.state = 13;
        if (input.equals("3")) this.state = 14;
        if (input.equals("4")) this.state = 15;
        if (input.equals("5")) this.state = 3;
        if (input.equals("6")) this.state = 0;
    }

    public void createlisting() {
        System.out.println("Create A New Listing ~");
        System.out.print("Type: ");
        String type = getArgLine();
        System.out.print("Latitude: ");
        String latitude = getArgLine();
        System.out.print("Longitude: ");
        String longitude = getArgLine();
        System.out.print("Address: ");
        String address = getArgLine();
        System.out.print("City: ");
        String city = getArgLine();
        System.out.print("Country: ");
        String country = getArgLine();
        System.out.print("Postal Code: ");
        String post = getArgLine();
        String amenities = "'Luggage drop-off allowed', 'Kitchen', 'Free washer – In building', 'Hair dryer', " +
                "'Shampoo', 'Ethernet connection', 'Air conditioning', 'Indoor fireplace: gas', " +
                "'Security cameras on property', 'Dishwasher', 'Barbecue utensils'";
        System.out.println("Choose from: " + amenities);
        System.out.print("characteristics: ");
        String chara = getArgLine();
        op.createListing(this.uid, type, latitude, longitude, address, city, country, post, "('"+chara+"')");
        this.state = 11;
    }

    public void managelisting() {
        System.out.println("My listings");
        ArrayList<String> listings = op.getAllListings(this.uid);
        for (String listing : listings) {
            System.out.println(listing);
        }
        System.out.print("Choose a listing lid or 'back': ");
        String lidStr = getArgLine();
        if (lidStr.equals("back")) {
            this.state = 11;
            return;
        }
        int lid = Integer.parseInt(lidStr);
        System.out.println("1, Edit Price");
        System.out.println("2, Insert New Date");
        System.out.println("3, Edit Availability");
        System.out.println("4, Back to Host");
        System.out.print("Your choice: ");
        String input = getArgLine();
        if (input.equals("1")) {
            System.out.println("------------");
            System.out.println("Edit Price");
            System.out.print("Date: ");
            String date = getArgLine();
            System.out.print("Price: ");
            String price = getArgLine();
            op.changeCalendarPrice(lid, Integer.parseInt(price), date);
        }
        if (input.equals("2")) {
            System.out.println("------------");
            System.out.println("Insert new date to Calendar");
            System.out.print("Date: ");
            String date = getArgLine();
            System.out.print("Price: ");
            String price = getArgLine();
            System.out.print("Available: ");
            String ava = getArgLine();
            op.insertNewDate(lid, date, Integer.parseInt(price), ava);
        }
        if (input.equals("3")) {
            System.out.println("------------");
            System.out.println("Edit Available");
            System.out.print("Date: ");
            String date = getArgLine();
            System.out.print("Available: ");
            String ava = getArgLine();
            op.changeCalendarAvailable(lid, ava, date);
        }
        if (input.equals("4")) {
            this.state = 11;
        }
    }

    public void managehostbooking() {

    }

    public void cmtrenter() {

    }
}
