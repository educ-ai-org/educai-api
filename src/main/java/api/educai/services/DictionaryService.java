package api.educai.services;

import api.educai.dto.dictionary.GetWordDefinitionDTO;
import api.educai.dto.dictionary.MeaningDTO;
import api.educai.dto.dictionary.WordDefinitionDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class DictionaryService {
    public WordDefinitionDTO getWordDefinition(String word) {
        GetWordDefinitionDTO response = sendWordDefinitionEmail(word).get(0);

        WordDefinitionDTO wordDefinitionDTO = new WordDefinitionDTO(response.getWord());
        String audio = response.getPhonetics().stream().filter(phonetic -> phonetic.getAudio() != null && phonetic.getAudio()
                .contains("-us.mp3")).findFirst().map(GetWordDefinitionDTO.Phonetic::getAudio).orElse(null);

        wordDefinitionDTO.setAudio(audio);

        response.getMeanings().forEach(meaning -> {
            MeaningDTO meaningDTO = new MeaningDTO();
            meaningDTO.setPartOfSpeech(meaning.getPartOfSpeech());

            meaning.getDefinitions().forEach(definition -> {
                if(meaningDTO.getDefinitions().getSize() < meaningDTO.getDefinitions().getLength()) {
                    meaningDTO.addDefinition(definition.getDefinition());
                }
            });

            if(wordDefinitionDTO.getMeanings().getSize() < wordDefinitionDTO.getMeanings().getLength()) {
                wordDefinitionDTO.addMeaning(meaningDTO);
            }
        });

        wordDefinitionDTO.sortMeanings();
        wordDefinitionDTO.sortDefinitions();

        return wordDefinitionDTO;
    }

    private List<GetWordDefinitionDTO> sendWordDefinitionEmail(String word) {
        HttpClient http = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.dictionaryapi.dev/api/v2/entries/en/%s".formatted(word)))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            return new ObjectMapper().readValue(response.body(), new TypeReference<>(){});
        } catch (IOException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(500), "Error while fetching word definition");
        }
    }
}
