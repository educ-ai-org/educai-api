package api.educai.dto;

import lombok.Getter;
import org.bson.types.ObjectId;

@Getter
public class UserScoreDTO {

    private String id;
    private String name;
    private Integer score;
    private String profilePicture;

}
