package ru.sberbank.ManagementPoint;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import ru.sberbank.meta.logging.MainLogger;
import ru.sberbank.meta.logging.MainLoggerFileHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.Level;

public class GenerateProducer {

    private static Producer<String,String> producer;

    public static void init() throws UnknownHostException {
        Properties prop = new Properties();

        MainLogger.setLevel(Level.ALL);
        try {
            MainLogger.registerLogger(new MainLoggerFileHandler());
        } catch (IOException e) {
            MainLogger.error("MP", e);
        }

        try (InputStream input = GenerateProducer.class.getClassLoader().getResourceAsStream("config.properties")) {
            prop.load(input);
        } catch (IOException ex) {
            MainLogger.error("MP", ex);
        }

        prop.put("client.id", InetAddress.getLocalHost().getHostName());

        producer = new KafkaProducer<>(prop);
    }

    public synchronized static Producer getProducer() throws UnknownHostException {
        if (producer== null) {
            init();
            return producer;
        }
        return producer;
    }
}
