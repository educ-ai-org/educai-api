package api.educai.services;

import api.educai.dto.GetWordDefinitionDTO;
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
    public List<GetWordDefinitionDTO> getWordDefinition(String word) {
        List<GetWordDefinitionDTO> response = sendWordDefinitionEmail(word);

        return response;
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
            ObjectMapper mapper = new ObjectMapper();
            List<GetWordDefinitionDTO> list = mapper.readValue(response.body(), new TypeReference<List<GetWordDefinitionDTO>>(){});
            return list;
        } catch (IOException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(500), "Error while fetching word definition");
        }
    }
}
