package api.educai.controllers;

import api.educai.dto.GetWordDefinitionDTO;
import api.educai.services.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dictionary")
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping("/{word}/definition")
    public List<GetWordDefinitionDTO> getWordDefinition(@PathVariable String word) {
        return dictionaryService.getWordDefinition(word);
    }
}
