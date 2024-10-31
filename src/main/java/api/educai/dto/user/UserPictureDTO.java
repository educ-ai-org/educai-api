package api.educai.dto.user;

import api.educai.entities.User;
import lombok.Data;
import org.bson.types.ObjectId;

import static api.educai.services.UserService.getFileName;

@Data
public class UserPictureDTO {

    private ObjectId id;
    private String profilePicture;

    public UserPictureDTO(User user) {
        this.id = user.getId();
        this.profilePicture = getFileName(user.getProfilePicture());
    }

}
