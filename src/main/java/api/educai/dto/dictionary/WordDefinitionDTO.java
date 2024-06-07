package api.educai.dto.dictionary;

import api.educai.utils.ListObject;
import lombok.Getter;
import lombok.Setter;

@Getter
public class WordDefinitionDTO {
    private String word;
    @Setter
    private String audio;
    private ListObject<MeaningDTO> meanings;

    public WordDefinitionDTO(String word) {
        this.word = word;
        this.meanings = new ListObject<>(5);
    }

    public void addMeaning(MeaningDTO meaning) {
        meanings.add(meaning);
    }

    public void sortMeanings() {
        mergeSortMeaning(0, meanings.getSize(), meanings);
    }

    public void sortDefinitions() {
        for (int i = 0; i < meanings.getSize(); i++) {
            meanings.getElement(i).sortDefinitions();
        }
    }

    private static void mergeSortMeaning(int startIndex, int endIndex, ListObject<MeaningDTO> meanings) {
        if(startIndex < endIndex - 1) {
            int middleIndex = (startIndex + endIndex) / 2;

            mergeSortMeaning(startIndex, middleIndex, meanings);
            mergeSortMeaning(middleIndex, endIndex, meanings);
            mergeMeaning(startIndex, middleIndex, endIndex, meanings);
        }
    }

    private static void mergeMeaning(int p, int q, int r, ListObject<MeaningDTO> meanings) {
        int i = p;
        int j = q;
        int k = 0;
        MeaningDTO[] w = new MeaningDTO[r - p];

        while (i < q && j < r) {
            if (meanings.getElement(i).getPartOfSpeech().compareTo(meanings.getElement(j).getPartOfSpeech()) < 0) {
                w[k++] = meanings.getElement(i++);
            } else {
                w[k++] = meanings.getElement(j++);
            }
        }

        while (i < q) {
            w[k++] = meanings.getElement(i++);
        }

        while (j < r) {
            w[k++] = meanings.getElement(j++);
        }

        for (i = p; i < r; i++) {
            meanings.addInIndex(w[i - p], i);
        }
    }
}
