package com.streamingtools.kafka.logging;

import com.streamingtools.kafka.properties.GetConfigurations;
import org.apache.log4j.*;


public class DataProducerLogger {

    public DataProducerLogger(){

        /* Instantiating consoleAppender with PatternLayout,
        and Target set to System.out*/

        ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout(GetConfigurations.getLoggingPattern()),
                GetConfigurations.getConsoleLoggingTarget());
        consoleAppender.setName(GetConfigurations.getAppName()); // Sets the name of the logging tool
        consoleAppender.setThreshold(Level.INFO);
        consoleAppender.activateOptions();
        Logger.getRootLogger().addAppender(consoleAppender);


        //File Appender

        FileAppender fileAppender = new FileAppender();

        fileAppender.setFile(GetConfigurations.getLogPath()
        + GetConfigurations.getAppName()
        + GetConfigurations.getLogExtension());


        fileAppender.setLayout(new PatternLayout(GetConfigurations.getLoggingPattern()));
        fileAppender.setThreshold(Level.INFO);

        fileAppender.activateOptions();
        Logger.getRootLogger().addAppender(fileAppender);


    }
}
