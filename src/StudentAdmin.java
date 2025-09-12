import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class StudentAdmin extends users{

    public void dashboard(){ System.out.println(ConsoleColors.CYAN + "StudentAdmin → Manage Students"); }

    /*
        a. Delete Student
        b. Update Student
        c. View All Student
        d. Search Student
        e. View Overall Grades
        f. View Top 5 Students
     */

    //  Delete Student
    public void deleteStudent(Connection con , int studentId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_Id=? ";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, studentId);
        int rows = pst.executeUpdate();
        System.out.println(rows > 0 ? ConsoleColors.BLUE + "Student Deleted!" : ConsoleColors.RED + "Student Not Found!");
    }

    //  Update Student
    public void updateStudent(Connection con,Scanner sc,int studentId) throws SQLException {
        String sql = "UPDATE users SET first_name=?, last_name = ? , mobileNo = ? , email=?, password = ? WHERE user_Id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        System.out.print(ConsoleColors.BLUE +"Enter First Name:");
        String firstName=sc.nextLine();
        System.out.print(ConsoleColors.BLUE +"Enter Last Name:");
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
                System.out.println(ConsoleColors.RED + "Enter 10 Digit Phone Number Only");
            }
        }while(true);
        String email;
        do{
            System.out.print(ConsoleColors.BLUE +"Enter Email:");
            email=sc.next().toLowerCase();
            if(email.endsWith("@gmail.com")){
                String sql1="Select email From Users where email=?;";
                PreparedStatement ps=con.prepareStatement(sql1);
                ps.setString(1,email);
                ResultSet rs=ps.executeQuery();
                if(rs.next()){
                    System.out.println(ConsoleColors.RED + "Enterred Email Id Is Already Been Registered");
                }else{
                    break;
                }
            }else{
                System.out.println(ConsoleColors.RED + "Email Id Is Invalid!!");
            }
        }while(true);
        System.out.print(ConsoleColors.BLUE +"Enter Your Password:");
        String psassword=sc.next();
        pst.setString(1, firstName);
        pst.setString(2,lastName);
        pst.setString(3, mobileNo);
        pst.setString(4,email);
        pst.setString(5,psassword);
        pst.setInt(6, studentId);
        int rows = pst.executeUpdate();
        System.out.println(rows > 0 ?ConsoleColors.BLUE + "Student Updated!" : ConsoleColors.RED +"Student Not Found!");
    }

    //  View All Students
    public void viewAllStudents(Connection con) throws SQLException {
        String sql = "SELECT * FROM users WHERE role = ?";
        PreparedStatement st = con.prepareStatement(sql);
        st.setString(1,"Student");
        ResultSet rs = st.executeQuery();
        System.out.println();
        System.out.println();
        while (rs.next()) {
            System.out.println(ConsoleColors.YELLOW +"Student-ID : " + rs.getInt("user_Id"));
            System.out.println(ConsoleColors.YELLOW +"First Name : " + rs.getString("first_name"));
            System.out.println(ConsoleColors.YELLOW +"Last Name : " + rs.getString("last_name"));
            System.out.println(ConsoleColors.YELLOW +"Mobile No : " + rs.getString("mobileNo"));
            System.out.println(ConsoleColors.YELLOW +"Email : " + rs.getString("email"));
            System.out.println(ConsoleColors.YELLOW +"Password : " + rs.getString("password"));
            System.out.println();
            System.out.println();
        }
    }

    //  Search Student by name/email
    public void searchStudent(Connection con , int student_id) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_Id = ? AND role = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1,student_id);
        pst.setString(2,"Student");
        ResultSet rs = pst.executeQuery();
        if(rs.next()) {
            System.out.println(ConsoleColors.YELLOW +"ID : " + rs.getInt("user_Id"));
            System.out.println(ConsoleColors.YELLOW +"First Name : " + rs.getString("first_name"));
            System.out.println(ConsoleColors.YELLOW +"Last Name : " + rs.getString("last_name"));
            System.out.println(ConsoleColors.YELLOW +"Mobile No : " + rs.getString("mobileNo"));
            System.out.println(ConsoleColors.YELLOW +"Email : " + rs.getString("email"));
            System.out.println(ConsoleColors.YELLOW +"Password : " + rs.getString("password"));
        }else{
            System.out.println(ConsoleColors.RED +"No Such Student Exists");
        }
    }



    //  View Overall Grades (avg per course)
    public void viewOverallGrades(Connection con) throws SQLException {
        String sql = "SELECT c.courseName, AVG(g.marks) AS avg_Score " +
                "FROM grades g JOIN enrollment e on g.enrollment_Id = e.enrollment_Id " +
                "JOIN course c ON e.course_Id=c.course_Id " +
                "GROUP BY c.courseName";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        System.out.println();
        while (rs.next()) {
            System.out.println(ConsoleColors.BLUE + "CourseName:"+rs.getString("courseName") + ConsoleColors.BLUE + rs.getDouble("avg_Score"));
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    public void downloadStudentDetails(Connection con,int studentId)throws Exception{
        FileWriter fwr=new FileWriter(new File("Student_" +studentId+ ".txt"));
        String sql="Select u.user_Id,concat(u.first_name,u.last_name),u.mobileNo,u.email,u.password" +
                ",c.courseName,c.courseDescription" +
                " From users u Inner Join enrollment e On u.user_Id=e.student_Id " +
                " Inner Join course c On c.course_Id=e.course_Id WHERE u.role=? And " +
                "u.user_Id=?";
        PreparedStatement pst=con.prepareStatement(sql);
        pst.setString(1,"Student");
        pst.setInt(2,studentId);
        ResultSet rs=pst.executeQuery();
        if(rs.next()){
            fwr.write("==============================================");
            fwr.write("Student Id:"+rs.getInt(1)+"\n");
            fwr.write("Student Name:"+rs.getString(2)+"\n");
            fwr.write("Student MobileNo:"+rs.getString(3)+"\n");
            fwr.write("Student Email:"+rs.getString(4)+"\n");
            fwr.write("Student's AccountPassword:"+rs.getString(5)+"\n");
            fwr.write("Enrolled Course Name"+rs.getString(6)+"\n");
            fwr.write("CourseDescription:"+rs.getString(7)+"\n");
            fwr.write("==============================================\n\n");
            fwr.flush();
            System.out.println(ConsoleColors.YELLOW + "Student Details Downloaded");
        }else{
            System.out.println(ConsoleColors.RED + "No Such Student Found!!");
        }

    }

    //  View Top 5 Students (overall)
    public void viewTop5Students(Connection con) throws Exception {

        String sql = "Select concat(u.first_name,u.last_name) As StudentName,g.marks,g.grade From users u " +
                "Inner Join enrollment e On u.user_Id=e.student_Id " +
                "Inner Join grades g On g.enrollment_Id=e.enrollment_Id " +
                "WHERE u.role = ? Group By StudentName,g.marks,g.grade Order By g.marks DESC Limit 5 ";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1,"Student");
        ResultSet rs = pst.executeQuery();
        System.out.println();
        System.out.println();
        System.out.println(ConsoleColors.CYAN + "Top 5 Students Are :-");
        while (rs.next()){
            System.out.println(ConsoleColors.YELLOW + "Student Name : " + rs.getString(1));
            System.out.println(ConsoleColors.YELLOW +"Marks : " + rs.getInt(2));
            System.out.println(ConsoleColors.YELLOW +"Grade : " + rs.getString(3));
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    //  Logout (just exit session)
    public void logout() {
        System.out.println(ConsoleColors.YELLOW + "Logged out successfully!");
    }

    ArrayList<Integer> al=new ArrayList<>();
    void setUpArrayList(Connection con)throws Exception{
        al.clear();
        String query="Select user_Id From users WHERE role='Student'";
        PreparedStatement pst=con.prepareStatement(query);
        ResultSet rs=pst.executeQuery();
        while(rs.next()){
            al.add(rs.getInt(1));
        }
    }

    boolean checkStudent(int studentId){
        for(int x:al){
            if(x==studentId){
                return false;
            }
        }
        return true;
    }
    public void StudentAdminFunctionality(Connection con,Scanner sc) throws Exception{
        System.out.println(ConsoleColors.RED_BOLD + "Logged In As Student Admin!");
        dashboard();
        setUpArrayList(con);
        boolean temp=true;
        do{
            System.out.println(ConsoleColors.BLUE + "\n====== STUDENT MANAGEMENT MENU ======");
            System.out.println(ConsoleColors.WHITE +"Enter 1 For Delete Student.");
            System.out.println(ConsoleColors.WHITE +"Enter 2 For Update Student.");
            System.out.println(ConsoleColors.WHITE +"Enter 3 For View All Student.");
            System.out.println(ConsoleColors.WHITE +"Enter 4 For Search Student.");
            System.out.println(ConsoleColors.WHITE +"Enter 5 For View Overall Grades.");
            System.out.println(ConsoleColors.WHITE +"Enter 6 For Download Student Details.");
            System.out.println(ConsoleColors.WHITE +"Enter 7 For View Top 5 Students.");
            System.out.println(ConsoleColors.WHITE +"Enter 8 For Logout.");
            System.out.print(ConsoleColors.BLUE +"Enter Your Choice : ");
          try {
              int choice = sc.nextInt();sc.nextLine();

              switch (choice) {
                  case 1:
                      System.out.print(ConsoleColors.BLUE +"Enter Student ID : ");
                      try {
                          int id = sc.nextInt();sc.nextLine();
                          if(checkStudent(id)==false){
                              deleteStudent(con, id);
                              setUpArrayList(con);
                          }else{
                              System.out.println(ConsoleColors.RED + "No Such Student Found");
                          }
                      } catch (InputMismatchException e) {
                          sc.nextLine();
                          System.out.println(e.getMessage());
                          continue;
                      }

                      break;

                  case 2:
                      System.out.print(ConsoleColors.BLUE +"Enter Student ID : ");
                      try {
                          int student_id = sc.nextInt();sc.nextLine();
                          if(checkStudent(student_id)==false){
                              updateStudent(con, sc, student_id);
                              setUpArrayList(con);
                          }else{
                              System.out.println(ConsoleColors.RED + "No Such Student Found");
                          }

                      } catch (InputMismatchException e) {
                          System.out.println(e.getMessage());
                          continue;
                      }
                      break;

                  case 3:
                      viewAllStudents(con);
                      break;

                  case 4:
                      System.out.print(ConsoleColors.BLUE +"Enter Student ID You Want to Search : ");
                      try {
                          int search_id = sc.nextInt();sc.nextLine();
                          searchStudent(con, search_id);

                      } catch (InputMismatchException e) {
                          sc.nextLine();
                          System.out.println(e.getMessage());
                          continue;
                      }
                      break;

                  case 5:
                      viewOverallGrades(con);
                      break;

                  case 6:
                      System.out.print(ConsoleColors.BLUE +"Enter Student ID You Want to Search : ");
                      try {
                          int student_id2 = sc.nextInt();
                          if(checkStudent(student_id2)==false){
                              downloadStudentDetails(con, student_id2);
                          }else{
                              System.out.println(ConsoleColors.RED + "No Such Student Found");
                          }

                      } catch (InputMismatchException e) {
                          sc.nextLine();
                          System.out.println(e.getMessage());
                          continue;
                      }
                      break;
                  case 7:
                      viewTop5Students(con);
                      break;

                  case 8:
                      logout();
                      temp=false;
                      break;

                  default:
                      System.out.println(ConsoleColors.RED + "Enter Appropriate Choice!");
              }
          }catch ( InputMismatchException e){
              sc.nextLine();
              System.out.println(e.getMessage());

          }
        }while (temp);
    }

}