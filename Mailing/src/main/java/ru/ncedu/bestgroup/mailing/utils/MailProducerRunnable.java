package ru.ncedu.bestgroup.mailing.utils;

import ru.ncedu.bestgroup.mailing.MailProducer;
import ru.ncedu.bestgroup.mailing.model.Mail;
import ru.ncedu.bestgroup.mailing.model.MailTemplate;
import ru.ncedu.bestgroup.mailing.parsing.MailTemplateParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by Илья on 06.11.2015.
 */
public class MailProducerRunnable implements Runnable {
    private final ScheduledThreadPoolExecutor executor;
    private volatile BlockingQueue<Mail> queue;

    public MailProducerRunnable(ScheduledThreadPoolExecutor executor, BlockingQueue<Mail> queue) {
        this.executor = executor;
        this.queue = queue;
    }

    @Override
    public void run() {
        Future<Collection<MailTemplate>> mailTemplateFuture = executor.submit(new MailTemplateParser("./"));

        while (!mailTemplateFuture.isDone()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Collection<MailTemplate> result = null;
        try {
            result = mailTemplateFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Properties properties = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(new File("config", "application.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailProducer producer = new MailProducer(queue, properties);
        Collection<Mail> mailCollection = new ArrayList<Mail>();
        try {
            mailCollection.addAll(producer.buildMail(result, XMLUtils.parseBusinessCards(XMLUtils.readFromFile("recipients.xml"))));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
