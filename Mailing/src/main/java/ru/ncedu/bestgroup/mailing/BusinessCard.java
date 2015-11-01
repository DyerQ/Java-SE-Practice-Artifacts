package ru.ncedu.bestgroup.mailing;

import java.util.*;


public class BusinessCard {
    private String mail;
    private Map<String, String> properties = new HashMap<String, String>();

    public BusinessCard(String mail, Map<String, String> properties) {
        if (mail == null) {
            throw new IllegalArgumentException("Mail is null!");
        }
        this.mail = mail;
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        return !(o == null || getClass() != o.getClass()) && mail.equals(((BusinessCard) o).getMail());

    }

    @Override
    public int hashCode() {
        return mail.hashCode();
    }

    public String getMail() {
        return mail;
    }

    public String getProperty(String key) {
        return properties.get(key);
    }
    public Set<String> getKeySet(){
        return properties.keySet();
    }
}
