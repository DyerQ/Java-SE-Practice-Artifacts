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

    public static Document readFromFile(String fileName) throws ParserConfigurationException, IOException, SAXException {
        Document doc = null;
        doc = getDocumentBuilder().parse(fileName);
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
        NodeList nodeList = element.getElementsByTagName("business-card");
        Set<BusinessCard> set = new LinkedHashSet<BusinessCard>();
        BusinessCard[] businessCards = new BusinessCard[nodeList.getLength()];
        int length = nodeList.getLength();

        for (int i = 0; i < length; ++i) {
            Element el = (Element) nodeList.item(i);
            String mail = el.getElementsByTagName("mail").item(0).getTextContent();
            NodeList list = el.getElementsByTagName("property");
            Map<String, String> properties = new LinkedHashMap<String, String>();

            for (int j = 0; j < list.getLength(); j++) {
                Node nNode = list.item(j);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String name = eElement.getElementsByTagName("name").item(0).getTextContent();
                    String value = eElement.getElementsByTagName("value").item(0).getTextContent();
                    properties.put(name, value);
                }
                businessCards[i] = new BusinessCard(mail, properties);

            }
        }
        Collections.addAll(set, businessCards);
        return set;
    }

    public static Document convertBusinessCards(Set<BusinessCard> businessCards) throws IOException, SAXException, XMLStreamException, ParserConfigurationException {

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

            xMLStreamWriter.writeStartElement("properties");
            for (String i : e.getKeySet()) {
                xMLStreamWriter.writeStartElement("property");

                xMLStreamWriter.writeStartElement("name");
                xMLStreamWriter.writeCharacters(i);
                xMLStreamWriter.writeEndElement();

                xMLStreamWriter.writeStartElement("value");
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

    }

    public static boolean writeToFile(Document document, String fileName) throws TransformerException {

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        Result output = new StreamResult(new File(fileName));
        Source input = new DOMSource(document);
        transformer.transform(input, output);
        return true;
    }
}