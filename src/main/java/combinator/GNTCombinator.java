package combinator;

import combinator.morphgnt.MorphGnt;
import combinator.strongs.GreekToStrongs;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

public class GNTCombinator extends DefaultHandler {

    private static final Logger LOGGER = Logger.getLogger(GNTCombinator.class.getName());

    private Stack<String> nsPath = new Stack<>();
    private String currentTag;
    private String currentVerse;
    private XMLOutputFactory xof =  XMLOutputFactory.newInstance();
    private XMLStreamWriter xtw;
    private MorphGnt morphGnt;
    private GreekToStrongs greekToStrongs;

    private int found = 0;
    private int notFoundCount = 0;
    private Map<String, Integer> notFound = new HashMap<>();


    public void writeGNTWithLemmas(InputSource bibleInput, GreekToStrongs greekToStrongs, MorphGnt morphGnt)
            throws ParserConfigurationException, SAXException, IOException, XMLStreamException {

        this.greekToStrongs = greekToStrongs;
        this.morphGnt = morphGnt;

        xtw = xof.createXMLStreamWriter(new FileWriter(Config.get("combinator.outputFile").toString()));
        xtw.writeStartDocument("UTF-8","1.0");
        xtw.writeCharacters("\n");

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        xmlReader.setContentHandler(this);

        xmlReader.parse(bibleInput);

        xtw.writeEndDocument();
        xtw.flush();
        xtw.close();

        LOGGER.info("Found: " + found);
        notFound.forEach(
            (String word, Integer count) -> { if (count > 10) LOGGER.info(word + ": " + count); }
        );
        LOGGER.info("Not found: " + notFoundCount);
    }

    public void startPrefixMapping(String prefix, String uri) {
        nsPath.push(prefix);
    }
    public void endPrefixMapping(String prefix) {
        nsPath.pop();
    }

    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) {
        try {
            currentTag = localName;
            if (currentTag.equals("verse")) {
                currentVerse = atts.getValue("osisID");
            }

            xtw.writeStartElement(qName);
            // Write xmlns for osis tag
            if (qName.equals("osis")) {
                xtw.writeAttribute("xmlns", namespaceURI);
            }

            for (int i = 0; i < atts.getLength(); i++) {
                String attURI = atts.getURI(i);
                // Write namespace for osis tag
                if (!attURI.isEmpty() && qName.equals("osis")) {
                    xtw.writeNamespace(nsPath.peek(), attURI);
                }

                xtw.writeAttribute(atts.getQName(i), atts.getValue(i));
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }


    }

    public void endElement(String uri, String localName, String qName) {
        try {
            xtw.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        currentTag = null;
    }

    public void characters(char ch[], int start, int length) {
        try {
            if (currentTag != null && currentTag.equals("w")) {
                String greekWord = new String(ch, start, length);
                String normal = morphGnt.getNormal(greekWord);
                if (normal != null) {
                    Integer strongs = greekToStrongs.getStrongs(normal);
                    if (strongs != null) {
                        xtw.writeAttribute("lemma", strongs.toString());
                        found++;
                    }
                    else {
                        LOGGER.warning("Couldn't find Strong's number for " + normal +
                            " (" + currentVerse + ")"
                        );
                        addNotFound(normal);
                    }
                }
                else {
                    LOGGER.warning("Couldn't normalize " + greekWord);
                }
            }
            xtw.writeCharacters(ch, start, length);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void addNotFound(String word) {
        Integer count = notFound.get(word);
        notFound.put(word, count == null ? 1 : count + 1);
        notFoundCount++;
    }
}
