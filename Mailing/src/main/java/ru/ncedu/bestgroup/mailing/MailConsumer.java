package ru.ncedu.bestgroup.mailing;

import org.apache.commons.mail.*;
import ru.ncedu.bestgroup.mailing.model.Mail;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class MailConsumer {
    private BlockingQueue<Mail> queue;
    private final static String HOST_NAME = "smtp.gmail.com";
    private final static int SMTP_PORT = 465;
    private final static String PASSWORD= "bestgroup";
    private final static String LOGIN = "testing.bestgroup@gmail.com";



    public MailConsumer(BlockingQueue<Mail> queue) {
        this.queue = queue;
    }

    public Collection<Mail> consumeMail() throws InterruptedException {
        Set<Mail> mailSet = new LinkedHashSet<Mail>();
        for(Mail e:queue) {
            mailSet.add(e);
        }
        return mailSet;
    }

    public void sendAllMail(Collection<Mail> mailCollection) throws EmailException {
        for (Mail e:mailCollection){
            HtmlEmail email = new HtmlEmail();
            email.setHostName(HOST_NAME);
            email.setSmtpPort(SMTP_PORT);
            email.setSSLOnConnect(true);
            email.setAuthenticator(new DefaultAuthenticator(LOGIN,PASSWORD));
            email.addTo(e.getTo());
            email.setFrom(LOGIN);
            email.setSubject(e.getSubject());
            email.setTextMsg(e.getBody());
            email.send();

        }
    }
}
