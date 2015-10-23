package ru.ncedu.bestgroup.mailing;

import java.util.HashMap;
import java.util.Map;


public class BusinessCard {
    private String mail;
    private Map<String, String> properties = new HashMap<String, String>();

    public String getMail() {
        return mail;
    }

    public String getProperty(String key) {
        return properties.get(key);
    }
}
