import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.util.*;

public class Student extends users implements DatabaseOperation  {

    int userId;

    ArrayList<Course> allCourses =new ArrayList<>();
    public Student(int userId) {
        this.userId = userId;
    }

    /*
        Student Functionality
        1. Course Enrollment
        2. View Courses
        3. View Owned Courses
        4. Update Profile
        5. View Profile
        6. Disenroll Course
        7. Logout
        8. Delete Account
     */

    public void insert(Connection con,int courseId)throws SQLException{
        HashMap<Integer,Course> hm1=new HashMap<>();
        for(Course c:allCourses){
            hm1.put(c.getCourseId(),c);
        }
        if(hm1.containsKey(courseId)){
            if(new CourseDump().paymentMenu(new java.util.Scanner(System.in),userId,hm1.get(courseId).getPrice())){
                String query = "INSERT INTO enrollment(student_Id, course_Id) VALUES (?, ?)";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, userId);
                ps.setInt(2, courseId);
                int r = ps.executeUpdate();
                System.out.println(r > 0 ? ConsoleColors.BLUE +"Enrollment Successful!" : ConsoleColors.RED +"Enrollment UnSuccessful!");
            }else{
                System.out.println(ConsoleColors.RED + "Enrollment Unsuccessfull!");
            }

        }else{
            System.out.println(ConsoleColors.RED + "No Such Course Exists");
        }

    }
    public void courseEnrollment(Connection con,int courseId) throws Exception{
        insert(con,courseId);
    }

    public void viewCourses(Connection con) throws Exception{
        String query = "SELECT * FROM course";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        System.out.println(ConsoleColors.CYAN + "All Courses List:");
        while (rs.next()) {
        System.out.println(ConsoleColors.YELLOW + "Course-Id : "+rs.getInt("course_Id") + ConsoleColors.YELLOW +
                    rs.getString("courseName") + ConsoleColors.YELLOW +  "\nCourse Category : " +
                    rs.getString("category") +"\n\n");
        }
    }
    public void viewTop5ExpensiveCourses(Connection con)throws SQLException{
        PriorityQueue<Course> expensiveCourses=new PriorityQueue<>(Comparator.comparing(Course::getPrice).reversed());
        expensiveCourses.addAll(allCourses);
        for(int i=1;i<=5;i++){
            System.out.println(ConsoleColors.CYAN + "Course-"+i);
            System.out.println(expensiveCourses.poll());
        }
    }
    public void viewEnrolledCourses(Connection con) throws Exception{
        String query =  "SELECT c.course_Id, c.courseName FROM course c " +
                "JOIN enrollment e ON c.course_Id = e.course_Id " +
                "WHERE e.student_Id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(ConsoleColors.BLUE + rs.getInt("course_Id") + " - " + ConsoleColors.BLUE + rs.getString("courseName"));
        }
    }

    @Override
    public void update(Connection con, Scanner sc) throws Exception {
        System.out.print(ConsoleColors.BLUE + "Enter First Name:");
        String firstName=sc.nextLine();
        System.out.print(ConsoleColors.BLUE +"Enter Last Name:");
        String lastName=sc.nextLine();
        String mobileNo;
        do{
            System.out.print(ConsoleColors.BLUE +"Enter Mobile No:");
            mobileNo=sc.next();
            if(mobileNo.length()==10){
                break;
            }else{
                System.out.println(ConsoleColors.RED + "Enter 10 Digit Phone Number Only");
            }
        }while(true);
        String email;
        do{
            System.out.print(ConsoleColors.BLUE + "Enter Email:");
            email=sc.next();
            if(email.endsWith("@gmail.com")){
                String sql="Select email From Users where email=?;";
                PreparedStatement pst=con.prepareStatement(sql);
                pst.setString(1,email);
                ResultSet rs=pst.executeQuery();
                if(rs.next()){
                    System.out.println(ConsoleColors.RED + "Enterred Email Id Is Already Been Registered");
                }else{
                    break;
                }
            }
        }while(true);
        String passWord;
        do{
            System.out.print(ConsoleColors.BLUE +"Enter Your Password:");
            passWord=sc.next();
            System.out.print(ConsoleColors.BLUE +"Confirm Your Password:");
            String confirmPassword=sc.next();sc.nextLine();
            if(passWord.equals(confirmPassword)){
                break;
            }
        }while(true);
        String query = "UPDATE users SET first_name = ? , last_name = ? , mobileNo = ? ,email = ?, password = ? WHERE user_Id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.setString(3, mobileNo);
        ps.setString(4,email);
        ps.setString(5,passWord);
        ps.setInt(6,userId);
        int r = ps.executeUpdate();
        System.out.println(r > 0 ? "Profile updated successfully!" : "Profile Updation UnSuccessful!");
    }

    public void updateProfle(Connection con, Scanner sc) throws Exception{
       update(con,sc);
    }
        //View Profile
    public void view(Connection con) throws Exception{
        String query = "SELECT * FROM users WHERE user_Id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println(ConsoleColors.CYAN + "Your Profile :-");
            System.out.println(ConsoleColors.YELLOW + "User ID: " + rs.getInt("user_Id"));
            System.out.println(ConsoleColors.YELLOW +"First Name : " + rs.getString("first_name"));
            System.out.println(ConsoleColors.YELLOW +"Last Name : " + rs.getString("last_name"));
            System.out.println(ConsoleColors.YELLOW +"Mobile Number : " + rs.getString("mobileNo"));
            System.out.println(ConsoleColors.YELLOW +"Email: " + rs.getString("email"));
            System.out.println(ConsoleColors.YELLOW +"Password : " + rs.getString("password"));
        }
    }


    public void disEnrollCourse(Connection con,int courseId) throws Exception{
        HashMap<Integer,Course> hm1=new HashMap<>();
        for(Course c:allCourses){
            hm1.put(c.getCourseId(),c);
        }
        if(hm1.containsKey(courseId)){
            String query = "DELETE FROM enrollment WHERE student_Id = ? AND course_Id = ? ";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setInt(2, courseId);
                int r = ps.executeUpdate();

            System.out.println(r > 0 ? " Disenrolled from course!" : "Disenrollment UnSuccessful!");
        }else{
            System.out.println(ConsoleColors.RED + "No Such Course Id Exist");
        }

    }

    public void logout(Connection con) throws Exception{
        System.out.println(ConsoleColors.CYAN + " Logged out successfully!");
    }

    @Override
    public void delete(Connection con,int id) throws Exception {
        String query = "DELETE FROM users WHERE user_Id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        int r = ps.executeUpdate();
        System.out.println(r > 0 ? "Account deleted permanently!" : "Account Deletion UnSuccessful!");
    }

    public void delAccount(Connection con) throws Exception{
        delete(con,userId);
    }
    public void searchCourse(Scanner sc){
        HashMap<Integer,Course> hm1=new HashMap<>();
        for(Course c:allCourses){
            hm1.put(c.getCourseId(),c);
        }
        System.out.print(ConsoleColors.BLUE + "Enter Course Id:");
        int searchId=sc.nextInt();
        if(hm1.containsKey(searchId)){
            System.out.println(hm1.get(searchId).toString());
        }else{
            System.out.println(ConsoleColors.RED + "No Such Course Found!!");
        }
    }
    public void StudentFunctionality(Connection con,Scanner sc) throws Exception{

        System.out.println(ConsoleColors.RED_BOLD + "Logged In As Student!");
        String query = "SELECT * FROM course";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            Course o=new Course(rs.getInt(1),rs.getString(2),rs.getString(5),rs.getString(6),rs.getDouble(7));
            allCourses.add(o);
        }
        DatabaseOperation obj=new Student(userId);
        boolean temp=true;
        do{
            System.out.println(ConsoleColors.BLUE + "\n====== STUDENT MENU ======");
            System.out.println(ConsoleColors.WHITE+"Enter 1 For View Courses.");
            System.out.println(ConsoleColors.WHITE+"Enter 2 For Course Enrollment.");
            System.out.println(ConsoleColors.WHITE+"Enter 3 For View Enrolled Courses.");
            System.out.println(ConsoleColors.WHITE+"Enter 4 For Update Profile");
            System.out.println(ConsoleColors.WHITE+"Enter 5 For View Profile.");
            System.out.println(ConsoleColors.WHITE+"Enter 6 For Disenroll Course.");
            System.out.println(ConsoleColors.WHITE+"Enter 7 For Top 5 Expensive Course.");
            System.out.println(ConsoleColors.WHITE+"Enter 8 For Searching A Course.");
            System.out.println(ConsoleColors.WHITE+"Enter 9 For Logout.");
            System.out.println(ConsoleColors.WHITE+"Enter 10 For Delete Account.");
            System.out.print(ConsoleColors.BLUE +"Enter Your Choice : ");
            int choice;
           try{
              try {
                  choice = sc.nextInt();
                  sc.nextLine();
              }catch (InputMismatchException e){
                  sc.nextLine();
                  System.out.println("Invalid Exception");
                  continue;
              }
               switch (choice){
                   case 1 :
                       viewCourses(con);
                       break;

                   case 2 :
                       System.out.print(ConsoleColors.BLUE +"Enter Course ID You Want To Enroll : ");
                       try{
                           int course_id = sc.nextInt();
                           sc.nextLine();
                           courseEnrollment(con,course_id);
                       }catch (Exception e){
                           System.out.println(ConsoleColors.RED + "Sorry Invalid Input");
                           continue;
                       }

                       break;

                   case 3 :
                       viewEnrolledCourses(con);
                       break;

                   case 4 :
                       obj.update(con,sc);
                       break;

                   case 5 :
                       obj.view(con);
                       break;

                   case 6 :
                       System.out.print(ConsoleColors.BLUE +"Enter Course ID You Want to Dis-Enroll : ");
                       try{
                           int disenrollcourse_id = sc.nextInt();sc.nextLine();
                           disEnrollCourse(con,disenrollcourse_id);
                       }catch (InputMismatchException e){
                           System.out.println(ConsoleColors.RED + "Invalid Input");
                           continue;
                       }

                       break;

                   case 7:
                       viewTop5ExpensiveCourses(con);
                       break;

                   case 8:
                       searchCourse(sc);
                       break;

                   case 9:
                       logout(con);
                       temp=false;
                       break;

                   case 10 :
                       delAccount(con);
                       return;
                      // break;

                   default:
                       System.out.println(ConsoleColors.RED + "Enter Appropriate Choice!");
               }
           } catch (Exception e) {

               System.out.println(ConsoleColors.RED + "Invalid Input");
               System.out.println(ConsoleColors.RED + "Error Occured In Try Catch");
               continue;
           }

        }while (temp);
    }

}

