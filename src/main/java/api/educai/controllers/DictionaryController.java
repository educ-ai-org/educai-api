package api.educai.controllers;

import api.educai.dto.WordDefinitionDTO;
import api.educai.services.DictionaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Atividades", description = "API para serviços relacionados a atividades.")
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;

    @Operation(summary = "Retorna definição de uma palavra escolhida")
    @GetMapping("/{word}/definition")
    public WordDefinitionDTO getWordDefinition(@PathVariable String word) {
        return dictionaryService.getWordDefinition(word);
    }
}
