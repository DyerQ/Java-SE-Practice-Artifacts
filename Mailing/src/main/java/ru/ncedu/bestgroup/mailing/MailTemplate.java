package ru.ncedu.bestgroup.mailing;

import java.util.Properties;
import java.util.regex.Pattern;


public class MailTemplate {
    private String body;
    public MailTemplate(String body){
        this.body = body;
    }
    public String getBody() {
        return body;
    }

    public Mail getMail(BusinessCard card, Properties defaultProperties) {


        for (String s: defaultProperties.stringPropertyNames()){
            String  replacement = defaultProperties.getProperty(card.getProperty(s));
            body = body.replaceAll("#\\{"+ s +"\\}",replacement);
        }
        return new Mail(card.getMail(),body);
    }
}

