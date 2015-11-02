package ru.ncedu.bestgroup.mailing.parsing;

import ru.ncedu.bestgroup.mailing.MailTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;


public class MailTemplateParser implements Callable<Collection<MailTemplate>> {
    @Override
    public Collection<MailTemplate> call() throws Exception {
        File mailTemplateFolder = new File("./mail-templates");

        if (mailTemplateFolder.isDirectory()){
            Collection<MailTemplate> result = new ArrayList<MailTemplate>();
            for (File f: mailTemplateFolder.listFiles()){
                result.add(new MailTemplate(bodyRead(f), f.getName()));
            }
            return result;
        }else throw new Exception("no such directory");
    }
    private String bodyRead(File f) throws IOException {
        StringBuilder text = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line;
        while ((line = br.readLine()) != null) {
            text.append(line + '\n');
        }
        return text.toString();
    }

}

