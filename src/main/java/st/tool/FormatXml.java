package st.tool;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class FormatXml {
    public static Document newDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }

    public static Document getDocument(String xmlPath) throws SAXException, IOException, ParserConfigurationException {
        return getDocument(new File(xmlPath));
    }

    public static Document getDocument(File file) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
    }

    public static void saveDocument(Document doc, String xmlPath)
            throws TransformerConfigurationException, TransformerException {
        saveDocument(doc, new File(xmlPath));
    }

    public static void saveDocument(Document doc, File file)
            throws TransformerConfigurationException, TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    public static void write(Document doc, Writer writer)
            throws TransformerConfigurationException, TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
    }

    public static void viewNode(Node ele, String flag) {
        System.out.println("---------- Element:" + flag);
        System.out.println("getNodeName:" + ele.getNodeName());
        System.out.println("getLocalName:" + ele.getLocalName());
        System.out.println("getNodeValue:" + ele.getNodeValue());
        System.out.println("getTextContent:" + ele.getTextContent());
        System.out.println("getPrefix:" + ele.getPrefix());
        System.out.println("getBaseURI:" + ele.getBaseURI());
        System.out.println("getNamespaceURI:" + ele.getNamespaceURI());
        System.out.println("getNodeType:" + ele.getNodeType());
    }
}