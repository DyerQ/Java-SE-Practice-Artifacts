package ru.ncedu.bestgroup.mailing;

import java.util.Properties;
import java.util.regex.Pattern;


public class MailTemplate {
    private String body;
    private String subject;
    public MailTemplate(String body, String subject){
        this.body = body;
        this.subject = subject;
    }
    public String getBody() {
        return body;
    }
    public String getSubject(){
        return subject;         //comment for commit
    }

    public Mail getMail(BusinessCard card, Properties defaultProperties) {

        for (String s: defaultProperties.stringPropertyNames()){
            String  replacement = defaultProperties.getProperty(card.getProperty(s));
            body = body.replaceAll("#\\{"+ s +"\\}",replacement);
        }
        return new Mail(card.getMail(),body);
    }
}

