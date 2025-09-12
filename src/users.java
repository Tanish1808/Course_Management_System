public class users {
    private int user_Id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String role;
    private double salary;

    public int getUser_Id() {
        return user_Id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public double getSalary() {
        return salary;
    }

    public users(){

        }

    public users(int user_Id, String first_name, String last_name, String email, String password, String role, double salary) {
        this.user_Id = user_Id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.role = role;
        if(role.equalsIgnoreCase("Student")){
            this.salary = 0;
        }else{
            this.salary = salary;
        }
    }

    @Override
    public String toString () {
        return ConsoleColors.CYAN +"\nuser_Id=" + user_Id +
                ConsoleColors.CYAN +"\nfirst_name='" + first_name +
                ConsoleColors.CYAN + "\nlast_name='" + last_name +
                ConsoleColors.CYAN +"\nemail='" + email +
                ConsoleColors.CYAN +"\npassword='" + password +
                ConsoleColors.CYAN + "\nrole='" + role  +
                ConsoleColors.CYAN + "\nsalary=" + salary ;
    }

    public void dashboard() { System.out.println("Generic User Dashboard"); }
}
