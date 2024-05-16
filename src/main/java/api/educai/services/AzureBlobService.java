package api.educai.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobItem;

@Service
public class AzureBlobService {

    @Autowired
    BlobContainerClient blobContainerClient;
    private static String STORAGE_URL = "https://educaistorage.blob.core.windows.net/storage-educai/";

    public String upload(MultipartFile file)
            throws IOException {

        String uuid = String.valueOf(UUID.randomUUID());
        BlobClient blob = blobContainerClient
                .getBlobClient(uuid);
        blob.upload(file.getInputStream(),
                file.getSize(), true);

        return STORAGE_URL + uuid;
    }

    public String getBlobUrl(String fileName){
        BlobClient blob = blobContainerClient.getBlobClient(fileName);
        return blob.getBlobUrl();
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

}