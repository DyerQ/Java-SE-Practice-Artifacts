package ru.ncedu.bestgroup.mailing;

public class Mail {
    private String to;
    private String body;
    private String subject;

    public String getSubject() {
        return subject;
    }

    public Mail(String to, String body, String subject) {
        this.to = to;
        this.body = body;
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public String getBody() {
        return body;
    }
}
