/* Student Class

//    int studentId;
//    String studentName;
//    int age;
//    long mobileNumber;
//    String email;
//    String intrestedField;
//    double grade;
//
//    public Student(int studentId, String studentName, int age, long mobileNumber, String email, String intrestedField , double grade) {
//        this.studentId = studentId;
//        this.studentName = studentName;
//        this.age = age;
//        this.mobileNumber = mobileNumber;
//        this.email = email;
//        this.intrestedField = intrestedField;
//        this.grade = grade;
//    }
//


//    public int getStudentId() {
//        return studentId;
//    }
//
//    public String getStudentName() {
//        return studentName;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public long getMobileNumber() {
//        return mobileNumber;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public double getGrade() {
//        return grade;
//    }
//
//    public String getIntrestedField() {
//        return intrestedField;
//    }
//
//    @Override
//    public String toString() {
//        return "Student{" +
//                "studentId=" + studentId +
//                ", studentName='" + studentName + '\'' +
//                ", age=" + age +
//                ", mobileNumber=" + mobileNumber +
//                ", email='" + email + '\'' +
//                ", intrestedField='" + intrestedField + '\'' +
//                ", grade=" + grade +
//                '}';
//    }

 */

/* Instructor Class

//    int instructorId;
//    String instructorName;
//    long mobileNumber;
//    String email;
//    String Specialization;
//    double rating;
//    double Experience;
//    double Income = 5000;
//
//    public Instructor(int instructorId, String instructorName, long mobileNumber, String email, String specialization, double rating, double experience) {
//        this.instructorId = instructorId;
//        this.instructorName = instructorName;
//        this.mobileNumber = mobileNumber;
//        this.email = email;
//        this.Specialization = specialization;
//        this.rating = rating;
//        this.Experience = experience;
//    }
//

//    public int getInstructorId() {
//        return instructorId;
//    }
//
//    public String getInstructorName() {
//        return instructorName;
//    }
//
//    public long getMobileNumber() {
//        return mobileNumber;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public String getSpecialization() {
//        return Specialization;
//    }
//
//    public double getRating() {
//        return rating;
//    }
//
//    public double getExperience() {
//        return Experience;
//    }
//
//
//    @Override
//    public String toString() {
//        return "Instructor{" +
//                "instructorId=" + instructorId +
//                ", instructorName=" + instructorName +
//                ", mobileNumber=" + mobileNumber +
//                ", email=" + email +
//                ", Specialization=" + Specialization +
//                ", rating=" + rating +
//                ", Experience=" + Experience +
//                '}';
//    }
//
//    public void IncomeSpike(Connection con){
//
//    }
//
//    public void mainMenu(Connection con) throws Exception{
//
//    }
 */

