package wk2.activity2.jdbc.models;

import wk2.activity2.jdbc.core.Student;
import wk2.activity2.jdbc.core.Validate;
import wk2.activity2.jdbc.logger.ServiceLogger;

import java.util.ArrayList;

public class GetStudentResponseModel implements Validate {
    private StudentModel[] students;

    public GetStudentResponseModel() { }

    private GetStudentResponseModel(StudentModel[] students) {
        this.students = students;
    }

    public static GetStudentResponseModel buildModelFromList(ArrayList<Student> students) {
        // Jackson cannot convert complex data structures to text. It can convert arrays of objects.
        // Must convert arraylist to array.
        ServiceLogger.LOGGER.info("Creating model...");

        if (students == null) {
            ServiceLogger.LOGGER.info("No student list passed to model constructor.");
            return new GetStudentResponseModel(null);
        }

        ServiceLogger.LOGGER.info("Students list is not empty...");
        int len = students.size();
        StudentModel[] array = new StudentModel[len];

        for (int i = 0; i < len; ++i) {
            ServiceLogger.LOGGER.info("Adding student " + students.get(i).getEmail() + " to array.");
            // Convert each student in the arraylist to a StudentModel
            StudentModel sm = StudentModel.buildModelFromObject(students.get(i));
            // If the new model has valid data, add it to array
            if (sm.isValid()) {
                array[i] = sm;
            }
        }
        ServiceLogger.LOGGER.info("Finished building model. Array of students contains: ");
        for (StudentModel sm : array) {
            ServiceLogger.LOGGER.info("\t" + sm);
        }
        return new GetStudentResponseModel(array);
    }

    public StudentModel[] getStudents() {
        return students;
    }

    @Override
    public boolean isValid() {
        // If students[] is null, return false
        ServiceLogger.LOGGER.info("students == null ? " + (students == null));
        ServiceLogger.LOGGER.info("students.length > 0 ? " + (students.length > 0));
        return (students != null) || (students.length == 0);
    }
}
