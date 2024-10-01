package api.educai.controllers;

import api.educai.services.AzureBlobService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("file")
@Tag(name = "Azure Storage", description = "API para serviços relacionados ao armazenamento de arquivos.")
public class AzureStorageController {

    @Autowired
    private AzureBlobService azureStorageService;

    @GetMapping("/sas-token")
    public String getSasToken(@RequestParam String fileName) {
        return azureStorageService.generateSasToken(fileName);
    }

}
