package ru.ncedu.bestgroup.mailing;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class MailConsumer {
    private BlockingQueue<Mail> queue;

    public MailConsumer(BlockingQueue<Mail> queue) {
        this.queue = queue;
    }

    public Collection<Mail> consumeMail() throws InterruptedException {
        Set<Mail> mailSet = new LinkedHashSet();
        for(Mail e:queue) {
            mailSet.add(e);
        }
        return mailSet;
    }

    public void sendAllMail(Collection<Mail> mailCollection) throws EmailException {
        for (Mail e:mailCollection){
            HtmlEmail email = new HtmlEmail();
            email.setHostName("");
            email.addTo(e.getTo());
            email.setFrom("");
            email.setSubject(e.);
            email.setTextMsg(e.getBody());
            email.send();
        }
    }
}
