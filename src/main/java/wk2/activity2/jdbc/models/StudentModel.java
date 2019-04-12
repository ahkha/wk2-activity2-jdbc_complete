package wk2.activity2.jdbc.models;

import wk2.activity2.jdbc.core.Student;
import wk2.activity2.jdbc.core.Validate;

public class StudentModel implements Validate {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private float gpa;

    // More secure for constructors to be private.
    private StudentModel() { }

    // More secure for constructors to be private.
    private StudentModel(int id, String email, String firstName, String lastName, float gpa) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gpa = gpa;
    }

    // Using a factory method to create an instance of StudentModel is more secure because it gives
    // ONLY YOU control over how the object is instantiated.
    public static StudentModel buildModelFromObject(Student s) {
        return new StudentModel(s.getId(), s.getEmail(), s.getFirstName(), s.getLastName(), s.getGPA());
    }

    public String toString() {
        return "ID: " + id + ", email: " + email + ", first name: " + firstName + ", last name: " + lastName + ", gpa: " + gpa;
    }

    @Override
    // Used to ensure that all data fields within the model contain valid values. If an HTTP request
    // comes to the server, you'll use Jackson to validate that the request has a proper format. But
    // you need to manually check that the values actually sent in the request are valid. By making
    // your model classes implement the Validate interface, you're forcing all models to contain a
    // function isValid() that should do the data validation. You don't HAVE to do data validation
    // in this manner, however. This is just one of MANY ways to do it.
    public boolean isValid() {
        return (email != null) &&
                (firstName != null) &&
                (lastName != null) &&
                (gpa >= 0) &&
                (gpa <= 4.0);
    }

    // This model only has getters because the information contained within a model should not be changed.
    // If you need to change data fields, convert it to a Student object.
    // When creating a model from a request, the model itself should not be changed so that you don't
    // mistakenly change the data from the request.

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public float getgpa() {
        return gpa;
    }
}
