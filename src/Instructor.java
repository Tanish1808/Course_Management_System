import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Instructor extends users implements DatabaseOperation  {



    int instructorId;

    public Instructor (int instructorId) {
        this.instructorId = instructorId;
    }

    public void dashboard() {
        System.out.println(ConsoleColors.CYAN + "Instructor Dashboard → Manage Courses, Assign Grades");
    }
   /*
    Instructor Functionality
    1.View Assigned Students
    2.Request To Admin
    3.View Existing Uploaded Courses
    4.Search Specific Course Performance
    5.Pass Approved Courses to Admin
    6.View Status of Their Request
    7.Logout
    8.Delete Account
 */

    // 1. View assigned students
    public void viewAssignedStudents(Connection con) throws Exception {
        String query = "SELECT u.user_Id, u.email, c.courseName " +
                "FROM enrollment e " +
                "JOIN users u ON e.student_Id = u.user_Id " +
                "JOIN course c ON e.course_Id = c.course_Id " +
                "WHERE c.instructor_Id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, instructorId);
        ResultSet rs = ps.executeQuery();
        int i = 1;
        System.out.println(ConsoleColors.CYAN + "All Enrolled Student List:-");
        while (rs.next()) {
            System.out.println(ConsoleColors.BLUE_BOLD + "Student - " + i++);
            System.out.println(ConsoleColors.YELLOW + "Student ID: " + rs.getInt("user_Id"));
            System.out.println(ConsoleColors.YELLOW + "Email: " + rs.getString("email"));
            System.out.println(ConsoleColors.YELLOW + "Course: " + rs.getString("courseName"));
            System.out.println();
            System.out.println();
        }
    }

    // 2. Request to Admin (for new course approval)
    public void update(Connection con,Scanner sc)throws Exception{
        viewUploadedCourses(con);
        System.out.print(ConsoleColors.BLUE +"Enter Course Name You Want to Request : ");
        String courseName = sc.nextLine();
        System.out.print(ConsoleColors.BLUE +"Enter Course Description You Want to Request : ");
        String courseDescription = sc.nextLine();
        String query = "INSERT INTO request(instructor_Id, courseName,courseDescription,status) VALUES (?, ?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, instructorId);
        ps.setString(2, courseName);
        ps.setString(3, courseDescription);
        ps.setString(4,"Pending");
        int r = ps.executeUpdate();
        System.out.println(r > 0 ? " Request Sent To Admin for Approval!" : "Request Approval Failed!");

    }
    public void requestToAdmin(Connection con,Scanner sc) throws Exception {
        update(con,sc);
            }

    // 3. View existing uploaded courses
    public void viewUploadedCourses(Connection con) throws Exception {
        String query = "SELECT * FROM course WHERE instructor_Id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, instructorId);
        ResultSet rs = ps.executeQuery();
        int i = 1;
        while (rs.next()) {
            System.out.println(ConsoleColors.CYAN + "Course - " + i++);
            System.out.println(ConsoleColors.BLUE +"Course ID: " + rs.getInt("course_Id") +
                    ConsoleColors.BLUE + ", CourseName: " + rs.getString("courseName"));
        }
    }

    // 4. Search specific course performance
    public void searchCoursePerformance(Connection con , int courseId) throws Exception {
        String query = "SELECT u.user_Id,u.first_name, u.last_name, u.email, g.grade " +
                "FROM grades g Inner Join enrollment e On e.enrollment_Id=g.enrollment_Id " +
                "Inner JOIN users u ON e.student_Id = u.user_Id " +
                "WHERE e.course_Id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, courseId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(ConsoleColors.YELLOW + "Student ID : " + rs.getInt(1));
            System.out.println(ConsoleColors.YELLOW +"Student First Name : " + rs.getString(2));
            System.out.println(ConsoleColors.YELLOW +"Student Last Name : " + rs.getString(3));
            System.out.println(ConsoleColors.YELLOW +"Student' Email: " + rs.getString(4));
            System.out.println(ConsoleColors.YELLOW +"Grade: " + rs.getString(5));
        }
    }
    //5. Add Course Content
    public void insert(Connection con,int courseId) throws Exception{
        java.util.Scanner sc=new java.util.Scanner(System.in);
        System.out.print(ConsoleColors.PURPLE + "Enter FilePath:");
        String filePath = sc.next();
        sc.nextLine();// Could be "lecture1.jpg" or "lecture1.mp4"
//        String[] s=filePath.split("\\");
//        filePath=null;
//        filePath+=s[0];
//        for(int i=1;i<s.length;i++){
//            filePath=filePath+"\\"+s[i];
//        }
        System.out.print(ConsoleColors.BLUE + "Lecture No:");
        int lectureNo =sc.nextInt() ;     // Set as needed
            String sql = "INSERT INTO coursecontent (Course_Id, Lecture_No, Lecture_Content) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setInt(1, courseId);
                pstmt.setInt(2, lectureNo);

                try {
                    FileInputStream fis = new FileInputStream(filePath);
                    pstmt.setBinaryStream(3, fis, fis.available());
                    int rowsInserted = pstmt.executeUpdate();
                    System.out.println(ConsoleColors.BLUE + "Rows inserted: " + rowsInserted);
                } catch (IOException e) {
                    System.out.println(ConsoleColors.RED + "File IO error: " + e.getMessage());
                }


        // Change file path and details for your actual file
    }
    // 6. View status of request
    public void view(Connection con) throws Exception {
        String query = "SELECT r.request_Id, r.courseName, r.status " +
                "FROM request r " +
                "WHERE r.instructor_Id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, instructorId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(ConsoleColors.YELLOW +"Request ID: " + rs.getInt("request_Id") +
                    ConsoleColors.YELLOW + ", Course: " + rs.getString("courseName") +
                    ConsoleColors.YELLOW +", Status: " + rs.getString("status"));
        }
    }

    // 7. Logout
    public void logout() {
        System.out.println(" Logged out successfully!");
    }

    // 8. Delete account
    public void delete(Connection con,int id) throws Exception {
        String query = "DELETE FROM users WHERE user_Id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        int r = ps.executeUpdate();
        System.out.println(r > 0 ? "Instructor Account Deleted!" : "Instructor Account Deletion UnSuccssful!");
    }
        boolean checkCourse(Connection con,int courseId)throws Exception{
            String query="Select course_Id From course WHERE course_Id=? AND instructor_Id=?";
            PreparedStatement pst=con.prepareStatement(query);
            pst.setInt(1,courseId);
            pst.setInt(2,instructorId);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                return true;
            }else{
                System.out.println(ConsoleColors.RED + "No Such Course Exist");
                return false;
            }
        }
    public void InstructorFunctionality(Connection con,Scanner sc) throws Exception {
        System.out.println(ConsoleColors.RED_BOLD + "Logged In As Instructor!");
        DatabaseOperation obj=new Instructor(instructorId);
        boolean temp=true;
        do {
            System.out.println(ConsoleColors.BLUE + "\n====== INSTRUCTOR MENU ======");
            System.out.println(ConsoleColors.WHITE +"Enter 1 For View Assigned Students.");
            System.out.println(ConsoleColors.WHITE +"Enter 2 For Request to Admin.");
            System.out.println(ConsoleColors.WHITE +"Enter 3 For View Existing Uploaded Courses.");
            System.out.println(ConsoleColors.WHITE +"Enter 4 For Search Specific Course Performance.");
            System.out.println(ConsoleColors.WHITE +"Enter 5 For Upload Course Content.");
            System.out.println(ConsoleColors.WHITE +"Enter 6 For View Status of Request.");
            System.out.println(ConsoleColors.WHITE +"Enter 7 For Logout.");
            System.out.println(ConsoleColors.WHITE +"Enter 8 For Delete Account.");
            System.out.print(ConsoleColors.BLUE +"Enter Your Choice : ");
            try {
                int choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1:
                        viewAssignedStudents(con);
                        break;

                    case 2:
                        requestToAdmin(con,sc);
                        break;

                    case 3:
                        viewUploadedCourses(con);
                        break;

                    case 4:
                        System.out.print(ConsoleColors.BLUE + "Enter Course Name You Want to See : ");
                        String courseName = sc.nextLine();
                        String sql = "Select course_Id FROM course WHERE courseName LIKE ? AND instructor_Id = ?";
                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setString(1,"%"+courseName+"%");
                        ps.setInt(2,instructorId);
                        ResultSet rs = ps.executeQuery();
                        int course_id;
                        if(rs.next()){
                            course_id = rs.getInt(1);
                            searchCoursePerformance(con,course_id);
                        }else{
                            System.out.println(ConsoleColors.RED + "Wrong Course Name Enterred You Haven't Uploaded it!");
                        }
                        break;

                    case 5:
                        do{
                            System.out.print(ConsoleColors.BLUE + "Enter CourseId For Which You Want To Upload More Content:");
                            try{
                                int courseId=sc.nextInt();
                                sc.nextLine();
                                if(checkCourse(con,courseId)){
                                    System.out.println(ConsoleColors.YELLOW + "Course Found! Uploading Course Content");
                                    insert(con,courseId);
                                    break;
                                }
                            }catch(InputMismatchException w){
                                sc.nextLine();
                                System.out.println(ConsoleColors.RED + "Invalid Input");
                            }
                        }while(true);

                        break;

                    case 6:
                        obj.view(con);
                        break;

                    case 7:
                        logout();
                        temp=false;
                        break;

                    case 8:
                        obj.delete(con,instructorId);
                        return;

                    default:
                        System.out.println(ConsoleColors.RED + "Enter Appropriate Choice!");
                }
            }catch(InputMismatchException e){
                sc.nextLine();
                System.out.println(ConsoleColors.RED + "Invalid Input");
            }


        } while (temp);
    }
}
