package com.streamingtools.kafka.properties;

import com.streamingtools.kafka.logging.DataProducerLogger;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class GetConfigurations {

    private static Logger logger = Logger.getLogger(GetConfigurations.class);

    private static InputStream stream;
    private static String BOOTSTRAP_SERVERS_CONFIG;
    private static String KAFKA_TOPIC;
    private static String MESSAGE;
    private static int NUMBER_OF_MESSAGES;
    private static int NUMOFTASKS_FOR_PARALLELISM;
    private static String LOG_PATH;
    private static String LOGGING_PATTERN;
    private static String CONSOLE_LOGGING_TARGET;
    private static String APP_NAME;
    private static String LOG_EXTENSION;


    public GetConfigurations(String configFilePath){
        try{
            stream = new FileInputStream(new File(configFilePath));
            loadProperties(stream);
            new DataProducerLogger();



        }catch (IOException e){
            logger.error("Exception encountered while reading input config file" + e.getMessage());

        }

    }

    /*
       Getting the property values from the input config file
    */
   private static void loadProperties(InputStream stream) throws IOException {

        Properties properties = new Properties();
        properties.load(stream);

       BOOTSTRAP_SERVERS_CONFIG=properties.getProperty("BOOTSTRAP_SERVERS_CONFIG");
       KAFKA_TOPIC=properties.getProperty("KAFKA_TOPIC");
       MESSAGE= properties.getProperty("MESSAGE");
       NUMBER_OF_MESSAGES=Integer.parseInt(properties.getProperty("NUMBER_OF_MESSAGES"));
       NUMOFTASKS_FOR_PARALLELISM=Integer.parseInt(properties.getProperty("NUMOFTASKS_FOR_PARALLELISM"));
       LOG_PATH=properties.getProperty("LOG_PATH");
       LOGGING_PATTERN=properties.getProperty("LOGGING_PATTERN");
       CONSOLE_LOGGING_TARGET=properties.getProperty("CONSOLE_LOGGING_TARGET");
       APP_NAME=properties.getProperty("APP_NAME");
       LOG_EXTENSION=properties.getProperty("LOG_EXTENSION");

    }

    public static String getLogPath() {
        return LOG_PATH;
    }

    public static String getBootstrapServersConfig() {
        return BOOTSTRAP_SERVERS_CONFIG;
    }

    public static String getKafkaTopic() {
        return KAFKA_TOPIC;
    }

    public static String getRECORD() {
        return MESSAGE;
    }

    public static int getNumberOfMessages() {
        return NUMBER_OF_MESSAGES;
    }

    public static int getNumoftasksForParallelism() {
        return NUMOFTASKS_FOR_PARALLELISM;
    }

    public static String getLoggingPattern() {
        return LOGGING_PATTERN;
    }

    public static String getConsoleLoggingTarget() {
        return CONSOLE_LOGGING_TARGET;
    }

    public static String getAppName() {
        return APP_NAME;
    }

    public static String getLogExtension() {
        return LOG_EXTENSION;
    }


}
