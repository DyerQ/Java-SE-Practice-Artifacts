package ru.ncedu.bestgroup.mailing.utils;

import org.apache.commons.mail.EmailException;
import ru.ncedu.bestgroup.mailing.MailConsumer;
import ru.ncedu.bestgroup.mailing.model.Mail;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Илья on 06.11.2015.
 */
public class MailConsumerRunnable implements Runnable {
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
            e.printStackTrace();
        }
        if (!mails.isEmpty()) {
            try {
                mailConsumer.sendAllMail(mails);
            } catch (EmailException e) {
                e.printStackTrace();
            }
        }

    }
}
