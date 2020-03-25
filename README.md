Kafka Data Publisher's objective is to simulate the streaming environment using the user provided configurations in the publisherconfig.properties. It uses Apache Kafka Producer API to publish n messages asynchronously of type String into the input Kafka topic. This is to help load test streaming applications consumes from kafka. Kafka Data Publisher can produce up to one million messages per second. 


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
Step 1:  Create a file publisheconfig.properties and copy the following contents into it

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

Step 3. Run the following java command to start the Kafka-Data-Publisher 
```

```shell
java -jar kafka-producer-1.0-SNAPSHOT.jar publisherconfig.properties
```

```shell

```
