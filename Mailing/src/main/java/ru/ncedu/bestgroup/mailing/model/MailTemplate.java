package ru.ncedu.bestgroup.mailing.model;

import java.util.Properties;


public class MailTemplate {
    private String body;
    private String subject;

    public MailTemplate(String body, String subject) {
        this.body = body;
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public String getSubject() {
        return subject;         //comment for commit
    }

    public Mail getMail(BusinessCard card, Properties defaultProperties) {
        for (String s : card.getKeySet()) {
            String replacement = card.getProperty(card.getProperty(s));
            body = body.replaceAll("#\\{" + s + "\\}", replacement);
        }
        for (String s : defaultProperties.stringPropertyNames()) {
            String replacement = defaultProperties.getProperty(defaultProperties.getProperty(s));
            body = body.replaceAll("#\\{" + s + "\\}", replacement);
        }
        return new Mail(card.getMail(), body, subject);
    }
}

