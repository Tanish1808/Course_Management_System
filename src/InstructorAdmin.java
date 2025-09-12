import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InstructorAdmin extends users{

    public void dashboard(){ System.out.println(ConsoleColors.CYAN + "InstructorAdmin → Manage Instructors"); }

    /*

        Instructor Admin Functionality.
      a. Add Instructor
      b. Delete Instructor
      c. Update Instructor
      d. View All Instructor
      e. Search Instructor
      f. View Overall Salary
     */
    ArrayList<String> al=new ArrayList<>();
    void setUpEmail(Connection con) throws Exception{
        String query="Select email From users ";
        PreparedStatement pst=con.prepareStatement(query);
        ResultSet rs=pst.executeQuery();
        while (rs.next()){
            al.add(rs.getString(1));
        }
    }
    boolean emailFound(String email)throws Exception{
        for(String x:al){
            if(x.equalsIgnoreCase(email)){
                return true;
            }
        }
        return false;
    }
    // 1. Add Instructor
    public void addInstructor(Connection con,String firstName, String lastName, String mobileNo,
                              String email, String password, double salary) throws SQLException {
        String sql = "INSERT INTO users(first_name, last_name, mobileNo, email, password, role, salary) VALUES(?,?,?,?,?, 'instructor', ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.setString(3, mobileNo);
        ps.setString(4, email);
        ps.setString(5, password);
        ps.setDouble(6, salary);
        System.out.println(ConsoleColors.BLUE + " Trying to add instructor...");
        int noOfRows= ps.executeUpdate();
        if(noOfRows>0){
            System.out.println(ConsoleColors.RED + " Instructor added successfully.");
        }else{
            System.out.println(ConsoleColors.YELLOW + " Instructor Was Not added.");
        }

    }

    // 2. Delete Instructor
    public void deleteInstructor(Connection con,int instructorId) throws SQLException{
        String sql = "DELETE FROM users WHERE user_Id=? AND role='instructor'";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, instructorId);
        String query="Select user_Id From users WHERE role='instructor' AND user_Id=?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,instructorId);
        ResultSet rs=pst.executeQuery();
        if(rs.next()){
            if(rs.getInt(1)==instructorId){
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println(ConsoleColors.BLUE + " Instructor deleted successfully.");
                } else {
                    System.out.println(ConsoleColors.YELLOW + " Instructor not found.");
                }
                return;
            }else{
                System.out.println(ConsoleColors.RED + "No Such Instructor Found");
                System.out.println();
            }
        }else{
            System.out.println(ConsoleColors.RED + "No Such Instructor Found");
            System.out.println();
        }

    }

    // 3. Update Instructor
    public void updateInstructor(Connection con,int instructorId,String firstName,String lastName,String mobileNo, String newEmail, String password,double newSalary)throws SQLException {
        String sql = "UPDATE users SET first_name=?,last_name=?,mobileNo=?, email=?,password=?, salary=? WHERE user_Id=? AND role='instructor'";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.setString(3, mobileNo);
        ps.setString(4, newEmail);
        ps.setString(5, password);
        ps.setDouble(6, newSalary);
        ps.setInt(7, instructorId);
        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println(ConsoleColors.BLUE + "Instructor updated successfully.");
        } else {
            System.out.println(ConsoleColors.RED + "️ Instructor not found.");
        }
    }

    // 4. View All Instructors
    public void viewAllInstructors(Connection con)throws SQLException {
        String sql = "SELECT user_Id As Instructor_Id, first_name, last_name, email, salary FROM users WHERE role=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1,"instructor");
        ResultSet rs = pstmt.executeQuery();
        System.out.println( ConsoleColors.CYAN +" All Instructors:");
        while (rs.next()) {
            System.out.printf(ConsoleColors.YELLOW + "%d | %s %s | %s | Salary: %.2f\n",
                    rs.getInt("Instructor_Id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getDouble("salary"));
        }
    }

    // 5. Search Instructor
    public void searchInstructor(Connection con,int instructor_Id)throws SQLException {
        String sql = "SELECT user_Id, first_name, last_name, email, salary FROM users WHERE role='instructor' AND " +
                "user_Id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1,instructor_Id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println(ConsoleColors.CYAN + " Search Results:");
            System.out.printf(ConsoleColors.YELLOW + "%d | %s %s | %s | Salary: %.2f\n",
                    rs.getInt("user_Id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getDouble("salary"));
        }else{
            System.out.println("No Such Instructor Exists");
        }
    }

    public void downloadInstructorDetails(Connection con,int instructorId)throws Exception{
        FileWriter fwr=new FileWriter(new File("Instructor_" +instructorId+ ".txt"));
        String sql="Select * from users WHERE role='instructor' AND user_Id=?";
        PreparedStatement pst=con.prepareStatement(sql);
        pst.setInt(1,instructorId);
        ResultSet rs=pst.executeQuery();
        if(rs.next()){
            fwr.write("==============================================");
            fwr.write("\nInstructor Id:"+rs.getInt(1)+"\n");
            fwr.write("Instructor First Name:"+rs.getString(2)+"\n");
            fwr.write("Instructor Last Name:"+rs.getString(3)+"\n");
            fwr.write("Instructor MobileNo:"+rs.getString(4)+"\n");
            fwr.write("Instructor Email :"+rs.getString(5)+"\n");
            fwr.write("Instructor Account Password:"+rs.getString(6)+"\n");
            fwr.write("Instructor Salary:"+rs.getDouble(8)+"\n");
            fwr.write("==============================================\n\n");
        }else{
            System.out.println(ConsoleColors.RED + "No Such Instructor Exists");
        }
        fwr.flush();
    }

    // 7. View Overall Salary
    public void viewOverallSalary(Connection con) throws SQLException{
        String sql = "SELECT SUM(salary) AS TotalSalary FROM users WHERE role='instructor'";
        Statement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            System.out.println(ConsoleColors.BLUE + " Total Salary of All Instructors: " + rs.getDouble("totalSalary"));
        }
    }
    //Main Menu Of Instructor Admin
    public void InstructorAdminFunctionality(Connection con,Scanner sc) throws Exception {
        System.out.println(ConsoleColors.RED_BOLD + "Logged In As Instructor Admin!");
        dashboard();
        int choice=-1;
        setUpEmail(con);
        do {
            System.out.println(ConsoleColors.BLUE + "\n====== INSTRUCTOR MANAGEMENT MENU ======");
            System.out.println(ConsoleColors.WHITE + "1. Add Instructor");
            System.out.println(ConsoleColors.WHITE +"2. Delete Instructor");
            System.out.println(ConsoleColors.WHITE +"3. Update Instructor");
            System.out.println(ConsoleColors.WHITE +"4. View All Instructors");
            System.out.println(ConsoleColors.WHITE +"5. Search Instructor");
            System.out.println(ConsoleColors.WHITE +"6. Download Instructor Details");
            System.out.println(ConsoleColors.WHITE +"7. View Overall Salary");
            System.out.println(ConsoleColors.WHITE +"8. Logout");
            System.out.print(ConsoleColors.BLUE + " Enter your choice: ");
           try {
               choice = sc.nextInt();
               sc.nextLine();
           }catch(InputMismatchException e){
               sc.nextLine();
               System.out.println("Invalid Input !!");
               continue;
           }
            switch (choice) {
                case 1:
                    System.out.print(ConsoleColors.BLUE +"First Name: ");
                    String fn = sc.next();
                    System.out.print(ConsoleColors.BLUE +"Last Name: ");
                    String ln = sc.next();
                    String mob;
                    do {
                        boolean isValid=true;
                        System.out.print(ConsoleColors.BLUE + "Enter Mobile No:");
                        mob = sc.next();sc.nextLine();
                        if (mob.length() == 10) {
                            for (char c : mob.toCharArray()) {
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
                    do{
                        System.out.print(ConsoleColors.BLUE +"Email: ");
                         email = sc.next();sc.nextLine();
                        if(email.endsWith("@gmail.com") && emailFound(email)==false){
                                break;
                        }else{
                            System.out.println(ConsoleColors.RED + "Already Registered By Someone Else");
                        }
                    }while(true);

                    System.out.print(ConsoleColors.BLUE +"Password: ");
                    String pass = sc.next();
                    System.out.print(ConsoleColors.BLUE +"Salary: ");
                    try{
                        double sal = sc.nextDouble();sc.nextLine();
                        addInstructor(con, fn, ln, mob, email, pass, sal);
                        setUpEmail(con);
                    }catch (InputMismatchException e){
                        System.out.println(e.getMessage());
                        continue;
                    }

                    break;

                case 2:
                    System.out.print(ConsoleColors.BLUE +"Enter Instructor ID to delete: ");
                    try{
                        int delId = sc.nextInt();
                        sc.nextLine();
                        deleteInstructor(con, delId);
                        al.clear();
                        setUpEmail(con);
                    }catch (InputMismatchException e){
                        sc.nextLine();
                        System.out.println("Invalid Input");
                    }
                    break;

                case 3:
                    System.out.print(ConsoleColors.BLUE +"Enter Instructor ID to update: ");
                    int instructorId1 = sc.nextInt();sc.nextLine();
                    String query="Select user_Id From users WHERE role='instructor' AND user_Id=?";
                    PreparedStatement pst = con.prepareStatement(query);
                    pst.setInt(1,instructorId1);
                    ResultSet rs=pst.executeQuery();
                    if(rs.next()){
                        System.out.print(ConsoleColors.BLUE +"Enter First Name : ");
                        String firstName=sc.nextLine();
                        System.out.print(ConsoleColors.BLUE +"Enter Last Name : ");
                        String lastName=sc.nextLine();
                        String mobileNo;
                        do{
                            boolean isValid=true;
                            System.out.print(ConsoleColors.BLUE +"Enter Mobile No:");
                            mobileNo=sc.next();
                            if(mobileNo.length()==10){
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
                            }else{
                                System.out.println( ConsoleColors.RED+ "Enter 10 Digit Phone Number Only");
                            }
                        }while(true);
                        String newEmail;
                        do{
                            System.out.print(ConsoleColors.BLUE +"New Email: ");
                            newEmail = sc.next();
                            if(emailFound(newEmail)==false){
                                break;
                            }else{
                                System.out.println(ConsoleColors.YELLOW + "Duplicate Email Found");
                            }
                        }while(true);
                        System.out.print(ConsoleColors.BLUE +"Enter New Password : ");
                        String newPass=sc.next();sc.nextLine();
                        System.out.print(ConsoleColors.BLUE +"New Salary: ");
                        try{
                            double newSal = sc.nextDouble();sc.nextLine();
                            updateInstructor(con, instructorId1,firstName,lastName,mobileNo, newEmail,newPass, newSal);
                            setUpEmail(con);
                        }catch(InputMismatchException e){
                            System.out.println(e.getMessage());
                        }

                    }else{
                        System.out.println(ConsoleColors.RED + "No Such Instructor Found");
                    }

                    break;

                case 4:
                    viewAllInstructors(con);
                    break;

                case 5:
                    System.out.print(ConsoleColors.BLUE +"Enter Instructor ID to search: ");
                    try{
                        int searchId = sc.nextInt();sc.nextLine();
                        searchInstructor(con, searchId);
                    }catch(InputMismatchException e){
                        System.out.println(e.getMessage());continue;
                    }
                    break;

                case 6 :
                    System.out.print(ConsoleColors.BLUE +"Enter Instructor ID to Download: ");
                    try {
                        int instructorId = sc.nextInt();
                        downloadInstructorDetails(con, instructorId);
                    }catch (InputMismatchException e){
                        System.out.println(e.getMessage());
                        continue;
                    }
                    break;
                case 7:
                    viewOverallSalary(con);
                    break;

                case 8:
                    System.out.println(ConsoleColors.YELLOW + " Loging Out... Thank you!");
                    break;

                default:
                    System.out.println(ConsoleColors.RED + " Invalid choice, please try again!");
            }
        } while (choice != 8);
    }
}
