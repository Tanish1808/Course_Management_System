import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CourseAdmin {

    public void dashboard(){ System.out.println(ConsoleColors.CYAN + "CourseAdmin → Manage Courses"); }

    /*
        Course Admin Functionality.
        a.	Approve Course
        b.	Add Course
        c.	Update Course
        d.	Delete Course
        e.	View All Course
        f.	Search Course
        g.	View Overall Grades
     */
    ArrayList<Integer> al=new ArrayList<>();
    void setUpArrayList(Connection con)throws Exception{
        String query="Select user_Id From users WHERE role='instructor'";
        PreparedStatement pst=con.prepareStatement(query);
        ResultSet rs=pst.executeQuery();
        while(rs.next()){
            al.add(rs.getInt(1));
        }
    }
    // 1. Approve Course
    public void approveCourse(Connection con,int courseId)throws SQLException {
        String sql = "UPDATE request SET status='Approved'  WHERE request_Id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, courseId);
        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println(ConsoleColors.BLUE + " Course approved successfully.");
        } else {
            System.out.println(ConsoleColors.RED + " Course not found.");
        }
    }

    // 2. Add Course
    public void addCourse(Connection con,String courseName, String description, int instructorId,String courseDuration,String Category,double price)throws SQLException {
        String sql = "INSERT INTO course(courseName, courseDescription , instructor_Id, courseDuration,category,coursePrice) VALUES(?,?,?,?,?,?) ";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, courseName);
        ps.setString(2, description);
        ps.setInt(3, instructorId);
        ps.setString(4, courseDuration);
        ps.setString(5, Category);
        ps.setDouble(6, price);
        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println(ConsoleColors.BLUE + "Course added successfully.");
        }else{
            System.out.println(ConsoleColors.RED + "Course Didn't Added ");
        }
    }

    // 3. Update Course
    public void updateCourse(Connection con,int courseId, String newName, String newDesc)throws SQLException {
        String sql = "UPDATE course SET courseName=?, courseDescription=? WHERE course_Id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, newName);
        ps.setString(2, newDesc);
        ps.setInt(3, courseId);
        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println(ConsoleColors.BLUE + "Course updated successfully.");
        } else {
            System.out.println(ConsoleColors.RED + "Course not found.");
        }
    }

    // 4. Delete Course
    public void deleteCourse(Connection con,int courseId) throws SQLException{
        String sql = "DELETE FROM course WHERE course_Id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, courseId);
        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println(ConsoleColors.BLUE + " Course deleted successfully.");
        } else {
            System.out.println(ConsoleColors.RED + " Course not found.");
        }
    }

    // 5. View All Courses
    public void viewAllCourses(Connection con) throws SQLException{
        String sql = "SELECT c.course_Id, c.courseName, c.courseDescription, concat(u.first_name,u.last_name) As InstructorName FROM course c Inner Join users u on c.instructor_Id=u.user_Id;";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        System.out.println(" All Courses:");
        while (rs.next()) {
            System.out.printf(ConsoleColors.CYAN + "CourseId:%d \nCourseName:%s \nCourseDescription:%s \nInstructorName: %s \n",
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4));
            System.out.println( );
            System.out.println( );
        }
    }

    // 6. Search Course
    public void searchCourse(Connection con,String keyword)throws SQLException {
        String sql = "SELECT course_Id, courseName, courseDescription, instructor_Id,concat(first_name,last_name)" +
                " As InstructorName FROM course c " +
                "Inner Join users u on u.user_Id=c.instructor_Id WHERE courseName LIKE ? OR courseDescription LIKE ?";
        PreparedStatement ps = con.prepareStatement(sql);
        String likePattern = "%" + keyword + "%";
        ps.setString(1, likePattern);
        ps.setString(2, likePattern);
        ResultSet rs = ps.executeQuery();
        System.out.println(ConsoleColors.CYAN + " Search Results:");
        while (rs.next()) {
            System.out.println(ConsoleColors.BLUE_BOLD + "Course:-");
            System.out.print(ConsoleColors.YELLOW + "CourseId:"+rs.getInt(1));
            System.out.print(ConsoleColors.YELLOW +" CourseName:"+rs.getString(2));
            System.out.print(ConsoleColors.YELLOW +"\n CourseDescription:"+rs.getString(3));
            System.out.print(ConsoleColors.YELLOW +"\n InstructorId:"+rs.getInt(4));
            System.out.print(ConsoleColors.YELLOW +" InstructorName:"+rs.getString(5)+"\n\n");
        }
    }

    public void downloadCourseDetails(Connection con,int courseId) throws Exception{
        FileWriter fwr=new FileWriter(new File("Course_" + courseId+".txt"));
        String sql="Select course_Id,courseName,concat(first_name,last_name) As InstructorName, courseDuration,courseDescription,category From course c Inner Join users u On u.user_Id=c.instructor_Id WHERE u.role='instructor' And c.course_Id=?";
        PreparedStatement pst=con.prepareStatement(sql);
        pst.setInt(1,courseId);
        ResultSet rs=pst.executeQuery();
        if(rs.next()){
            System.out.println("Found Your Course");
            fwr.write("==============================================");
            fwr.write("\nCourseId:"+rs.getInt(1)+"\n");
            fwr.write("CourseName:"+rs.getString(2)+"\n");
            fwr.write("InstructorName:"+rs.getString(3)+"\n");
            fwr.write("CourseCategory:"+rs.getString(6)+"\n");
            fwr.write("CourseDescription:"+rs.getString(5)+"\n");
            fwr.write("CourseDuration:"+rs.getString(4)+"\n");
            fwr.write("==============================================\n\n");
        }else{
            System.out.println(ConsoleColors.RED + "No Such Course Exist");
        }
        fwr.flush();
    }

    // 8. View Overall Grades (Example: Assuming grades table links students & courses)
    public void viewOverallGrades(Connection con) throws SQLException{
        String sql = "SELECT c.course_Id,courseName, AVG(marks) AS avgMarks FROM grades g Inner Join enrollment e " +
                "On g.enrollment_Id=e.enrollment_Id Inner Join course c On c.course_Id=e.course_Id GROUP BY c.course_Id,courseName";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        System.out.println(ConsoleColors.CYAN + " Overall Grades (by Course):");
        while (rs.next()) {
            System.out.println(ConsoleColors.BLUE + "CourseId:"+rs.getInt(1)+ConsoleColors.BLUE + "  CourseName:"+rs.getString(2)+
                    ConsoleColors.BLUE +"  AvgGrade:"+rs.getInt(3) );
        }
    }
