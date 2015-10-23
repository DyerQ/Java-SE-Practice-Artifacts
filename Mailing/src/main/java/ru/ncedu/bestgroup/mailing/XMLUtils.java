package ru.ncedu.bestgroup.mailing;
import org.w3c.dom.*;

import java.util.Set;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLUtils {

    public static Element readFromFile(String fileName) {
        Document doc = null;
        try {
            doc = getDocumentBuilder().parse(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc.getDocumentElement();
    }



    private static DocumentBuilder getDocumentBuilder() throws Exception {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringComments(true);
            dbf.setCoalescing(true);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setValidating(false);
            return dbf.newDocumentBuilder();
        } catch (Exception exc) {
            throw new Exception(exc.getMessage());
        }
    }

    public static Set<BusinessCard> parseBusinessCards(Document document) {
        new BusinessCard()

        return null;
    }

    public static Document convertBusinessCards(Set<BusinessCard> businessCards) {


        return null;
    }

    public static boolean writeToFile(Element document, String fileName) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(new File(fileName));
            Source input = new DOMSource(document);
            transformer.transform(input, output);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return false; // returns false on unsuccessful write
    }
}