package wk2.activity2.jdbc.core;

public class Student {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private float GPA;

    public Student(int id, String email, String firstName, String lastName, float GPA) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.GPA = GPA;
    }

    boolean isGoodStudent() {
        return GPA > 3.0;
    }

    public String toString() {
        return "ID: " + id + ", email: " + email + ", first name: " + firstName + ", last name: " + lastName + ", gpa: " + GPA;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public float getGPA() {
        return GPA;
    }

    public void setGPA(float GPA) {
        this.GPA = GPA;
    }
}
