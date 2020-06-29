## Purpose

Kafka Data Publisher's objective is to simulate the streaming environment using the user provided configurations in the publisherconfig.properties. It uses Apache Kafka Producer API to publish n messages asynchronously of type String into the input Kafka topic. This is to help load test streaming applications consumes from Kafka. Kafka Data Publisher can produce up to one million messages per second. 


## Getting Started

1. Requires JDK 1.8 or above
2. Built with Gradle 
3. Requires Kafka cluster with at least one broker to run the KafkaDataPublisher application


## Installation

```nginx
git clone -b master git@github.com:devaramv/kafka-data-publisher.git
cd into project-directory
gradle clean
gradle build 

```

## How to run

#### Non Kerberos clusters

```properties
Step 1:  Create a file publisherconfig.properties and copy the following contents into it

#Kafka-cluster-details

BOOTSTRAP_SERVERS_CONFIG=localhost:9092
KAFKA_TOPIC=kafka_publisher_topic

#Message Information

MESSAGE={"app-name":"kafka-datapublisher" }
NUMBER_OF_MESSAGES=100
NUMOFTASKS_FOR_PARALLELISM=2


#Logging constants

LOG_PATH=/u/user/publisherlogs/
LOGGING_PATTERN=%d %-5p [%c{1}] %m%n
CONSOLE_LOGGING_TARGET=System.out
APP_NAME=Kafka-Data-Publisher
LOG_EXTENSION = _LOGS.log



Step 2: Update  publishconfig.properties file with necessary properties corresponding to the environment. 

Step 3: Run the following java command to start the Kafka-Data-Publisher 
```

```shell
java -jar kafka-producer-1.0-SNAPSHOT.jar publisherconfig.properties
```

#### Kerberos Clusters

```shell
Kerberos clusters requires secure user authentication that can be achieved using jaas file. Follow the following steps to create jaas file.This process works to authenticate using security.protocol = SASL_PLAINTEXT.

Uncomment /* properties.put("security.protocol","SASL_PLAINTEXT"); */ in ProducerWithThreads.java class  

Step 1: Create a file kafkaauthorization.jaas

Step 2: Copy the following contents given below into the file kafkaauthorization.jaas by replacing appropriate values for kerberos principal,username with the id running the application

 KafkaClient {

 com.sun.security.auth.module.Krb5LoginModule required

 doNotPrompt=true

 useTicketCache=true

 principal="username@DOMAIN.COM"

 useKeyTab=true

 serviceName="kafka"

 keyTab="/u/username/username.keytab"

 client=true;

};

Client {

 com.sun.security.auth.module.Krb5LoginModule required

 doNotPrompt=true

 useTicketCache=true

 principal="username@DOMAIN.COM"

 useKeyTab=true

 serviceName="zookeeper"

 keyTab="/u/username/username.keytab"

 client=true;

};


```

#####  Run the following command to start producer using SASL_PLAINTEXT

```java
java -Djava.security.auth.login.config==/u/username/kafkaauthorization.jaas -jar kafka-producer-1.0-SNAPSHOT.jar publisherconfig.properties
```

## After run testing

Run the following set of commands before and after running publisher. Lag column indicates the number of message KafkaDataPublisher app produced. Total sum of messages in the Lag column must equal the number of message produced 

```shell
echo "security.protocol=SASL_PLAINTEXT">/tmp/consumergroupsecurity.prop

/usr/hdp/current/kafka-broker/bin/kafka-consumer-groups.sh --bootstrap-server loclhost:9092 --group kafka-publisher-test --describe --command-config /tmp/consumergroupsecurity.prop
```

Run the Kafka console consumer to consume all the messages published in the previous run using the same group id  as above

```shell
/usr/hdp/current/kafka-broker/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092  --topic kafka-publisher-test1-lz --group kafka-publisher-test  --consumer-property security.protocol=SASL_PLAINTEXT
```
