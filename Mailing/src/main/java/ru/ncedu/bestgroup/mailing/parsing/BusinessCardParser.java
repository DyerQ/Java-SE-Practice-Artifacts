package ru.ncedu.bestgroup.mailing.parsing;

import org.w3c.dom.Document;
import ru.ncedu.bestgroup.mailing.model.BusinessCard;
import ru.ncedu.bestgroup.mailing.utils.XMLUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

public class BusinessCardParser implements Callable<Integer> {
    private String directory;
    private String recipientsFileName;

    public BusinessCardParser(String directory, String recipientsFileName) {
        this.directory = directory;
        this.recipientsFileName = recipientsFileName;
    }

    public Integer call() throws Exception {
        Document doc = XMLUtils.readFromFile(recipientsFileName);
        Set <BusinessCard> set = XMLUtils.parseBusinessCards(doc);
        Set <BusinessCard> resultedSet = new HashSet<BusinessCard>();
        int j = 0;
        File currentDir = new File(directory);
        for (File f: currentDir.listFiles()){
            BusinessCard bc = parseBusinessCard(f);
            if(bc.equals(null)){
                f.renameTo(new File(directory + File.separator + "invalid"));
            }
            else{
                j++;
                f.renameTo(new File(directory + File.separator + "processed"));
            }
            for (BusinessCard next : set) {
                if (!(next.getMail().equals(bc.getMail()))) {
                    resultedSet.add(next);
                }
            }
            resultedSet.add(bc);
        }

        XMLUtils.writeToFile(XMLUtils.convertBusinessCards(resultedSet), recipientsFileName);
        Integer result = new Integer(j);

        return result;
    }

    private BusinessCard parseBusinessCard(File file) {
        try{
            if(!file.exists()){
                throw new Exception();
            }
            final int length = (int) file.length();
            if (length != 0) {

                char[] cbuf = new char[length];
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file),"CP1251");
                final int read = isr.read(cbuf);
                String text = new String(cbuf, 0, read);
                isr.close();
                Map<String, String> properties = new HashMap<String, String>();
                int i =0;
                int j =0;
                String key = "";
                String property = "";
                String mail = "";

                while (!(text.isEmpty())){
                    while (text.charAt(i) != ':'){
                        i++;
                    }
                    key = text.substring(0, i);
                    System.out.println(key);
                    try{
                        property = text.substring(i+2, text.indexOf('\n'));
                    }catch(StringIndexOutOfBoundsException e){
                        property = text.substring(i+2);
                        text = "";
                    }
                    text = text.substring(text.indexOf('\n') + 1);
                    i = 0;
                    if(key.equals("e-mail")){
                        mail = property;
                        continue;
                    }
                    properties.put(key, property);
                }
                BusinessCard bc = new BusinessCard(key, properties);
                return bc;

            }
        }catch(Exception e){
            return  null;
        }
        return null;
    }
}
