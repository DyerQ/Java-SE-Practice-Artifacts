package ru.ncedu.bestgroup.mailing;

import java.io.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class XMLUtils {

    public static Document readFromFile(String fileName) {
        Document doc = null;
        try {
            doc = getDocumentBuilder().parse(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
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

    public static Set<BusinessCard> parseBusinessCards(Document element) {
        try {
            element.normalize();
            NodeList nList = element.getElementsByTagName("business-card");
            Set<BusinessCard> set = new LinkedHashSet<BusinessCard>();
            BusinessCard[] businessCards = new BusinessCard[nList.getLength()];
            for (int temp = 0; temp < nList.getLength(); temp++) {


                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String mail = eElement.getElementsByTagName("mail").item(0).getTextContent();
                    String fullName = eElement.getElementsByTagName("full-name").item(0).getTextContent();
                    String university = eElement.getElementsByTagName("university").item(0).getTextContent();
                    Map<String, String> properties = new HashMap<String, String>();
                    properties.put("full-name", fullName);
                    properties.put("university", university);
                    businessCards[temp] = new BusinessCard(mail, properties);
                }
            }
            Collections.addAll(set, businessCards);
            return set;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Document convertBusinessCards(Set<BusinessCard> businessCards) {

        try {

            StringWriter stringWriter = new StringWriter();

            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);

            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeStartElement("business-cards");

            for (BusinessCard e : businessCards) {
                xMLStreamWriter.writeStartElement("business-card");
                xMLStreamWriter.writeStartElement("mail");
                xMLStreamWriter.writeCharacters(e.getMail());
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeStartElement("full-name");
                xMLStreamWriter.writeCharacters(e.getProperty("full-name"));
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeStartElement("university");
                xMLStreamWriter.writeCharacters(e.getProperty("university"));
                xMLStreamWriter.writeEndElement();
                xMLStreamWriter.writeEndElement();

            }
            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndDocument();
            xMLStreamWriter.flush();
            xMLStreamWriter.close();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(stringWriter.getBuffer().toString())));

            return doc;

        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }


        return null;
    }

    public static boolean writeToFile(Document document, String fileName) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(new File(fileName));
            Source input = new DOMSource(document);
            transformer.transform(input, output);
            return true;
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return false; // returns false on unsuccessful write
    }
}