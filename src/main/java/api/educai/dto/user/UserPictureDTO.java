package api.educai.dto.user;

import api.educai.entities.User;
import api.educai.services.AzureBlobService;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class UserPictureDTO {

    private ObjectId id;
    private String profilePicture;

    public UserPictureDTO(User user) {
        this.id = user.getId();
        this.profilePicture = user.getProfilePicture() + "?" + AzureBlobService.getGlobalToken();
    }

}
