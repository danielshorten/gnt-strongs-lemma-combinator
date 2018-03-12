package combinator.strongs;

import combinator.Config;
import combinator.util.GreekCharNormalizer;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.*;

public class GreekToStrongs extends DefaultHandler {

    private Map<String, Integer> greekToStrongsMap = new HashMap<>();

    private Stack<String> path = new Stack<>();
    private Integer currentStrongs = null;

    public void load() throws ParserConfigurationException, SAXException, IOException {
        InputSource source = new InputSource(
            GreekToStrongs.class.getClassLoader().getResourceAsStream(
                    Config.get("strongs.greek.resourcePath").toString()
            )
        );
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        xmlReader.setContentHandler(this);

        xmlReader.parse(source);
    }

    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) {
        path.push(localName);
        if (localName.equals("greek")) {
            String greek = atts.getValue("unicode").toLowerCase();
            greekToStrongsMap.put(
                GreekCharNormalizer.normalize(greek),
                currentStrongs
            );
        }
    }

    public void endElement(String uri, String localName, String qName) {
        path.pop();
    }

    public void characters(char ch[], int start, int length) {
        if (path.peek().equals("strongs")) {
            currentStrongs = Integer.parseInt(new String(ch, start, length));
        }
    }

    /*
     * There are some discrepancies between words in the SBLGNT, the morphgnt
     * index and Strong's dictionary, which are difficult to account for
     * automatically, so I'm hard-coding a list of them here.
     */
    private static final Map<String, String> strongsExceptions;
    static {
        strongsExceptions = new HashMap<>();
        strongsExceptions.put("ἄλφα", "α");
        strongsExceptions.put("ἀνάκειμαι", "ἀνακεῖμαι");
        strongsExceptions.put("ἀποθνῄσκω", "ἀποθνήσκω");
        strongsExceptions.put("βαρναβᾶς", "βαρνάβας");
        strongsExceptions.put("γρηγορέω", "γρηγορεύω");
        strongsExceptions.put("δαυίδ", "δαβίδ");
        strongsExceptions.put("δείκνυμι", "δεικνύω");
        strongsExceptions.put("εἴκοσι(ν)", "εἴκοσι");
        strongsExceptions.put("ἐκπλήσσομαι", "ἐκπλήσσω");
        strongsExceptions.put("ἐλεάω", "ἐλεέω");
        strongsExceptions.put("ἔνατος", "ἔννατος");
        strongsExceptions.put("ἐνδοξάζομαι", "ἐνδοξάζω");
        strongsExceptions.put("ἐπαγγέλλομαι", "ἐπαγγέλλω");
        strongsExceptions.put("ἐπικαλέω", "ἐπικαλέομαι");
        strongsExceptions.put("ἔξεστι(ν)", "ἔξεστι");
        strongsExceptions.put("ζῷον", "ζῶον");
        strongsExceptions.put("ζῳοποιέω", "ζωοποιέω");
        strongsExceptions.put("ἠλίας", "ἡλίας");
        strongsExceptions.put("ἡρῴδης", "ἡρώδης");
        strongsExceptions.put("ἠσαΐας", "ἡσαΐας");
        strongsExceptions.put("ἰερουσαλήμ", "ἱερουσαλήμ");
        strongsExceptions.put("κατάθεμα", "κατανάθεμα");
        strongsExceptions.put("κολλάομαι", "κολλάω");
        strongsExceptions.put("λοιπός", "λοιποί");
        strongsExceptions.put("μιμνῄσκομαι", "μιμνήσκω");
        strongsExceptions.put("οἶδα", "εἴδω");
        strongsExceptions.put("οὔ", "οὐ");
        strongsExceptions.put("οὕτω(ς)", "οὕτω");
        strongsExceptions.put("παρρησία", "παῤῥησία");
        strongsExceptions.put("πίμπλημι", "πλήθω");
        strongsExceptions.put("πραΰτης", "πραΰτης");
        strongsExceptions.put("ῥυπαρεύω", "ῥυπόω");
        strongsExceptions.put("σάρδιον", "σάρδιος");
        strongsExceptions.put("σῴζω", "σώζω");
        strongsExceptions.put("τις", "τίς");
        strongsExceptions.put("φοβέομαι", "φοβέω");
    }

    public Integer getStrongs(String greekWord) {
        // Check to see if this is one of the exceptional words
        String strongsWord = strongsExceptions.get(greekWord);
        greekWord = strongsWord != null ? strongsWord : greekWord;

        return greekToStrongsMap.get(greekWord);
    }
}