/*
        //        System.out.println(ConsoleColors.GREEN + "======== Course Management System : CourseNet ========");
//        System.out.println(ConsoleColors.CYAN + "1. Enter 1 For Admin.");
//        System.out.println(ConsoleColors.CYAN + "2. Enter 2 For Instructor.");
//        System.out.println(ConsoleColors.CYAN + "3. Enter 3. For Student.");
//        System.out.print(ConsoleColors.BLACK + "Enter Your Choice : ");
//        int choice= sc.nextInt();
//        boolean flag = true;
//        switch (choice){
//            case 1 : // For Admin
//              //  admin.adminDefaultLogin(con);
//                break;
//
//            case 2 : // For Instructor
//                break;
//
//            case 3 : // For Student
//                student.StudentFunctionality(con);
//                break;
//
//            default:
//                System.out.println(ConsoleColors.RED + "Enter Appropriate Choice!");
//        }
//    }
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
    // StudentAdmin Class
//
//    public void adminLogin(Connection con) throws Exception{
//        System.out.println("===== Admin Login Portal =====");
//        System.out.print("Enter Admin Id : ");
//        int id = sc.nextInt();
//        sc.nextLine();
//        System.out.print("Enter Admin Password : ");
//        String pass = sc.next();
//        String sql = "Select adminRole,adminName from Admin WHERE adminId = " + id+ " AND adminPassword = '" + pass + "'";
//        Statement st = con.createStatement();
//        ResultSet rs = st.executeQuery(sql);
//        rs.next();
//        if(rs == null){
//            System.out.println("Wrong Credentials");
//            adminLogin(con);
//        }else{
//            System.out.println("Welcome Mr/Ms " + rs.getString(2));
//            String role = rs.getString(1).toUpperCase();
//            switch (role){
//                case "STUDENT" :
//                        new Student().mainMenu(con);
//                    break;
//
//                case "INSTRUCTOR" :
//                        new Instructor().mainMenu(con);
//                    break;
//
//                case "COURSE" :
//
//                    break;
//            }
//        }
//    }
//
//}
//
//public void mainMenu(Connection con) throws Exception{
//    do{
//        System.out.println("Enter 1 For Remove Student.");
//        System.out.println("Enter 2 For View ALL Student.");
//        System.out.println("Enter 3 For View Particular Student.");
//        System.out.println("Enter 4 For View Overall Grade");
//        System.out.println("Enter 5 For View Top 5 Student.");
//        System.out.println("Enter 6 For Exit The System.");
//        System.out.print("Enter Your Choice : ");
//        int choice = sc.nextInt();
//
//        switch (choice){
//            case 1 :
//                delStudent(con);
//                break;
//            case 2 :
//                viewAllStudent(con);
//                break;
//
//            case 3 :
//                viewPartucularStudent(con);
//                break;
//
//            case 4 :
//                viewOverallGrade(con);
//                break;
//
//            case 5 :
//                view(con);
//                break;
//
//            case 6 :
//                System.out.println("Thank You Coming!");
//                System.out.println("Exiting The System.....");
//                return;
//
//            default:
//                System.out.println("Please Enter Appropriate Input!");
//
//        }
//    }while(true);
//}
//
//public void delStudent(Connection con) throws Exception{
//    System.out.print("Enter Name of That Student : ");
//    String name = sc.next();
//    String sql = "Select * from Student WHERE StudentName LIKE %" + name + "%";
//    Statement st = con.createStatement();
//    ResultSet rs = st.executeQuery(sql);
//    while (rs.next()){
//        System.out.println(new Student(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getLong(4),rs.getString(5),rs.getString(6),rs.getDouble(7)).toString());
//    }
//    System.out.println("Enter Particular Student ID : ");
//    int id = sc.nextInt();
//    String sql1 = "Delete FROM Student WHERE Studentid = " + id;
//    int r = st.executeUpdate(sql1);
//    System.out.println(r > 0 ? "Record Deleted!" : "Deletion Failed!");
//}
//
//public void viewAllStudent(Connection con) throws Exception{
//    String sql = "SELECT * FROM Student";
//    Statement st = con.createStatement();
//    ResultSet rs = st.executeQuery(sql);
//    while (rs.next()){
//        System.out.println(new Student(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getLong(4),rs.getString(5),rs.getString(6),rs.getDouble(7)).toString());
//    }
//}
//
//public void viewPartucularStudent(Connection con) throws Exception{
//    System.out.print("Enter Name of That Student : ");
//    String name = sc.next();
//    String sql = "Select * from Student WHERE StudentName LIKE %" + name + "%";
//    Statement st = con.createStatement();
//    ResultSet rs = st.executeQuery(sql);
//    while (rs.next()){
//        System.out.println(new Student(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getLong(4),rs.getString(5),rs.getString(6),rs.getDouble(7)).toString());
//    }
//    System.out.println("Enter Particular Student ID : ");
//    int id = sc.nextInt();
//    String sql1 = "Select *  FROM Student WHERE Studentid = " + id;
//    ResultSet rs1 = st.executeQuery(sql1);
//    if (rs1.next()){
//        System.out.println(new Student(rs1.getInt(1),rs1.getString(2),rs1.getInt(3),rs1.getLong(4),rs1.getString(5),rs1.getString(6),rs.getDouble(7)).toString());
//    }
//}
//
//public void viewOverallGrade(Connection con) throws Exception{
//    String sql = "Select courseName From Course";
//    PreparedStatement pst = con.prepareStatement(sql);
//    ResultSet rs = pst.executeQuery();
//    while (rs.next()){
//        System.out.println("Course : " + rs.getString(1));
//    }
//    String sql1 = "Select AVG(grade) FROM Student WHERE courseName = ?";
//    System.out.print("Enter Course Name : ");
//    String course = sc.nextLine();
//    pst = con.prepareStatement(sql1);
//    pst.setString(1,course);
//    ResultSet rs1 = pst.executeQuery();
//    if (rs.next()){
//        System.out.println("Overall Grade  : " + rs.getDouble(1));
//    }
//}
//
//public void view(Connection con) throws Exception{
//    String sql = "Select * FROM Student ORDER BY grade DESC limit 5";
//    PreparedStatement pst = con.prepareStatement(sql);
//    ResultSet rs1 = pst.executeQuery();
//    while (rs1.next()){
//        System.out.println(new Student(rs1.getInt(1),rs1.getString(2),rs1.getInt(3),rs1.getLong(4),rs1.getString(5),rs1.getString(6),rs1.getDouble(7)).toString());
//    }
//
 */
