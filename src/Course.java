import java.time.format.DateTimeFormatter;

public class Course {
    int courseId;
    String courseName;
    String courseDiscription;
    DateTimeFormatter CourseDuration;
    String category;
    double price;
    /* DateTimeFormatter duration = DateTimeDuration.ofPattern()*/
    /* Duration mins = Duration.ofMinutes(5) : Also for Hours and Seconds */

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseDiscription(String courseDiscription) {
        this.courseDiscription = courseDiscription;
    }

    public void setCourseDuration(DateTimeFormatter courseDuration) {
        CourseDuration = courseDuration;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return ConsoleColors.CYAN + "Course " +
                ConsoleColors.CYAN + "\ncourseId=" + courseId +
                ConsoleColors.CYAN +  "\ncourseName='" + courseName +
                ConsoleColors.CYAN +  "\ncourseDiscription='" + courseDiscription +
                ConsoleColors.CYAN +  "\ncategory='" + category + '\'' +
                ConsoleColors.CYAN +  "\nprice=" + price +"\n";
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseDiscription() {
        return courseDiscription;
    }

    public DateTimeFormatter getCourseDuration() {
        return CourseDuration;
    }

    public String getCategory() {
        return category;
    }

    public Course(int courseId, String courseName,double price) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.price=price;
    }

    public double getPrice() {
        return price;
    }

    public Course(int courseId, String courseName, String courseDiscription,  String category, double price) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDiscription = courseDiscription;
        this.category = category;
        this.price = price;
    }
}