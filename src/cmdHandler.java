import java.util.ArrayList;
import java.util.Scanner;

public class cmdHandler {

    private operation op;
    private Scanner s;
    private int uid;
    private int state;
    mysqlDao dao;
    public cmdHandler(operation op, Scanner s, mysqlDao dao) {
        this.op = op;
        this.state = 0;
        this.s = s;
        this.dao = dao;
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
            rentercmt();
        } else if (state == 10) {
            hostcmt();
        } else if (state == 11) {
            host();
        } else if (state == 12) {
            createlisting();
        } else if (state == 13) {
            managelisting();
        } else if (state == 14) {
            managehostbooking();
        } else if (state == 15){
            managereport();
        } else if (state == 16){
            manageGetBookingsNumberCityPeriod();
        } else if (state == 17){
             manageGetBookingsNumberCityPostal();
        } else if (state == 18){
            manageGetListingsNumberCountry();
        }else if (state == 19){
            manageGetListingsNumberCity();
        }else if (state == 20){
            manageGetListingsNumberPostal();
        }else if (state == 21){
            manageListingsRankByCountry();
        }else if (state == 22){
            manageListingsRankByCity();
        }else if (state == 23){
            manageListingsOwnerMoreThanTenPersentByCountry();
        }else if (state == 24){
            manageListingsOwnerMoreThanTenPersentByCity();
        }else if (state == 25){
            manageRentersRankByPeriod();
        }else if (state == 26){
            manageRentersRankByPeriodAndCity();
        }else if (state == 27){
            manageHostsLargestCanceled();
        }else if (state == 28){
            manageRentersLargestCanceled();
        }else if (state == 29){
            manageCommentRank();
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
        System.out.println("3, Report");
        System.out.print("Your choice: ");
        String[] arg = getArguments();
        if (arg[0].equals("1")) this.state = 1;
        else if (arg[0].equals("2")) this.state = 2;
        else if (arg[0].equals("3")) this.state = 15;
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
        System.out.println("Register ...");
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
        System.out.println("5, delete user");
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
        if (arg[0].equals("5")) {
            System.out.println("Type DELETE to confirm: ");
            String input = getArgLine();
            if (input.equals("DELETE")) {
                System.out.println("Deleting ...");
                op.deleteUser(this.uid);
                this.state = 0;
            }
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
        System.out.println("4, Write Comment");
        System.out.println("5, Back to Home");
        System.out.println("6, Logout");
        System.out.print("Your choice: ");
        String input = getArgLine();
        if (input.equals("1")) this.state = 6;
        if (input.equals("2")) this.state = 7;
        if (input.equals("3")) this.state = 8;
        if (input.equals("4")) this.state = 9;
        if (input.equals("5")) this.state = 3;
        if (input.equals("6")) this.state = 0;
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
        String filter;
        System.out.println("Find A Listing then Book ~");
        System.out.println("Filters");
        System.out.println("1, latitude & longitude");
        System.out.println("2, postal code");
        System.out.println("3, price range");
        System.out.println("4, date range");
        System.out.println("5, address city country");
        System.out.println("6, type");
        System.out.println("Choose the filters you want to use ~");
        System.out.print("(Separate By Space): ");
        String[] args = getArguments();
        String[] data = new String[14];
        for (String arg: args) {
            if (arg.equals("1")) {
                System.out.print("latitude: ");
                data[0] = getArgLine();
                System.out.print("longitude: ");
                data[1] = getArgLine();
                System.out.print("radius: ");
                data[2] = getArgLine();
                if (data[2].equals("")) {
                    data[2] = "10";
                }
            }
            if (arg.equals("2")) {
                System.out.print("Postal Code: ");
                data[3] = getArgLine();
            }
            if (arg.equals("3")) {
                System.out.print("Low: ");
                data[4] = getArgLine();
                System.out.print("High: ");
                data[5] = getArgLine();
            }
            if (arg.equals("4")) {
                System.out.print("Start: ");
                data[6] = getArgLine();
                System.out.print("End: ");
                data[7] = getArgLine();
            }
            if (arg.equals("5")) {
                System.out.print("Address: ");
                data[8] = getArgLine();
                System.out.print("City: ");
                data[9] = getArgLine();
                System.out.print("Country: ");
                data[10] = getArgLine();
            }
            if (arg.equals("6")) {
                System.out.print("Type: ");
                data[11] = getArgLine();
            }
        }
        System.out.println("Rank By: 1, location  2, price");
        System.out.print("1, Ascending  2, Descending: ");
        String[] acd = getArguments();
        if (acd.length == 2) {
            data[12] = acd[0];
            data[13] = acd[1];
        }
        System.out.println("Results:");
        ArrayList<String> listings = op.getFilteredListing(data);
        for (String listing : listings) {
            System.out.println(listing);
        }
        System.out.print("Book (yes/no) ?: ");
        String input = getArgLine();
        if (input.equals("yes")) {
            System.out.println("lid: ");
            String lidStr = getArgLine();
            int lid;
            try {
                lid = Integer.parseInt(lidStr);
            } catch (Exception e) {
                System.out.println("Wrong input");
                return;
            }
            System.out.print("Start Date: ");
            String start = getArgLine();
            System.out.print("End Date: ");
            String end = getArgLine();
            op.book(this.uid, lid, start, end);
        }
        this.state = 5;
    }

    public void managebooking() {
        System.out.println("My Bookings");
        ArrayList<String> bookings = op.getAllBookings(this.uid, false);
        for (String booking : bookings) {
            System.out.println(booking);
        }
        System.out.print("Choose a Booking to cancel or 'back': ");
        String input = getArgLine();
        if (input.equals("back") || input.equals("")) {
            this.state = 11;
            return;
        }
        int bid = Integer.parseInt(input);
        op.cancelBook(bid);
    }

    public void rentercmt() {
        System.out.println("Choose a Booking to Comment");
        ArrayList<String> bookings = op.getAllBookings(this.uid, true);
        for (String booking : bookings) {
            System.out.println(booking);
        }
        System.out.print("Choose a Booking to comment or 'back': ");
        String input = getArgLine();
        if (input.equals("back") || input.equals("")) {
            this.state = 11;
            return;
        }
        int bid = Integer.parseInt(input);
        System.out.println("1, Comment to Listing");
        System.out.println("2, Comment to Host");
        System.out.print("Your Choice: ");
        input = getArgLine();
        System.out.print("Rate [1 ~ 5]: ");
        String rate = getArgLine();
        System.out.print("Comment: ");
        String cmt = getArgLine();
        if (input.equals("1")) op.commentListing(this.uid, bid, cmt, rate);
        if (input.equals("2")) op.commentHost(this.uid, bid, cmt, rate);
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
        if (input.equals("4")) this.state = 10;
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
        System.out.println("Most popular amenity: " + op.getPoPAmenity());
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
        if (lidStr.equals("back") || lidStr.equals("")) {
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
            System.out.println("Suggested Price: " + op.getSuggestedPrice(lid));
            System.out.print("Price: ");
            String price = getArgLine();
            if (date.equals("")) return;
            if (price.equals("")) return;
            op.changeCalendarPrice(lid, Integer.parseInt(price), date);
        }
        if (input.equals("2")) {
            System.out.println("------------");
            System.out.println("Insert new date to Calendar");
            System.out.print("Date: ");
            String date = getArgLine();
            System.out.println("Suggested Price: " + op.getSuggestedPrice(lid));
            System.out.print("Price: ");
            String price = getArgLine();
            System.out.print("Available: ");
            String ava = getArgLine();
            if (date.equals("")) return;
            if (price.equals("")) return;
            if (ava.equals("")) return;
            op.insertNewDate(lid, date, Integer.parseInt(price), ava);
        }
        if (input.equals("3")) {
            System.out.println("------------");
            System.out.println("Edit Available");
            System.out.print("Date: ");
            String date = getArgLine();
            System.out.print("Available: ");
            String ava = getArgLine();
            if (date.equals("")) return;
            if (ava.equals("")) return;
            op.changeCalendarAvailable(lid, ava, date);
        }
        if (input.equals("4")) {
            this.state = 11;
        }
    }

    public void managehostbooking() {
        System.out.println("Host Existing Bookings");
        ArrayList<String> bookings = op.getAllHostBookings(this.uid, false);
        for (String booking : bookings) {
            System.out.println(booking);
        }
        System.out.print("Choose a Booking to cancel or 'back': ");
        String input = getArgLine();
        if (input.equals("back") || input.equals("")) {
            this.state = 11;
            return;
        }
        int bid = Integer.parseInt(input);
        op.cancelBook(bid);
    }

    public void hostcmt() {
        System.out.println("Choose a Booking to Comment");
        ArrayList<String> bookings = op.getAllHostBookings(this.uid, false);
        for (String booking : bookings) {
            System.out.println(booking);
        }
        System.out.print("Choose a Booking to comment or 'back': ");
        String input = getArgLine();
        if (input.equals("back") || input.equals("")) {
            this.state = 11;
            return;
        }
        int bid = Integer.parseInt(input);
        System.out.print("Rate [1 ~ 5]: ");
        String rate = getArgLine();
        System.out.print("Comment: ");
        String cmt = getArgLine();
        op.commentRenter(this.uid, bid, cmt, rate);
    }
    public void managereport() {
        System.out.println("Report");
        System.out.println("0, go back to home page");
        System.out.println("1, total number of bookings in a " +
                "specific date range by city");
        System.out.println("2, total number of bookings in a " +
                "specific date range by city and zip code");
        System.out.println("3, total number of listings in a " +
                "country");
        System.out.println("4, total number of listings in a " +
                "city");
        System.out.println("5, total number of listings in a " +
                "postal code");
        System.out.println("6, rank the hosts by the total number of listings they have " +
                "per country");
        System.out.println("7, rank the hosts by the total number of listings they have " +
                "per city");
        System.out.println("8, hosts that have " +
                "number of listings that is more than 10% of the number of listings in a " +
                "country");
        System.out.println("9, hosts that have " +
                "number of listings that is more than 10% of the number of listings in a " +
                "city");
        System.out.println("10, rank the renters by the number of bookings in a " +
                "specific time period");
        System.out.println("11, rank the renters by the number of bookings in a " +
                "specific time period per city");
        System.out.println("12, hosts with the largest number of " +
                "cancellations within a year");
        System.out.println("13, renters with the largest number of " +
                "cancellations within a year");
        System.out.println("14, popular noun phrases associated with the listing");
        System.out.print("Your choice: ");
        String[] arg = getArguments();
        if (arg[0].equals("1")) this.state = 16;
        else if (arg[0].equals("0")) this.state = 0;
        else if (arg[0].equals("2")) this.state = 17;
        else if (arg[0].equals("3")) this.state = 18;
        else if (arg[0].equals("4")) this.state = 19;
        else if (arg[0].equals("5")) this.state = 20;
        else if (arg[0].equals("6")) this.state = 21;
        else if (arg[0].equals("7")) this.state = 22;
        else if (arg[0].equals("8")) this.state = 23;
        else if (arg[0].equals("9")) this.state = 24;
        else if (arg[0].equals("10")) this.state = 25;
        else if (arg[0].equals("11")) this.state = 26;
        else if (arg[0].equals("12")) this.state = 27;
        else if (arg[0].equals("13")) this.state = 28;
        else if (arg[0].equals("14")) this.state = 29;

        else System.out.println("Incorrect input!");
    }
    public void manageGetBookingsNumberCityPeriod(){
        System.out.println("1, total number of bookings in a " +
                "specific date range by city");
        System.out.println("Please type in start date");
        String start = getArgLine();
        System.out.println("Please type in end date");
        String end = getArgLine();
        System.out.println("Please type in city");
        String city = getArgLine();
        System.out.println("Result is:");
        try {
            dao.getBookingsNumberCityPeriod(start, end, city);
        }catch (Exception e) {
            e.printStackTrace();
        }
        getArgLine();
        this.state=15;
    }
    public void manageGetBookingsNumberCityPostal(){
        System.out.println("2, total number of bookings in a " +
                "specific date range by city and zip code");
        System.out.println("Please type in start date");
        String start = getArgLine();
        System.out.println("Please type in end date");
        String end = getArgLine();
        System.out.println("Please type in city");
        String city = getArgLine();
        System.out.println("Please type in postal code");
        String postal = getArgLine();
        try {
            System.out.println("Result is:");
            dao.getBookingsNumberCityPostal(start, end, city, postal);
        }catch (Exception e) {
            e.printStackTrace();
        }
        getArgLine();

        this.state=15;
    }
    public void manageGetListingsNumberCountry(){
        System.out.println("3, total number of listings in a " +
                "country");
        System.out.println("Please type in Country");
        String country = getArgLine();
        try {
            System.out.println("Result is:");
            dao.getListingsNumberCountry(country);
        }catch (Exception e) {
            e.printStackTrace();
        }
        getArgLine();

        this.state=15;
    }
    public void manageGetListingsNumberCity(){
        System.out.println("4, total number of listings in a " +
                "city");
        System.out.println("Please type in Country");
        String country = getArgLine();
        System.out.println("Please type in City");
        String city = getArgLine();
        try {
            System.out.println("Result is:");
            dao.getListingsNumberCity(country, city);
        }catch (Exception e) {
            e.printStackTrace();
        }
        getArgLine();

        this.state=15;
    }
    public void manageGetListingsNumberPostal(){
        System.out.println("5, total number of listings in a " +
                "Postal");
        System.out.println("Please type in Country");
        String country = getArgLine();
        System.out.println("Please type in City");
        String city = getArgLine();
        System.out.println("Please type in Postal");
        String postal = getArgLine();
        try {
            System.out.println("Result is:");
            dao.getListingsNumberPostal(country, city, postal);
        }catch (Exception e) {
            e.printStackTrace();
        }
        getArgLine();

        this.state=15;
    }
    public void manageListingsRankByCountry(){
        System.out.println("6, rank the hosts by the total number of listings they have per country");
        System.out.println("Please type in Country");
        String country = getArgLine();
        try {
            System.out.println("Result is:");
            dao.listingsRankByCountry(country);
        }catch (Exception e) {
            e.printStackTrace();
        }
        getArgLine();

        this.state=15;
    }
    public void manageListingsRankByCity(){
        System.out.println("7, rank the hosts by the total number of listings they have per city");
        System.out.println("Please type in Country");
        String country = getArgLine();
        System.out.println("Please type in City");
        String city = getArgLine();
        try {
            System.out.println("Result is:");
            dao.listingsRankByCity(country, city);
        }catch (Exception e) {
            e.printStackTrace();
        }
        getArgLine();

        this.state=15;
    }
    public void manageListingsOwnerMoreThanTenPersentByCountry(){
        System.out.println("8, hosts that have number of listings that is more than 10% of the number of listings in a country");
        System.out.println("Please type in Country");
        String country = getArgLine();
        try {
            System.out.println("Result is:");
            dao.listingsOwnerMoreThanTenPersentByCountry(country);
        }catch (Exception e) {
            e.printStackTrace();
        }
        getArgLine();

        this.state=15;
    }
    public void manageListingsOwnerMoreThanTenPersentByCity(){
        System.out.println("9, hosts that have number of listings that is more than 10% of the number of listings in a city");
        System.out.println("Please type in Country");
        String country = getArgLine();
        System.out.println("Please type in City");
        String city = getArgLine();
        try {
            System.out.println("Result is:");
            dao.listingsOwnerMoreThanTenPersentByCity(country, city);
        }catch (Exception e) {
            e.printStackTrace();
        }
        getArgLine();

        this.state=15;
    }
    public void manageRentersRankByPeriod(){
        System.out.println("10, rank the renters by the number of bookings in a specific time period");
        System.out.println("Please type in start date");
        String start = getArgLine();
        System.out.println("Please type in end date");
        String end = getArgLine();
        try {
            System.out.println("Result is:");
            dao.rentersRankByPeriod(start, end);
        }catch (Exception e) {
            e.printStackTrace();
        }
        getArgLine();

        this.state=15;
    }
    public void manageRentersRankByPeriodAndCity(){
        System.out.println("11, rank the renters by the number of bookings in a specific time period per city");
        System.out.println("Please type in start date");
        String start = getArgLine();
        System.out.println("Please type in end date");
        String end = getArgLine();
        System.out.println("Please type in City");
        String city = getArgLine();
        try {
            System.out.println("Result is:");
            dao.rentersRankByPeriodAndCity(start, end, city);
        }catch (Exception e) {
            e.printStackTrace();
        }
        getArgLine();

        this.state=15;
    }
    public void manageHostsLargestCanceled(){
        System.out.println("12, hosts with the largest number of cancellations within a year");
        try {
            System.out.println("Result is:");
            dao.hostsLargestCanceled();
        }catch (Exception e) {
            e.printStackTrace();
        }
        getArgLine();
        this.state=15;
    }
    public void manageRentersLargestCanceled(){
        System.out.println("13, renters with the largest number of cancellations within a year");

        try {
            System.out.println("Result is:");
            dao.rentersLargestCanceled();
        }catch (Exception e) {
            e.printStackTrace();
        }
        getArgLine();
        this.state=15;
    }
    public void manageCommentRank(){
        System.out.println("14, popular noun phrases associated with the listing");
        System.out.print("Lid: ");
        int lid = Integer.parseInt(getArgLine());
        op.printPoPComment(lid);
        getArgLine();
        this.state=15;
    }
}
