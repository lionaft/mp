package ru.sberbank.ManagementPoint;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import ru.sberbank.meta.logging.MainLogger;

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

    private void SendToKafka(JsonObject json) {
        final ProducerRecord record = new ProducerRecord("DataWriterQueue", json.get("type"), json.toString());
        try {
            Producer prod = GenerateProducer.getProducer();
            GenerateProducer.getProducer().send(record, new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    if (e != null)
                        MainLogger.info("MP", String.format("Send failed for record %s : %s", record.toString(), e.toString()));
                        MainLogger.error("MP", e);
                }
            });
        }
        catch (Exception ex){
            MainLogger.error("MP", ex);
        }

    }
}