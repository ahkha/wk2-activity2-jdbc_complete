package wk2.activity2.jdbc.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import wk2.activity2.jdbc.core.StudentRecords;
import wk2.activity2.jdbc.logger.ServiceLogger;
import wk2.activity2.jdbc.models.GetStudentResponseModel;
import wk2.activity2.jdbc.models.InsertStudentRequestModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("student")
public class StudentsPage {
    @Path("get")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentsRequest(@QueryParam("name") String name) {
        ServiceLogger.LOGGER.info("Received request for students.");
        ObjectMapper mapper = new ObjectMapper();
        GetStudentResponseModel responseModel;

        ServiceLogger.LOGGER.info("Retrieve students with name like " + name);
        responseModel = StudentRecords.retrieveStudentsFromDB(name);
        ServiceLogger.LOGGER.info("Students retrieved.");

        if (responseModel.isValid()) {
            ServiceLogger.LOGGER.info("ResponseModel data is valid.");
            return Response.status(Status.OK).entity(responseModel).build();
        } else {
            ServiceLogger.LOGGER.info("ResponseModel data is invalid.");
            return Response.status(Status.NO_CONTENT).entity(responseModel).build();
        }

    }

    @Path("insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertStudentRequest(String jsonText) {
        ServiceLogger.LOGGER.info("Received request to insert student into DB.");
        ServiceLogger.LOGGER.info(jsonText);
        ObjectMapper mapper = new ObjectMapper();
        InsertStudentRequestModel requestModel;

        try {
            // Map the JSON to an appropriate request model
            requestModel = mapper.readValue(jsonText, InsertStudentRequestModel.class);

            // Verify the data in the request is valid
            if (!requestModel.isValid()) {
                return Response.status(Status.BAD_REQUEST).build();
            }

            // Insert the student into the database
            if (StudentRecords.insertStudentsToDb(requestModel)) {
                // Insertion was successful
                return Response.status(Status.OK).build();
            } else {
                // Insertion failed
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
            }
            return Response.status(Status.BAD_REQUEST).build();
        }
    }
}
