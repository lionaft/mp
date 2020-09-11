package ru.sberbank.ManagementPoint;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.sberbank.meta.logging.MainLogger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.UnknownHostException;

@Path("/receive")
public class ManagementPointReceive{

    @POST
    @Path("/data")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response ReceiveData(String jsonData) throws JsonParseException, UnknownHostException {

        JsonObject reseiveObject = new JsonObject();
        JsonObject responseObject = new JsonObject();

        reseiveObject = reseiveObject.getAsJsonObject(jsonData);
        responseObject.addProperty("Success", true);
        GenerateProducer.getProducer();
        MainLogger.info("MP", jsonData);
        MainLogger.info("MP", reseiveObject.toString());
        SendToKafka(reseiveObject);

        return Response.status(200).entity(responseObject.toString()).build();
    }

    private void SendToKafka(JsonObject json) {
        final ProducerRecord record = new ProducerRecord("DataWriterQueue", json.get("type"), json.toString());
        try {
            Producer prod = GenerateProducer.getProducer();
            prod.send(record, (metadata, e) -> {
                if (e != null)
                    MainLogger.info("MP", String.format("Send failed for record %s : %s", record.toString(), e.toString()));
                    MainLogger.error("MP", e);
            });
        }
        catch (Exception ex){
            MainLogger.error("MP", ex);
        }

    }
}