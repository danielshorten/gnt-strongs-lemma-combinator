package combinator;

import combinator.morphgnt.MorphGnt;
import combinator.strongs.GreekToStrongs;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class CombinatorRunner {

    private static final Logger LOGGER = Logger.getLogger(CombinatorRunner.class.getName());

    public static void main(String[] args)
            throws ParserConfigurationException, IOException, SAXException, XMLStreamException {
        try {
            Config.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        GreekToStrongs greekToStrongs = new GreekToStrongs();
        greekToStrongs.load();

        MorphGnt morphGnt = new MorphGnt();
        morphGnt.load();

        InputStream inputStream = CombinatorRunner.class.getClassLoader().getResourceAsStream(
            Config.get("bible.el.resourcePath").toString()
        );
        GNTCombinator combinator = new GNTCombinator();
        combinator.writeGNTWithLemmas(
                new InputSource(inputStream),
                greekToStrongs,
                morphGnt
        );
    }
}