interface PaymentMethod {
    boolean pay(double amount);
}

// Concrete Implementations (Polymorphism)
class CreditCardPayment implements PaymentMethod {
    public boolean pay(double amount) {
        System.out.println(ConsoleColors.BLUE + "Paid ₹" + amount + " using Credit Card.");
        return true; // Assume always successful
    }
}

class UPIPayment implements PaymentMethod {
    public boolean pay(double amount) {
        System.out.println(ConsoleColors.BLUE +"Paid ₹" + amount + " using UPI.");
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
class CourseDump {
    public boolean paymentMenu(Scanner sc,int studentId,double coursePrice) {

        PaymentPortal portal = new PaymentPortal();
        System.out.println(ConsoleColors.CYAN + "Choose Payment Method: ");
        System.out.println(ConsoleColors.CYAN + "Enter 1 For Credit Card.");
        System.out.println(ConsoleColors.CYAN + "Enter 2 For UPI.");
        System.out.print(ConsoleColors.PURPLE + "Enter Your Choice : ");
        try{
        int choice = sc.nextInt();
        PaymentMethod method;

        switch (choice) {
            case 1: method = new CreditCardPayment(); break;
            case 2: method = new UPIPayment(); break;
            default: System.out.println(ConsoleColors.RED + "Invalid choice"); return false;
        }

        if (portal.makePayment(studentId, coursePrice, method)) {
            System.out.println(ConsoleColors.BLUE +"Payment Successful  Student can now enroll in course.");
            return true;
        } else {
            System.out.println(ConsoleColors.RED + "Payment Failed ❌ Please try again.");
            return false;
        }
        }catch (InputMismatchException e){
            System.out.println("Invalid Input");
            return false;
        }
    }
}