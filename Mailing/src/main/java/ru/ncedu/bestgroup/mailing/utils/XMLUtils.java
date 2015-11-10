package ru.ncedu.bestgroup.mailing.utils;

import java.io.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(XMLUtils.class);

    private final static String BUSINESS_CARD_TAG = "business-card";
    private final static String BUSINESS_CARDS_TAG = "business-cards";
    private final static String MAIL = "mail";
    private final static String PROPERTY = "property";
    private final static String PROPERTIES = "properties";
    private final static String NAME = "name";
    private final static String VALUE = "value";


    public static Document readFromFile(String fileName) {
        Document doc;
        try {
            doc = getDocumentBuilder().parse(fileName);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            LOGGER.error("Reading error",e);
            try {
                return getDocumentBuilder().newDocument();
            } catch (ParserConfigurationException e1) {
                LOGGER.error("Can't create empty doc",e1);
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
        Set<BusinessCard> set = new LinkedHashSet<>();
        BusinessCard[] businessCards = new BusinessCard[nodeList.getLength()];
        int length = nodeList.getLength();


        for (int i = 0; i < length; ++i) {
            Element el = (Element) nodeList.item(i);
            NodeList list = el.getElementsByTagName(PROPERTY);
            String mail = el.getElementsByTagName(MAIL).item(0).getTextContent();
            LOGGER.debug("Found mail property with value {}",mail);
            Map<String, String> properties = new LinkedHashMap<>();

            for (int j = 0; j < list.getLength(); j++) {
                Node nNode = list.item(j);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String name = eElement.getElementsByTagName(NAME).item(0).getTextContent();
                    String value = eElement.getElementsByTagName(VALUE).item(0).getTextContent();
                    LOGGER.debug("Found {} property with value {}",name,value);
                    properties.put(name, value);
                }
            }
            businessCards[i] = new BusinessCard(mail, properties);

        }
        LOGGER.debug("{} business cards created",businessCards.length);
        Collections.addAll(set, businessCards);
        return set;
    }

    public static Document convertBusinessCards(Set<BusinessCard> businessCards) {

        StringWriter stringWriter = new StringWriter();

        XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);
            LOGGER.info("XML address book creation started");
            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeStartElement(BUSINESS_CARDS_TAG);

            for (BusinessCard e : businessCards) {
                xMLStreamWriter.writeStartElement(BUSINESS_CARD_TAG);
                xMLStreamWriter.writeStartElement(MAIL);
                xMLStreamWriter.writeCharacters(e.getMail());
                xMLStreamWriter.writeEndElement();

                xMLStreamWriter.writeStartElement(PROPERTIES);
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
            Document document =builder.parse(new InputSource(new StringReader(stringWriter.getBuffer().toString())));
            LOGGER.info("XML address book created");
            return document;

        }catch (XMLStreamException | ParserConfigurationException | SAXException | IOException e){
            LOGGER.error("Error while creating XML",e);
            try {
                return getDocumentBuilder().newDocument();
            } catch (ParserConfigurationException e1) {
                LOGGER.error("Can't create empty doc",e);
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
            LOGGER.error("transformer error",e);
            return false;
        }
    }
}