package ru.sberbank.ManagementPoint;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class GenerateProducer {

    private static Producer producer;

    public static void init() throws UnknownHostException {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "config.properties";
        Properties prop = new Properties();

        try (InputStream input = new FileInputStream(appConfigPath)) {
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        prop.put("client.id", InetAddress.getLocalHost().getHostName());

        producer = new KafkaProducer(prop);
    }

    public static Producer getProducer() throws UnknownHostException {
        if (producer== null) {
            init();
            return producer;
        }
        return producer;
    }
}
