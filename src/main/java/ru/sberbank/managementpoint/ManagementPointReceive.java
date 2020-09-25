package ru.sberbank.managementpoint;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.sberbank.constants.JsonTypes;
import ru.sberbank.constants.KafkaTopics;
import ru.sberbank.constants.json_fields.Common;
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

        JsonObject receiveObject = new Gson().fromJson(jsonData, JsonObject.class);
        JsonObject responseObject = new JsonObject();

        responseObject.addProperty("Success", true);
        GenerateProducer.getProducer();
        SendToKafka(receiveObject);

        return Response.status(200).entity(responseObject.toString()).build();
    }

    private void SendToKafka(JsonObject json) {
        ProducerRecord<String, String> record = GetProducerRecord(json);
        try {
            Producer prod = GenerateProducer.getProducer();
            prod.send(record, (metadata, e) -> {
                if (e != null) {
                    MainLogger.info("MP", String.format("Send failed for record %s : %s", record.toString(), e.toString()));
                    MainLogger.error("MP", e);
                }
            });
        }
        catch (Exception ex){
            MainLogger.error("MP", ex);
        }
    }

    private ProducerRecord<String,String> GetProducerRecord(JsonObject json) {
        String type = json.get(Common.TYPE).getAsString();
        switch (type.toLowerCase()) {
            case JsonTypes.INVENTARIZATION:
                return new ProducerRecord<>(KafkaTopics.INVENTORYPOINT, type, json.toString());
            default:
                return new ProducerRecord<>(KafkaTopics.DATAWRITER, type, json.toString());
        }
    }
}