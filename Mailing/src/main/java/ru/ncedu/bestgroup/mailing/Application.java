package ru.ncedu.bestgroup.mailing;

import ru.ncedu.bestgroup.mailing.model.Mail;
import ru.ncedu.bestgroup.mailing.model.MailTemplate;
import ru.ncedu.bestgroup.mailing.parsing.BusinessCardParser;
import ru.ncedu.bestgroup.mailing.parsing.MailTemplateParser;
import ru.ncedu.bestgroup.mailing.utils.BusinessCardParserRunnable;
import ru.ncedu.bestgroup.mailing.utils.MailConsumerRunnable;
import ru.ncedu.bestgroup.mailing.utils.MailProducerRunnable;
import ru.ncedu.bestgroup.mailing.utils.XMLUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.*;

public class Application {
    private static final String MAIL_TEMPLATE_DIRECTORY_KEY = "mail-templates-directory";
    private static final Integer WORK_PERIOD = 1500;
    private static volatile BlockingQueue<Mail> queue = new LinkedBlockingQueue<Mail>();
    public static void main(String[] args) throws Exception {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
            executor.schedule(new BusinessCardParserRunnable(executor),WORK_PERIOD/2, TimeUnit.SECONDS);
            executor.schedule(new MailProducerRunnable(executor,queue),WORK_PERIOD/4,TimeUnit.SECONDS);
            executor.schedule(new MailConsumerRunnable(queue),WORK_PERIOD/4,TimeUnit.SECONDS);
   }
}
