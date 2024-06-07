package api.educai.dto.dictionary;

import lombok.Getter;

import java.util.List;

@Getter
public class GetWordDefinitionDTO {
    private String word;
    private String phonetic;
    private List<Phonetic> phonetics;
    private List<Meaning> meanings;
    private License license;
    private List<String> sourceUrls;

    @Getter
    public static class Phonetic {
        private String text;
        private String audio;
        private String sourceUrl;
        private License license;
    }

    @Getter
    public static class Definition {
        private String definition;
        private List<String> synonyms;
        private List<String> antonyms;
        private String example;
    }

    @Getter
    public static class Meaning {
        private String partOfSpeech;
        private List<Definition> definitions;
        private List<String> synonyms;
        private List<String> antonyms;
        private String example;
    }

    @Getter
    public static class License {
        private String name;
        private String url;
    }
}