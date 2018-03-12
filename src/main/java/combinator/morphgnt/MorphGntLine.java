package combinator.morphgnt;

import combinator.util.GreekCharNormalizer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MorphGntLine {

    private Integer book;
    private Integer chapter;
    private Integer verse;

    private Set<String> variants;
    private String normal;

    public MorphGntLine() {
        /* Default constructor */
    }

    public MorphGntLine(String lineToParse) {
        List<String> parts = Pattern.compile(" ")
            .splitAsStream(lineToParse)
            .map(String::trim)
            .collect(Collectors.toList());

        assert parts.size() == 7;

        Integer[] bookChapterVerse = parseBookChapterVerse(parts.get(0));
        book = bookChapterVerse[0];
        chapter = bookChapterVerse[1];
        verse = bookChapterVerse[2];

        variants = new HashSet<>();
        for (int i = 3; i <= 5; i++) {
            variants.add(
                GreekCharNormalizer.normalize(parts.get(i))
            );
        }

        normal = GreekCharNormalizer.normalize(parts.get(6)).toLowerCase();
    }

    private Integer[] parseBookChapterVerse(String bookChapterVerse) {
        int book = Integer.parseInt(bookChapterVerse.substring(0,2));
        int chapter = Integer.parseInt(bookChapterVerse.substring(2, 4));
        int verse = Integer.parseInt(bookChapterVerse.substring(4,6));
        return new Integer[]{book, chapter, verse};
    }

    /* Getters */
    public Integer getBook() {
        return book;
    }

    public Integer getChapter() {
        return chapter;
    }

    public Integer getVerse() {
        return verse;
    }

    public Set<String> getVariants() {
        return variants;
    }

    public String getNormal() {
        return normal;
    }
}