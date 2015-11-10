package ru.ncedu.bestgroup.mailing.utils;

import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncedu.bestgroup.mailing.MailConsumer;
import ru.ncedu.bestgroup.mailing.model.Mail;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Илья on 06.11.2015.
 */
public class MailConsumerRunnable implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailConsumerRunnable.class);

    private volatile BlockingQueue<Mail> queue;

    public MailConsumerRunnable(BlockingQueue<Mail> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        MailConsumer mailConsumer = new MailConsumer(queue);
        Collection<Mail> mails = null;
        try {
            mails = mailConsumer.consumeMail();
        } catch (InterruptedException e) {
            LOGGER.error("process interrupted",e);
        }
        if (!mails.isEmpty()) {
            try {
                mailConsumer.sendAllMail(mails);
            } catch (EmailException e) {
                LOGGER.error("error while sending mails",e);
            }
        }

    }
}