//class Mai{
//    public static void main(String[] args) {
//        String jdbcUrl = "jdbc:mysql://localhost:3306/db-3";
//        String username = "root";
//        String password = "";
//        for(int i=1;i<=50;i++){
//            for(int j=1;j<=15;j++){
//                String filePath = "E:\\Study_Notes\\Unit 3_Try Catch.pdf"; // Could be "lecture1.jpg" or "lecture1.mp4"
//                int courseId = i;      // Set as needed
//                int lectureNo =j ;     // Set as needed
//                try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
//                    String sql = "INSERT INTO coursecontent (Course_Id, Lecture_No, Lecture_Content) VALUES (?, ?, ?)";
//                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//                        pstmt.setInt(1, courseId);
//                        pstmt.setInt(2, lectureNo);
//
//                        try (FileInputStream fis = new FileInputStream(filePath)) {
//                            pstmt.setBinaryStream(3, fis, fis.available());
//                            int rowsInserted = pstmt.executeUpdate();
//                            System.out.println("Rows inserted: " + rowsInserted);
//                        } catch (IOException e) {
//                            System.out.println("File IO error: " + e.getMessage());
//                        }
//                    }
//                } catch (SQLException e) {
//                    System.out.println("SQL error: " + e.getMessage());
//                }
//            }
//
//        }
//    }
//}
/*public abstract class DatabaseOperation<T> extends DBConnection {
    public abstract void insert(T obj) throws Exception;
    public abstract void update(T obj) throws Exception;
    public abstract void delete(int id) throws Exception;

    // Polymorphic method — each DAO can render/return a different “view”
    public abstract void view() throws Exception;
}*/
//void notification(Connection con,Scanner sc)throws Exception{
//        String notify="select n.event_id,n.notification_id,n.message,n.sent_date from notifications n" +
//                " inner join registrations  r on r.event_id=n.event_id inner join users u on u.user_id=r.user_id where name=? ";
//        System.out.println("Enter user name ");
//        sc.nextLine();
//        String username = sc.nextLine();
//        PreparedStatement pst = con.prepareStatement(notify);
//        pst.setString(1, username);
//        ResultSet rs=pst.executeQuery();
//        boolean found=false;
//        while (rs.next()) {
//            System.out.println("----------------------------------------------------");
//            found=true;
//            System.out.println("notification_id:  "+rs.getInt("notification_id"));
//            System.out.println("event id : "+rs.getInt("event_id"));
//            System.out.println("message : "+rs.getString("message"));
//            System.out.println("sent date : "+rs.getString("sent_date"));
//            System.out.println("----------------------------------------------------");
//        }
//        if(!found){
//            System.out.println("user name  not found");
//
//        }
//
//
//}
//        void addUserNotification(String username, int eventid)throws Exception {
//            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventdb", "root", "")) {
//                String notification = "select user_id from users where name=?";
//                PreparedStatement pst = con.prepareStatement(notification);
//                pst.setString(1,username);
//                ResultSet rs = pst.executeQuery();
//                if (rs.next()) {
//                    String msg = "Hello " + username + ", your ticket for event ID " + eventid + " has been confirmed!";
//                    String insert = "insert into notifications(event_id, message, sent_date) VALUES(?, ?,current_timestamp)";
//                    PreparedStatement pst2 = con.prepareStatement(insert);
//                    pst2.setInt(1, eventid);
//                    pst2.setString(2, msg);
//                    pst2.executeUpdate();
//                    a.ToFileNotification();
//                } else {
//                    System.out.println("Username not found, Notification not created.");
//                }
//            }
//        }
//}
import java.util.Vector;

