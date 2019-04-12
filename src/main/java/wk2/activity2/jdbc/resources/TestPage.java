package wk2.activity2.jdbc.resources;

import wk2.activity2.jdbc.logger.ServiceLogger;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("test")
public class TestPage {
    @Path("hello")
    @GET
    public Response helloWorld() {
        ServiceLogger.LOGGER.info("Hello world!");
        return Response.status(Status.OK).build();
    }
}
