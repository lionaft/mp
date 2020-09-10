package ru.sberbank.ManagementPoint;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/receive")
public class ManagementPointReceive{

    @POST
    @Path("/data")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response ReceiveData(String jsonData) throws JsonParseException {

        JsonObject reseiveObject = new JsonObject();
        JsonObject responseObject = new JsonObject();

        reseiveObject = reseiveObject.getAsJsonObject(jsonData);
        responseObject.addProperty("Success", true);

        return Response.status(200).entity(responseObject.toString()).build();
    }
}