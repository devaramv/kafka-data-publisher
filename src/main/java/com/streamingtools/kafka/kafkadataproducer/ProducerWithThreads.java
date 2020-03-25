package com.streamingtools.kafka.kafkadataproducer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;


import java.util.Properties;

public class ProducerWithThreads implements Runnable {

    private static final Logger logger = Logger.getLogger(ProducerWithThreads.class);
    private int numTasks;
    private int numberOfMessages;
    private String messageTobePublished;
    private int eachTaskMessages;
    private int leftOverMessagesAfterSplit;
    private boolean isSplitWorkEven;
    private static String bootstrapConfig;
    private String topicName;


    public ProducerWithThreads(int numTasks, String message, int numberofMessages,
                               String bootstrapconfig, String topicName) {
        this.numTasks = numTasks;
        this.numberOfMessages = numberofMessages;
        this.messageTobePublished = message;
        this.bootstrapConfig = bootstrapconfig;
        this.topicName = topicName;

    }

    private static Properties setProducerProperties() {

        logger.info("Setting the producer properties for " + Thread.currentThread().getName() );
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapConfig);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
      /*  properties.put("security.protocol","SASL_PLAINTEXT"); /* This property is specific to kerberos enabled
                                                               kafka clusters. This property can be disabled if running
                                                               in non kerberos clusters */
        return properties;

    }

    public void run() {

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(setProducerProperties());
        try{

            int messagePublishCounter = 0;
            while (true) {
                ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topicName,
                        messageTobePublished);
                kafkaProducer.send(producerRecord, new getNotificationViaCallback());
                messagePublishCounter++;
                if (messagePublishCounter == numberOfMessages) {
                    logger.info("Total no of records produced by " + Thread.currentThread().getName()+ " are " +
                            messagePublishCounter);
                    break;
                }

            }
            kafkaProducer.flush();
        }finally {
            logger.info("Closing the thread " + Thread.currentThread().getName());
            kafkaProducer.close();


        }


    }
    private static class getNotificationViaCallback implements Callback {

        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e == null) {
//                logger.info("This message has partition no  " + recordMetadata.partition() + " " +
//                        "and offset  " + recordMetadata.offset() );
            } else {
                e.printStackTrace();
            }
        }
    }

    public void spinThreads() {
        splitMessagesBetweenTasks();

        if(isSplitWorkEven == true){
            executeTasks(numTasks,eachTaskMessages);

        }else{
            int oddMessages = eachTaskMessages+leftOverMessagesAfterSplit;
            executeTasks(numTasks-1,eachTaskMessages);
            executeTasks(1,oddMessages);

        }

    }


    private void splitMessagesBetweenTasks(){
        /* Minimum num of tasks required are one
        *  If numTasks is not specified by default it will be one and
        * numTasks cannot be negative  */

        if(numTasks<=0){
            eachTaskMessages = this.numberOfMessages;
        }else{
            if(numberOfMessages%numTasks==0){
                eachTaskMessages = numberOfMessages/numTasks;
                isSplitWorkEven = true;
            }else{
                leftOverMessagesAfterSplit = numberOfMessages%numTasks;
                eachTaskMessages = numberOfMessages/numTasks;
                isSplitWorkEven =false;
            }
        }


    }


    private void executeTasks(int numTasks,int taskMessages){
        for(int i = 1; i<=numTasks; i++) {
            String ThreadName = "Producer Thread " + i + taskMessages;
            Thread producerThread = new Thread(new ProducerWithThreads(numTasks, messageTobePublished,taskMessages,
                    bootstrapConfig,topicName)
                    ,ThreadName);
            producerThread.start();
            logger.info("The thread " + producerThread.getName() + " has started \n");
        }
    }

}