boolean checkInstructor(int instructorId){
        for(int x:al){
            if(x==instructorId){
                return false;
            }
        }
        return true;
}
    //CourseAdminMainMenu
    public void CourseAdminMainMenu(Connection con,Scanner sc) throws Exception {
        System.out.println(ConsoleColors.RED_BOLD + "Logged In As Course Admin!");
        dashboard();
        int choice=-1;
        setUpArrayList(con);
        do {
            System.out.println(ConsoleColors.BLUE + "\n====== COURSE MANAGEMENT MENU ======");
            System.out.println(ConsoleColors.WHITE + "1. Approve Course");
            System.out.println(ConsoleColors.WHITE + "2. Add Course");
            System.out.println(ConsoleColors.WHITE + "3. Update Course");
            System.out.println(ConsoleColors.WHITE +"4. Delete Course");
            System.out.println(ConsoleColors.WHITE +"5. View All Courses");
            System.out.println(ConsoleColors.WHITE +"6. Search Course");
            System.out.println(ConsoleColors.WHITE +"7. Download Course Details");
            System.out.println(ConsoleColors.WHITE +"8. View Overall Grades");
            System.out.println(ConsoleColors.WHITE +"9. Logout");
            System.out.print(ConsoleColors.BLUE +" Enter your choice: ");
           try{
               choice = sc.nextInt();
               sc.nextLine();
               switch (choice) {
                   case 1:
                       String SQL="Select * From request Where status='Pending'";
                       Statement st=con.createStatement();
                       ResultSet rs=st.executeQuery(SQL);
                       System.out.println(ConsoleColors.CYAN + "Request To Approve:");
                       if(rs!=null){
                           while(rs.next()){
                               System.out.print(ConsoleColors.BLUE + "RequestId:"+rs.getInt(1));
                               System.out.print(ConsoleColors.BLUE +"\nCourseName:"+rs.getString(2));
                               System.out.print(ConsoleColors.BLUE +"\nCourseDescription:"+rs.getString(3));
                               System.out.print(ConsoleColors.BLUE +"\nInstructorId:"+rs.getInt(4));
                               System.out.print(ConsoleColors.BLUE +"\nStatus:"+rs.getString(5));
                               System.out.println();
                               System.out.println();
                           }
                           System.out.print(ConsoleColors.CYAN + "Want To Approve Any Course(Yes/No):");
                           String option=sc.next();
                           if(option.equalsIgnoreCase("Yes")){
                               System.out.print(ConsoleColors.BLUE + "Enter Course's Request ID to approve: ");
                               int approveId = sc.nextInt();sc.nextLine();
                               approveCourse(con, approveId);

                           }else if(option.equalsIgnoreCase("No")){
                               System.out.println(ConsoleColors.PURPLE + "No Worries Keeping Some For Next Time");
                           }else{
                               System.out.println(ConsoleColors.RED + "Wrong Choice Should Have Enterred Yes Or No");
                           }

                       }else{
                           System.out.println(ConsoleColors.YELLOW + "There Are No Request To Approve");
                       }
                       break;

                   case 2:
                       System.out.print(ConsoleColors.BLUE + "Enter Course Name: ");
                       String cname = sc.nextLine();
                       System.out.print(ConsoleColors.BLUE + "Enter Description: ");
                       String desc = sc.nextLine();
                       boolean tem=true;int instId;
                       do{
                           System.out.print(ConsoleColors.BLUE + "Enter Instructor ID: ");
                            instId = sc.nextInt();
                           sc.nextLine();
                           if(checkInstructor(instId)==false){
                               tem=false;
                           }else{
                               System.out.println(ConsoleColors.RED + "No Such Instructor Exists");
                           }
                       }while(tem);
                       try{
                           System.out.println(ConsoleColors.PURPLE + "Enter For Course Duration :- ");
                           System.out.print(ConsoleColors.BLUE + "Enter Hours : ");
                           int courseHours=sc.nextInt();sc.nextLine();
                           System.out.print(ConsoleColors.BLUE +"Enter Minutes : ");
                           int courseMinute=sc.nextInt();sc.nextLine();
                           if(courseMinute>=60){
                               courseHours+=(courseMinute/60);
                               courseMinute%=60;
                           }
                           System.out.print(ConsoleColors.BLUE +"Enter Seconds : ");
                           int courseSecond=sc.nextInt();sc.nextLine();
                           if(courseSecond>=60){
                               courseMinute+=(courseSecond/60);
                               courseSecond%=60;
                           }
                           System.out.print(ConsoleColors.BLUE +"Enter Category: ");
                           String category = sc.nextLine();
                           System.out.print(ConsoleColors.BLUE +"Enter Course Price:");
                           double price=sc.nextDouble();sc.nextLine();
                           String courseDuration=courseHours+":"+courseMinute+":"+courseSecond;
                           addCourse(con, cname, desc, instId, courseDuration,category,price);
                       }catch (InputMismatchException e){
                           System.out.println(e.getMessage());
                       }
                       break;

                   case 3:
                       System.out.print(ConsoleColors.BLUE +"Enter Course ID to update: ");
                       int updId = sc.nextInt();
                       sc.nextLine();
                       System.out.print(ConsoleColors.BLUE +"Enter New Course Name: ");
                       String newName = sc.nextLine();
                       System.out.print(ConsoleColors.BLUE +"Enter New Description: ");
                       String newDesc = sc.nextLine();
                       updateCourse(con, updId, newName, newDesc);
                       break;

                   case 4:
                       System.out.print(ConsoleColors.BLUE +"Enter Course ID to delete: ");
                       int delId = sc.nextInt();
                       deleteCourse(con, delId);
                       break;

                   case 5:
                       viewAllCourses(con);
                       break;

                   case 6:
                       System.out.print(ConsoleColors.BLUE +"Enter keyword to search: ");
                       String key = sc.nextLine();
                       searchCourse(con, key);
                       break;

                   case 7 :
                       try{
                           System.out.print(ConsoleColors.BLUE +"Enter Course ID You Want to Download: ");
                           int downloadId = sc.nextInt();sc.nextLine();
                           downloadCourseDetails(con,downloadId);
                       }catch (InputMismatchException e){
                           sc.nextLine();
                           System.out.println("Invalid Input!!" +
                                   "9");
                       }
                       break;

                   case 8:
                       viewOverallGrades(con);
                       break;

                   case 9:
                       System.out.println(ConsoleColors.BLUE_BOLD + " Exiting... Thank you!");
                       break;

                   default:
                       System.out.println(ConsoleColors.RED + " Invalid choice, please try again!");
               }
           }catch(InputMismatchException e){
               sc.nextLine();
               System.out.println(ConsoleColors.RED + "Enter Invalid Input!!");
           }

        } while (choice != 9);

    }
}
