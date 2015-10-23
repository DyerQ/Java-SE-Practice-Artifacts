package ru.ncedu.bestgroup.mailing;

import java.util.Properties;


public class MailTemplate {
    private String body;

    public String getBody() {
        return body;
    }

    public  Mail getMail(BusinessCard card, Properties defaultProperties){
       return new Mail();
    }
}

