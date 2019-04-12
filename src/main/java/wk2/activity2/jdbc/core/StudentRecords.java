package wk2.activity2.jdbc.core;

import wk2.activity2.jdbc.BasicService;
import wk2.activity2.jdbc.logger.ServiceLogger;
import wk2.activity2.jdbc.models.GetStudentRequestModel;
import wk2.activity2.jdbc.models.GetStudentResponseModel;
import wk2.activity2.jdbc.models.InsertStudentRequestModel;
import wk2.activity2.jdbc.models.StudentModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentRecords {
    public static GetStudentResponseModel retrieveStudentsFromDB(String name) {
        GetStudentResponseModel responseModel;

        try {
            // Construct the query
            String query = "SELECT id, email, firstName, lastName, GPA FROM students " +
                    "WHERE firstName LIKE ? OR lastName LIKE ?;";

            // Create the prepared statement
            PreparedStatement ps = BasicService.getCon().prepareStatement(query);

            // Set the arguments
            ps.setString(1, name);
            ps.setString(2, name);
            // Save the query result to a Result Set so records may be retrieved
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            // Retrieve the students from the Result Set
            ArrayList<Student> students = new ArrayList<>();
            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getFloat("GPA")
                );
                ServiceLogger.LOGGER.info("Retrieved student " + s);
                students.add(s);
            }

            // Got the students. Builds the response model
            return GetStudentResponseModel.buildModelFromList(students);
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve student records.");
            e.printStackTrace();
        }
        // No students were retrieved. Build response model with null arraylist.
        ServiceLogger.LOGGER.info("No students were retrieved.");
        return GetStudentResponseModel.buildModelFromList(null);
    }

    public static boolean insertStudentsToDb(InsertStudentRequestModel requestModel) {
        ServiceLogger.LOGGER.info("Inserting student into database...");
        try {
            // Construct the query
            String query =
                    "INSERT INTO students (email, firstName, lastName, GPA) VALUES (?, ?, ?, ?);";
            // Create the prepared statement
            PreparedStatement ps = BasicService.getCon().prepareStatement(query);
            // Set the paremeters
            ps.setString(1, requestModel.getEmail());
            ps.setString(2, requestModel.getFirstName());
            ps.setString(3, requestModel.getLastName());
            ps.setFloat(4, requestModel.getgpa());
            // Execute query
            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ps.execute();
            return true;
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Unable to insert student " + requestModel.getEmail());
            e.printStackTrace();
        }
        return false;
    }
}


