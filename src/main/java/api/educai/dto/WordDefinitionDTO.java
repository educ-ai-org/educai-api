package api.educai.dto;

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
        mergeSortMeaning(0, meanings.getSize(), meanings.getArray());
    }

    public void sortDefinitions() {
        for (int i = 0; i < meanings.getSize(); i++) {
            meanings.getElement(i).sortDefinitions();
        }
    }

    private static void mergeSortMeaning(int startIndex, int endIndex, MeaningDTO[] meanings) {
        if(startIndex < endIndex - 1) {
            int middleIndex = (startIndex + endIndex) / 2;

            mergeSortMeaning(startIndex, middleIndex, meanings);
            mergeSortMeaning(middleIndex, endIndex, meanings);
            mergeMeaning(startIndex, middleIndex, endIndex, meanings);
        }
    }

    private static void mergeMeaning(int p, int q, int r, MeaningDTO[] meanings) {
        int i = p;
        int j = q;
        int k = 0;
        MeaningDTO[] w = new MeaningDTO[r - p];

        while (i < q && j < r) {
            if (meanings[i].getPartOfSpeech().compareTo(meanings[j].getPartOfSpeech()) < 0) {
                w[k++] = meanings[i++];
            } else {
                w[k++] = meanings[j++];
            }
        }

        while (i < q) {
            w[k++] = meanings[i++];
        }

        while (j < r) {
            w[k++] = meanings[j++];
        }

        for (i = p; i < r; i++) {
            meanings[i] = w[i - p];
        }
    }
}
