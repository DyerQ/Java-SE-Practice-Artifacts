package ru.ncedu.bestgroup.mailing;

import ru.ncedu.bestgroup.mailing.model.Mail;
import ru.ncedu.bestgroup.mailing.model.MailTemplate;
import ru.ncedu.bestgroup.mailing.parsing.MailTemplateParser;
import ru.ncedu.bestgroup.mailing.utils.XMLUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Application {
    private static final String MAIL_TEMPLATE_DIRECTORY_KEY = "mail-templates-directory";
    private static final Integer WORK_PERIOD = 1500;

    public static void main(String[] args) throws Exception {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        BlockingQueue<Mail> queue = new LinkedBlockingQueue<Mail>();
        //final MailConsumer mailConsumer = ...

//        executor.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                Future<Integer> businessCardsFuture = executor.submit(new BusinessCardParser());
//                if (businessCardsFuture.isDone()) {
//                    int parsedCount = businessCardsFuture.get();
//                    if (parsedCount != 0) {
//                        System.out.println("Parsed ...");
//                    }
//
//                    try {
//                        Thread.sleep(750000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (businessCardsFuture.get() != 0 ) {
//                        executor.submit(new Callable<Boolean>() {
//                            @Override
//                            public Boolean call() throws Exception {
//                                Collection<Mail> mails = mailConsumer.;
//                                mailConsumer.sendAllMail(mails);
//                            }
//                        });
//                    }
//                }
//
//            }
//        },0,0, TimeUnit.MICROSECONDS);
        while (true) {
            Future<Integer> businessCardsFuture = executor.submit(new BusinessCardParser());
            if (businessCardsFuture.isDone()) {
                int parsedCount = businessCardsFuture.get();
                if (parsedCount != 0) {
                    System.out.println("Parsed ...");
                }

                Thread.sleep(750000);


                Future<Collection<MailTemplate>> mailTemplateFuture = executor.submit(new MailTemplateParser(""));

                while (!mailTemplateFuture.isDone()) {
                    Thread.sleep(100);
                }

                Collection<MailTemplate> result = mailTemplateFuture.get();
                Properties properties = new Properties();
                FileInputStream in = new FileInputStream(new File("config", "application.properties"));
                properties.load(in);
                in.close();

                MailProducer producer = new MailProducer(queue, properties);
                Collection<Mail> mailCollection = new ArrayList<Mail>();
                if (!result.isEmpty()) {
                    mailCollection.addAll(producer.buildMail(result, XMLUtils.parseBusinessCards(XMLUtils.readFromFile("recipients.xml"))));
                   /* executor.submit(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            Collection<Mail> mails = mailConsumer.enqueueMail(...);
                            mailConsumer.sendAllMail(mails);
                        }
                    });*/
                }
            }

        }
    }
}