//class NotificationManager {
//    private Vector<String> notifications = new Vector<>();
//    int OTP;
//    public void addNotification(String message) {
//        notifications.add(message);
//    }
//    private void viewOtpNotification(int otp){
//        System.out.println("Your OneTimePassword(OTP) Is :"+otp);
//    }
//    public void viewNotifications() {
//        for (String n : notifications) {
//            System.out.println("🔔 " + n);
//        }
//    }
//}
/*
Structure of Dummy Payment Portal

        Payment Class (Encapsulation)

        Fields: amount, paymentMethod, status.

        Methods: processPayment(), getStatus().

        Payment Methods (Polymorphism)

        CreditCardPayment

                UPIPayment

        WalletPayment

        All implement a common interface PaymentMethod.

        Payment Records (Data Structure)

        Use HashMap → store studentId → payment status.

        Example: HashMap<Integer, Boolean> paymentHistory.

        🔹 Example Java Code (Simple & Expandable)
import java.util.*;

        // Abstraction
        interface PaymentMethod {
            boolean pay(double amount);
        }

        // Concrete Implementations (Polymorphism)
        class CreditCardPayment implements PaymentMethod {
            public boolean pay(double amount) {
                System.out.println("Paid ₹" + amount + " using Credit Card.");
                return true; // Assume always successful
            }
        }

        class UPIPayment implements PaymentMethod {
            public boolean pay(double amount) {
                System.out.println("Paid ₹" + amount + " using UPI.");
                return true;
            }
        }

        class WalletPayment implements PaymentMethod {
            public boolean pay(double amount) {
                System.out.println("Paid ₹" + amount + " using Wallet.");
                return true;
            }
        }

        // Payment Portal (Encapsulation + DS usage)
        class PaymentPortal {
            private HashMap<Integer, Boolean> paymentHistory = new HashMap<>();

            public boolean makePayment(int studentId, double amount, PaymentMethod method) {
                boolean status = method.pay(amount);
                paymentHistory.put(studentId, status);
                return status;
            }

            public boolean hasPaid(int studentId) {
                return paymentHistory.getOrDefault(studentId, false);
            }
        }

        // Example usage in CMS
        public class CourseManagementSystem {
            public static void main(String[] args) {
                Scanner sc = new Scanner(System.in);
                PaymentPortal portal = new PaymentPortal();

                int studentId = 101;
                double courseFee = 5000.0;

                System.out.println("Choose Payment Method: 1. CreditCard  2. UPI  3. Wallet");
                int choice = sc.nextInt();
                PaymentMethod method;

                switch (choice) {
                    case 1: method = new CreditCardPayment(); break;
                    case 2: method = new UPIPayment(); break;
                    case 3: method = new WalletPayment(); break;
                    default: System.out.println("Invalid choice"); return;
                }

                if (portal.makePayment(studentId, courseFee, method)) {
                    System.out.println("Payment Successful ✅ Student can now enroll in course.");
                } else {
                    System.out.println("Payment Failed ❌ Please try again.");
                }
            }C:\Users\tanis\Downloads\T4_PB_JAVA-II_SEM_II_CE RELATED BRANCHES_EVEN_2025_V1.2.pdf
        }

🔹 How It Fits in Your System

        When a student clicks Enroll in Course → system calls portal.makePayment(studentId, courseFee, method).

        If payment is successful → student is enrolled in DB (INSERT INTO enrollment table).

        If not → block enrollment until payment done.

✅ This way, you’re not using real money transactions, but you’re clearly showing Java OOP + DS concepts in your project.*/