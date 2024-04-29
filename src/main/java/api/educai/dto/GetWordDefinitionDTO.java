package api.educai.dto;

import lombok.Getter;

@Getter
public class GetWordDefinitionDTO {
    private String word;
    private String phonetic;
    private Object phonetics;
    private String origin;
    private Object meanings;
}
