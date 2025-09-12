import java.awt.desktop.UserSessionEvent;
import java.sql.*;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.*;

class ConsoleColors {
    // Reset
    public static final String RESET = "\u001B[0m";

    // Regular Colors
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Bold
    public static final String RED_BOLD = "\033[1;31m";
    public static final String GREEN_BOLD = "\033[1;32m";
    public static final String BLUE_BOLD = "\033[1;34m";

    // Background
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BG_BLUE = "\u001B[44m";
}

class Main {
    Scanner sc = new Scanner(System.in);
    static int choiceForRole;
    Hashtable<String,String> hashTable = new Hashtable<>();

    private void setHashTable(Connection con) throws Exception{
        String sql = "SELECT email,password FROM users";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()){
            hashTable.put(rs.getString(1).trim(),rs.getString(2).trim());
        }
    }
    Main(Connection con){
        try
        {
            setHashTable(con);
        }catch (Exception e){

        }
    }
    private boolean emailFound(Connection con , String email,String password) throws Exception{
       //Only Email Verification
        if(password==null){
            for(String m : hashTable.keySet()){
                if(email.trim().equalsIgnoreCase(m)){
                    return true;
                }
            }
            return false;
        }
        //For Chechking If Email And Password Match Each Other
        for(Map.Entry m :hashTable.entrySet()){
            if(email.trim().equalsIgnoreCase(m.getKey().toString()) && password.trim().equalsIgnoreCase(m.getValue().toString())){
                return true;
            }
        }
        return false;
    }

    public void Login(Connection con) throws Exception {
        NotificationManager o=new NotificationManager();
        String email;
        String pass;
        String newPass;
        int user_id;
        String mobile;
        while (true) {//WhileLoop-1 Starts Here
            System.out.println(ConsoleColors.YELLOW + "=======================================================");
            System.out.println();
            System.out.println(ConsoleColors.YELLOW + "\t \t Course Management System : CourseNet \t \t");
            System.out.println();
            System.out.println(ConsoleColors.YELLOW + "=======================================================");
            System.out.print(ConsoleColors.BLUE + "Enter Your Email ID : ");
            email = sc.next().toLowerCase();
            sc.nextLine();
            if (email.endsWith("@gmail.com")) {
                if (emailFound(con,email,null)) {
                    System.out.print(ConsoleColors.BLUE + "Enter Your Password : ");
                    pass = sc.next();
                    if (emailFound(con,email,pass)) {
                        /* Role-Based Menu Will be Provided*/
                        String query1 = "SELECT role,user_Id FROM users WHERE email = ? AND password = ?";
                        PreparedStatement pst1 = con.prepareStatement(query1);
                        pst1.setString(1,email);
                        pst1.setString(2,pass);
                        ResultSet rs1 = pst1.executeQuery();
                        rs1.next();
                        String role = rs1.getString(1);
                        int id = rs1.getInt(2);
                        System.out.println(ConsoleColors.BLUE + "User ID : " + id);
                        if(role.equalsIgnoreCase("Student")){
                            // Student Menu will be Called
                            new Student(id).StudentFunctionality(con,sc);
                            return;
                        }else if(role.equalsIgnoreCase("Instructor")){
                            // Instructor Menu will be Called
                            new Instructor(id).InstructorFunctionality(con,sc);
                            return;
                        }else if(role.equalsIgnoreCase("StudentAdmin")){
                            // StudentAdmin Menu will be Called
                            new StudentAdmin().StudentAdminFunctionality(con,sc);
                            return;
                        }else if(role.equalsIgnoreCase("InstructorAdmin")){
                            // InstructorAdmin Menu will be Called
                            new InstructorAdmin().InstructorAdminFunctionality(con,sc);
                            return;
                        }else{
                            // CourseAdmin Menu will be Called
                            new CourseAdmin().CourseAdminMainMenu(con,sc);
                            return;
                        }
//                        break;
                    } else {
                        System.out.println(ConsoleColors.RED + "Wrong Credentials!");
                        int choice;
                        do {
                            System.out.println(ConsoleColors.WHITE + "Enter 1 For Re-Enter Password.");
                            System.out.println(ConsoleColors.WHITE + "Enter 2 For Forgot Password.");
                            System.out.println(ConsoleColors.WHITE + "Enter 3 For Exit The System.");
                            System.out.print(ConsoleColors.BLUE + "Enter Your Choice : ");
                           try{
                               choice = sc.nextInt();sc.nextLine();
                           } catch (Exception e) {
                               sc.nextLine();continue;
                           }
                            if (choice < 0 || choice > 3) {
                                System.out.println(ConsoleColors.RED + "Enter Appropriate Choice.");
                            } else if (choice == 1) {
                                Login(con);return;
                            } else if (choice == 2) {
                                    System.out.print(ConsoleColors.BLUE + "Enter Your Mobile Number : ");
                                    mobile = sc.next();
                                    String query4 = "SELECT mobileNo FROM users WHERE mobileNo = ? And email=? ";
                                    PreparedStatement pst3 = con.prepareStatement(query4);
                                    pst3.setString(1, mobile);
                                    pst3.setString(2, email);
                                    ResultSet rs3 = pst3.executeQuery();
                                    if (rs3.next() && o.stimulateOTP(mobile,sc)) {
                                        System.out.print(ConsoleColors.BLUE + "Enter New Password : ");
                                        newPass = sc.next();
                                        String query5 = "UPDATE users SET password = ? WHERE mobileNo = ?";
                                        PreparedStatement pst4 = con.prepareStatement(query5);
                                        pst4.setString(1, newPass);
                                        pst4.setString(2, mobile);
                                        if(pst4.execute()==false){
                                            System.out.println(ConsoleColors.BLUE + "Password Updation Was Successfull");
                                            hashTable.clear();
                                            setHashTable(con);
                                        }else{
                                            System.out.println(ConsoleColors.RED + "Password Updation Was Unsuccessfull");
                                        }
                                        Login(con);return;
                                    }

                            } else {
                                System.exit(0);
                            }
                        } while (true);
                    }
                } else {
                    System.out.println(ConsoleColors.RED + "Wrong Email!");
                }
            } else {
                System.out.println(ConsoleColors.RED + "Entered Invalid Input! Enter Valid Input");
            }//If-else Block Ends Here
        }//WhileLoop-1 Ends Here
    }

    public void signUp(Connection con) throws Exception {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.println(ConsoleColors.GREEN_BOLD + "=======================================================");
        System.out.println();
        System.out.println(ConsoleColors.GREEN + "\t \t Welcome To SignUp Page \t \t");
        System.out.println();
        System.out.println(ConsoleColors.GREEN_BOLD + "=======================================================");
        System.out.print(ConsoleColors.BLUE + "Enter First Name:");
        String firstName = sc.nextLine();
        System.out.print(ConsoleColors.BLUE + "Enter Last Name:");
        String lastName = sc.nextLine();
        String mobileNo;
        do {
            boolean isValid=true;
            System.out.print(ConsoleColors.BLUE + "Enter Mobile No:");
            mobileNo = sc.next();
            sc.nextLine();
            if (mobileNo.length() == 10) {
                for (char c : mobileNo.toCharArray()) {
                    if (!Character.isDigit(c)) {
                        isValid = false;
                        break;
                    }
                }
                if(isValid){
                    break;
                }else{
                    System.out.println(ConsoleColors.RED + "Character Don't Allowed! Enter Digits Only");
                }
            } else {
                System.out.println(ConsoleColors.RED + "Enter 10 Digit Phone Number Only");
            }
        } while (true);
        String email;
        do {
            System.out.print(ConsoleColors.BLUE + "Enter Email:");
            email = sc.next().toLowerCase();
            if (email.endsWith("@gmail.com")) {
                if(emailFound(con,email,null))
                {
                    System.out.println(ConsoleColors.RED + "This Email is Already Registered!");
                    continue;
                }else{
                    break;
                }
            }
        } while (true);
        String passWord;
        do {
            System.out.print(ConsoleColors.BLUE + "Enter Password:");
            passWord = sc.next();sc.nextLine();
            System.out.print(ConsoleColors.BLUE + "Confirm Password:");
            String confirmPassword = sc.next();sc.nextLine();
            if (passWord.equals(confirmPassword)) {
                break;
            }
        } while (true);
        String role;
        int choice;
        do {
            System.out.print(ConsoleColors.PURPLE + "Enter Choice For Role:-\n 1.For Student.\n2.For Instructor.\n");
            System.out.print(ConsoleColors.BLUE + "Choice:");
            try {
                 choice = sc.nextInt();sc.nextLine();
            }catch(InputMismatchException e){
                sc.nextLine();
                continue;
            }
            if (choice > 0 && choice < 3) {
                if (choice == 1) {
                    role = "Student";
                } else {
                    role = "Instructor";
                }
                break;
            }
        } while (true);

        String query6 = "Insert Into users(first_name,last_name,mobileNo,email,password,role)" +
                "values (?,?,?,?,?,?)";
        PreparedStatement pst5 = con.prepareStatement(query6);
        pst5.setString(1, firstName);
        pst5.setString(2, lastName);
        pst5.setString(3, mobileNo);
        pst5.setString(4, email.toLowerCase());
        pst5.setString(5, passWord);
        pst5.setString(6, role);
        int r = pst5.executeUpdate();
        if (r > 0) {
            System.out.println(ConsoleColors.RED + "Your Profile:-");
            query6 = "Select user_id,first_name,last_name,mobileNo,email,password,role From users WHERE email=?";
            pst5 = con.prepareStatement(query6);
            pst5.setString(1, email);
            ResultSet rs4 = pst5.executeQuery();
            if (rs4.next()) {
                System.out.println(ConsoleColors.WHITE + "UserId:" + rs4.getInt(1));
                System.out.println(ConsoleColors.WHITE +"FirstName:" + rs4.getString(2));
                System.out.println(ConsoleColors.WHITE +"LastName:" + rs4.getString(3));
                System.out.println(ConsoleColors.WHITE +"MobileNo:" + rs4.getString(4));
                System.out.println(ConsoleColors.WHITE +"Email:" + rs4.getString(5));
                System.out.println(ConsoleColors.WHITE +"Password:" + rs4.getString(6));
                System.out.println(ConsoleColors.WHITE +"Role:" + rs4.getString(7));
            }

            System.out.println(ConsoleColors.GREEN + "Note:Dont Forget Your User Id");
            setHashTable(con);
            Login(con);

        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        /* JDBC Connection Code */
        String dburl = "jdbc:mysql://localhost:3306/coursemanagementsystem";
        String dbuser = "root";
        String dbpass = "";

        /* 1 Load Driver */
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);

        /* 2. Connection Object */
        Connection con = DriverManager.getConnection(dburl, dbuser, dbpass);
        if (con != null) {
            System.out.println(ConsoleColors.RED + "Connection Successful!");
        } else {
            System.out.println(ConsoleColors.RED + "Connection Failed!");
        }

        String email, name;

        users u=new users();
        /* Login Code */
        do{
            System.out.println(ConsoleColors.BLUE_BOLD + "=======================================================");
            System.out.println();
            System.out.println(ConsoleColors.BLUE_BOLD + "\t \t Course Management System : CourseNet \t \t");
            System.out.println();
            System.out.println(ConsoleColors.BLUE_BOLD + "=======================================================");
            u.dashboard();
            System.out.print(ConsoleColors.CYAN + "Do You Have Any Existing Account? (Yes/No/Exit) : ");
            String accountExistOrNot = sc.next();sc.nextLine();
            if (accountExistOrNot.equalsIgnoreCase("Yes")) {
                //Login For User-User Already Has A Account
                new Main(con).Login(con);
            } else if(accountExistOrNot.equalsIgnoreCase("No")) {
                new Main(con).signUp(con);
            }else if(accountExistOrNot.equalsIgnoreCase("Exit")){
                System.out.println(ConsoleColors.YELLOW + "Thank You For Coming!");
                System.out.println(ConsoleColors.CYAN + "Exiting The System...");
                System.exit(0);
            }
            else{
                try{
                    throw new courseException(ConsoleColors.RED + "Enter Yes Or No Only");
                } catch (courseException e) {
                    System.out.println(e.toString());
             }
            }

        }while(true);

    }
}
class courseException extends RuntimeException{
    courseException(String Message){
        super(Message);
    }
}
class NotificationManager {
    private Vector<String> notifications = new Vector<>();
    int OTP;
    public void addNotification(String message) {
        notifications.add(message);
    }
    public int generateOTP(){
        OTP=(int)(10000* Math.random());
        System.out.println(ConsoleColors.PURPLE + "OTP GENERATED");
        addNotification(ConsoleColors.PURPLE + "Your OneTimePassword(OTP) Is :"+OTP);
        System.out.println(ConsoleColors.PURPLE + "Your OneTimePassword(OTP) Is :"+OTP);
        //viewNotifications();
        return OTP;
    }
    public boolean stimulateOTP(String mobileNo,Scanner sc){
        int temp=generateOTP();
        System.out.print(ConsoleColors.BLUE + "Enter OTP Sent To "+mobileNo+" :");
        int otp=sc.nextInt();sc.nextLine();
        if(otp==temp){
            return true;
        }else{
            return false;
        }
    }
    public void viewNotifications() {
        for (String n : notifications) {
            System.out.println("🔔 " + n);
        }
    }
}



/* Description */
/* Total Class Defined :
    1. Class Course
    2. Class Student
    3. Class Instructor
    4. Class Admin
    5. Class Main
 */