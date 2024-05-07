package api.educai.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Option {

    @Id
    private ObjectId id;

    @NotBlank
    @Size(max = 10)
    private String key;

    @NotBlank
    @Size(max = 200)
    private String description;

}
