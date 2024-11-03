package api.educai.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobItem;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AzureBlobService {

    @Autowired
    BlobContainerClient blobContainerClient;
    
    @Value("${azure.storage.url}")
    private String storageUrl;

    @Value("${azure.storage.connection.string}")
    private String connectionString;

    @Value("${azure.storage.container.name}")
    private String containerName;

    @Value("${azure.storage.blob-token}")
    private String token;

    @Getter
    private static String globalToken;

    @PostConstruct
    public void init() {
        globalToken = token;
    }

    public String upload(MultipartFile file)
            throws IOException {

        String uuid = String.valueOf(UUID.randomUUID());
        String[] parts;
        String type = "";

        try {
            if (file.getOriginalFilename() != null) {
                parts = file.getOriginalFilename().split("\\.");
                type = "." + parts[parts.length - 1];
            } else {
                if (file.getContentType() != null) {
                    parts = file.getContentType().split("/");
                    type = "." + parts[parts.length - 1];
                }
            }
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(415), "Error while trying to upload file. Check the file type and try again.");
        }

        BlobClient blob = blobContainerClient
                .getBlobClient(uuid + type);
        blob.upload(file.getInputStream(),
                file.getSize(), true);

        return storageUrl + uuid + type;
    }

    public String getBlobUrl(String fileName){
        BlobClient blob = blobContainerClient.getBlobClient(fileName);
        return blob.getBlobUrl() + "?%s".formatted(generateSasToken(fileName));
    }

    public byte[] download(String fileName)
            throws URISyntaxException {

        BlobClient blob = blobContainerClient.getBlobClient(fileName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blob.download(outputStream);
        return outputStream.toByteArray();

    }

    public List<String> listBlobs() {

        PagedIterable<BlobItem> items = blobContainerClient.listBlobs();
        List<String> names = new ArrayList<String>();
        for (BlobItem item : items) {
            names.add(item.getName());
        }
        return names;

    }

    public Boolean deleteBlob(String blobName) {

        BlobClient blob = blobContainerClient.getBlobClient(blobName);
        blob.delete();
        return true;
    }

    public String generateSasToken(String fileName) {

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        BlobClient blobClient = containerClient.getBlobClient(fileName);

        BlobSasPermission permission = new BlobSasPermission()
                .setReadPermission(true);
        OffsetDateTime expiryTime = OffsetDateTime.now().plusHours(1);
        BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(expiryTime, permission);

        return blobClient.generateSas(sasValues);

    }

}