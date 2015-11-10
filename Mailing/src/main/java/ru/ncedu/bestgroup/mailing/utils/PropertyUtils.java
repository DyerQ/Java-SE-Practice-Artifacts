package ru.ncedu.bestgroup.mailing.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtils.class);

    private static final String DEFAULT_CONFIG_DIRECTORY = "config";
    private static final String DEFAULT_APP_PROPERTIES_FILE_NAME = "application.properties";
    private static final String DEFAULT_CARD_PROPERTIES_FILE_NAME = "default.properties";
    private static final String DEFAULT_MAIL_SENDING_PROPERTIES_FILE_NAME = "mail-sending.properties";

    public static Properties loadApplicationProperties() throws IOException{
        return loadProperties(DEFAULT_APP_PROPERTIES_FILE_NAME);
    }

    public static Properties loadDefaulCardProperties() throws IOException{
        return loadProperties(DEFAULT_CARD_PROPERTIES_FILE_NAME);
    }

    public static Properties loadMailSendingProperties() throws IOException{
        return loadProperties(DEFAULT_MAIL_SENDING_PROPERTIES_FILE_NAME);
    }

    public static Properties loadNamedProperties(String fileName) throws IOException{
        return loadProperties(fileName);

    }

    private static Properties loadProperties(String fileName){
        Properties properties = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(new File(DEFAULT_CONFIG_DIRECTORY, fileName));
            properties.load(in);
            in.close();
            return properties;
        }catch (IOException e) {
            LOGGER.error("Error while loading file",e);
            return  properties;
        }
    }


}
