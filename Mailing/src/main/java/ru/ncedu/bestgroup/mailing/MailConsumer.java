package ru.ncedu.bestgroup.mailing;

import org.apache.commons.mail.*;
import ru.ncedu.bestgroup.mailing.model.Mail;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class MailConsumer {
    private BlockingQueue<Mail> queue;

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
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(465);
            email.setSSLOnConnect(true);
            email.setAuthenticator(new DefaultAuthenticator("testing.bestgroup","bestgroup"));
            email.addTo(e.getTo());
            email.setFrom("testing.bestgroup@gmail.com");
            email.setSubject(e.getSubject());
            email.setTextMsg(e.getBody());
            email.send();

        }
    }
}
