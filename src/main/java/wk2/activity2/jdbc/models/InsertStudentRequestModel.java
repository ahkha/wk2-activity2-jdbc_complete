package wk2.activity2.jdbc.models;

import wk2.activity2.jdbc.core.Validate;

public class InsertStudentRequestModel implements Validate {
    private String email;
    private String firstName;
    private String lastName;
    private float gpa;

    public InsertStudentRequestModel() { }

    public InsertStudentRequestModel(String email, String firstName, String lastName, float gpa) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gpa = gpa;
    }


    @Override
    public boolean isValid() {
        int emailLen = email.length();
        int firstNameLen = firstName.length();
        int lastNameLen = lastName.length();

        return (emailLen > 0 && emailLen <= 50) &&
                (firstNameLen > 0 && firstNameLen <= 50) &&
                (lastNameLen > 0 && lastNameLen <= 50) &&
                (gpa >= 0 && gpa <= 4.0);
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

    public float getgpa() {
        return gpa;
    }

    public void setgpa(float gpa) {
        this.gpa = gpa;
    }
}
