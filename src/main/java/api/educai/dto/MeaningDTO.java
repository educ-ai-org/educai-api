package api.educai.dto;

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

    private static void mergeSortDefinitions(int startIndex, int endIndex, String[] definitions) {
        if(startIndex < endIndex - 1) {
            int middleIndex = (startIndex + endIndex) / 2;

            mergeSortDefinitions(startIndex, middleIndex, definitions);
            mergeSortDefinitions(middleIndex, endIndex, definitions);
            mergeDefinitions(startIndex, middleIndex, endIndex, definitions);
        }
    }

    private static void mergeDefinitions(int p, int q, int r, String[] definitions) {
        int i = p;
        int j = q;
        int k = 0;
        String[] w = new String[r - p];

        while (i < q && j < r) {
            if (definitions[i].compareTo(definitions[j]) < 0) {
                w[k++] = definitions[i++];
            } else {
                w[k++] = definitions[j++];
            }
        }

        while (i < q) {
            w[k++] = definitions[i++];
        }

        while (j < r) {
            w[k++] = definitions[j++];
        }

        for (i = p; i < r; i++) {
            definitions[i] = w[i - p];
        }
    }

    public void addDefinition(String definition) {
        definitions.add(definition);
    }

    public void sortDefinitions() {
        mergeSortDefinitions(0, definitions.getSize(), definitions.getArray());
    }
}
