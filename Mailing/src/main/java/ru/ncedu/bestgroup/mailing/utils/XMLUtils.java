package ru.ncedu.bestgroup.mailing.utils;

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
import ru.ncedu.bestgroup.mailing.model.BusinessCard;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class XMLUtils {
    private final static String BUSINESS_CARD_TAG = "business-card";
    private final static String BUSINESS_CARDS_TAG = "business-cards";
    private final static String MAIL = "mail";
    private final static String PROPERTY = "property";
    private final static String PROPERTYIES = "properties";
    private final static String NAME = "name";
    private final static String VALUE = "value";


    public static Document readFromFile(String fileName) {
        Document doc = null;
        try {
            doc = getDocumentBuilder().parse(fileName);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            try {
                return getDocumentBuilder().newDocument();
            } catch (ParserConfigurationException e1) {
                return null;
            }
        }


        return doc;
    }


    private static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringComments(true);
        dbf.setCoalescing(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setValidating(false);
        return dbf.newDocumentBuilder();
    }

    public static Set<BusinessCard> parseBusinessCards(Document element) {
        NodeList nodeList = element.getElementsByTagName(BUSINESS_CARD_TAG);
        Set<BusinessCard> set = new LinkedHashSet<BusinessCard>();
        BusinessCard[] businessCards = new BusinessCard[nodeList.getLength()];
        int length = nodeList.getLength();

        for (int i = 0; i < length; ++i) {
            Element el = (Element) nodeList.item(i);
            NodeList list = el.getElementsByTagName(PROPERTY);
            String mail = el.getElementsByTagName(MAIL).item(0).getTextContent();
            Map<String, String> properties = new LinkedHashMap<String, String>();

            for (int j = 0; j < list.getLength(); j++) {
                Node nNode = list.item(j);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String name = eElement.getElementsByTagName(NAME).item(0).getTextContent();
                    String value = eElement.getElementsByTagName(VALUE).item(0).getTextContent();
                    properties.put(name, value);
                }
            }
            businessCards[i] = new BusinessCard(mail, properties);
        }
        Collections.addAll(set, businessCards);
        return set;
    }

    public static Document convertBusinessCards(Set<BusinessCard> businessCards) {

        StringWriter stringWriter = new StringWriter();

        XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);

            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeStartElement(BUSINESS_CARDS_TAG);

            for (BusinessCard e : businessCards) {
                xMLStreamWriter.writeStartElement(BUSINESS_CARD_TAG);

                xMLStreamWriter.writeStartElement(MAIL);
                xMLStreamWriter.writeCharacters(e.getMail());
                xMLStreamWriter.writeEndElement();

                xMLStreamWriter.writeStartElement(PROPERTYIES);
                for (String i : e.getKeySet()) {
                    xMLStreamWriter.writeStartElement(PROPERTY);

                    xMLStreamWriter.writeStartElement(NAME);
                    xMLStreamWriter.writeCharacters(i);
                    xMLStreamWriter.writeEndElement();

                    xMLStreamWriter.writeStartElement(VALUE);
                    xMLStreamWriter.writeCharacters(e.getProperty(i));
                    xMLStreamWriter.writeEndElement();

                    xMLStreamWriter.writeEndElement();
                }
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

            return builder.parse(new InputSource(new StringReader(stringWriter.getBuffer().toString())));

        }catch (XMLStreamException | ParserConfigurationException | SAXException | IOException e){
            try {
                return getDocumentBuilder().newDocument();
            } catch (ParserConfigurationException e1) {
                return null;
            }
        }

    }

    public static boolean writeToFile(Document document, String fileName){

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(new File(fileName));
            Source input = new DOMSource(document);
            transformer.transform(input, output);
            return true;
        }
        catch (TransformerException e){
            return false;
        }
    }
}