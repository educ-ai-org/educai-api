package api.educai.dto.dictionary;

import api.educai.utils.ListObject;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MeaningDTO {
    @Setter
    private String partOfSpeech;
    private ListObject<String> definitions;

    public MeaningDTO() {
        this.definitions = new ListObject<>(5);
    }

    private static void mergeSortDefinitions(int startIndex, int endIndex, ListObject<String> definitions) {
        if(startIndex < endIndex - 1) {
            int middleIndex = (startIndex + endIndex) / 2;

            mergeSortDefinitions(startIndex, middleIndex, definitions);
            mergeSortDefinitions(middleIndex, endIndex, definitions);
            mergeDefinitions(startIndex, middleIndex, endIndex, definitions);
        }
    }

    private static void mergeDefinitions(int p, int q, int r, ListObject<String> definitions) {
        int i = p;
        int j = q;
        int k = 0;
        String[] w = new String[r - p];

        while (i < q && j < r) {
            if (definitions.getElement(i).compareTo(definitions.getElement(j)) < 0) {
                w[k++] = definitions.getElement(i++);
            } else {
                w[k++] = definitions.getElement(j++);
            }
        }

        while (i < q) {
            w[k++] = definitions.getElement(i++);;
        }

        while (j < r) {
            w[k++] = definitions.getElement(j++);;
        }

        for (i = p; i < r; i++) {
            definitions.addInIndex(w[i - p], i);
        }
    }

    public void addDefinition(String definition) {
        definitions.add(definition);
    }

    public void sortDefinitions() {
        mergeSortDefinitions(0, definitions.getSize(), definitions);
    }
}
