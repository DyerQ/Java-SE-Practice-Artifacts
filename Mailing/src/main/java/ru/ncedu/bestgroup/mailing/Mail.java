package ru.ncedu.bestgroup.mailing;

public class Mail {
    private String to;
    private String body;

    public Mail(String to, String body) {
        this.to = to;
        this.body = body;
    }

    public String getTo() {
        return to;
    }

    public String getBody() {
        return body;
    }
}
