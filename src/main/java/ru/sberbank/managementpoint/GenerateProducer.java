package ru.sberbank.managementpoint;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import ru.sberbank.meta.logging.MainLogger;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.Level;

public class GenerateProducer {

    private static Producer<String,String> producer;

    private static void init() throws UnknownHostException {
        Properties prop = new Properties();

        MainLogger.setLevel(Level.ALL);

        try (InputStream input = GenerateProducer.class.getClassLoader().getResourceAsStream("config.properties")) {
            prop.load(input);
        } catch (IOException ex) {
            MainLogger.error("MP", ex);
        }
        prop.put("client.id", InetAddress.getLocalHost().getHostName());

        producer = new KafkaProducer<>(prop);
    }

    public static synchronized Producer getProducer() throws UnknownHostException {
        if (producer== null) {
            init();
            return producer;
        }
        return producer;
    }
}
