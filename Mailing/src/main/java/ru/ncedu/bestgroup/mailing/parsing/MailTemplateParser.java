package ru.ncedu.bestgroup.mailing.parsing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncedu.bestgroup.mailing.model.MailTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;


public class MailTemplateParser implements Callable<Collection<MailTemplate>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailTemplateParser.class);

    private String directory;

    public MailTemplateParser(String directory) {
        this.directory = directory;
    }

    @Override
    public Collection<MailTemplate> call() throws Exception {
        File mailTemplateFolder = new File(directory);
        if (mailTemplateFolder.isDirectory()) {
            Collection<MailTemplate> result = new ArrayList<MailTemplate>();
            for (File f : mailTemplateFolder.listFiles()) {
                try {
                    result.add(new MailTemplate(bodyRead(f), f.getName()));
                    LOGGER.debug("file {} was processed",f.getName());
                    f.renameTo(new File(directory + File.separator + "processed"));
                } catch (IOException e) {
                    LOGGER.error("Error while reading {}. File will be moved to invalid dir",f.getName(),e);
                    f.renameTo(new File(directory + File.separator + "invalid"));
                }
                

            }
            return result;
        } else throw new Exception("no such directory");
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

