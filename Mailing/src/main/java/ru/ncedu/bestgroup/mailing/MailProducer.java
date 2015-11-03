package ru.ncedu.bestgroup.mailing;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public class MailProducer {
    private BlockingQueue<Mail> queue;
    private Properties defaultProperties;

    public MailProducer(BlockingQueue<Mail> queue, Properties defaultProperties) {
        this.queue = queue;
        this.defaultProperties = defaultProperties;
    }

    public Collection<Mail> buildMail(Collection<MailTemplate> templates, Set<BusinessCard> businessCards) throws Exception {
        Collection<Mail> result = new ArrayList<Mail>();

        for (MailTemplate template : templates) {
            for (BusinessCard businessCard : businessCards) {
                result.add(template.getMail(businessCard, defaultProperties));
            }
        }
        return result;
    }

    public void enqueueMail(Collection<Mail> mails) throws Exception {
        queue.addAll(mails);
    }
}
