package combinator.morphgnt;

import java.io.*;
import java.net.URL;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class MorphGnt {
    private static final Logger LOGGER = Logger.getLogger(MorphGnt.class.getName());

    private Set<String> greekNormals = new HashSet<>();
    private Map<String, String> wordToNormal = new HashMap<>();

    public void load() throws IOException {
        CodeSource src = MorphGnt.class.getProtectionDomain().getCodeSource();
        if (src != null) {
            URL jar = src.getLocation();
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            while (true) {
                ZipEntry e = zip.getNextEntry();
                if (e == null)
                    break;
                String name = e.getName();
                if (name.startsWith("morphgnt/") && name.endsWith(".txt")) {
                    ZipFile zipFile = new ZipFile(jar.getFile());
                    ZipEntry zipEntry = zipFile.getEntry(name);
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    processInputStream(inputStream);

                    inputStream.close();
                    zipFile.close();
                }
            }
            zip.close();
        }
        else {
            LOGGER.warning("Failed to get jar location!");
        }
    }

    public void processInputStream(InputStream is) throws IOException {
        String line;

        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(is, "UTF-8"));

        while ((line = bufferedReader.readLine()) != null) {
            MorphGntLine parsedLine = new MorphGntLine(line);
            final String normal;
            if (!greekNormals.contains(parsedLine.getNormal())) {
                normal = parsedLine.getNormal();
                wordToNormal.put(normal, normal);
            }
            else {
                normal = wordToNormal.get(parsedLine.getNormal());
            }

            parsedLine.getVariants().forEach(
                (String variant) -> wordToNormal.put(variant, normal)
            );
        }

        bufferedReader.close();
    }

    public String getNormal(String greekWord) {
        return wordToNormal.get(greekWord);
    }
}
