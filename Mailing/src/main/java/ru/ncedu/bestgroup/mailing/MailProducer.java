package ru.ncedu.bestgroup.mailing;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public class MailProducer {
    private String recipientsFileName;
    private BlockingQueue<Mail> queue;
    private Properties defaultProperties;

    public MailProducer(String recipientsFileName, BlockingQueue<Mail> queue, Properties defaultProperties) {
        this.recipientsFileName = recipientsFileName;
        this.queue = queue;
        this.defaultProperties = defaultProperties;
    }

    public Collection<Mail> buildMail(Collection<MailTemplate> templates) throws Exception {
        Set<BusinessCard> businesscards;
        businesscards = XMLUtils.parseBusinessCards(XMLUtils.readFromFile(recipientsFileName));
        Collection<Mail> result = new ArrayList<Mail>();
        if (businesscards == null){
            throw new Exception("Recipients File does not exists");
        }
        else{
            for (MailTemplate templ : templates) {
                for (BusinessCard busincard : businesscards) {
                    result.add(templ.getMail(busincard, defaultProperties));
                }
            }
            return result;
        }
    }

    public void enqueueMail(Collection<Mail> mails) throws Exception {
        if (mails == null){
            throw new Exception("Check your mail collection");
        }
        else{
            for (Mail mail : mails) {
                queue.add(mail);
            }
        }
    }
}
