package api.educai.dto.user;

import api.educai.entities.User;
import api.educai.services.AzureBlobService;
import lombok.Data;

@Data
public class UserPictureDTO {

    private String id;
    private String profilePicture;

    public UserPictureDTO(User user) {
        this.id = user.getId().toString();
        this.profilePicture = user.getProfilePicture() + "?" + AzureBlobService.getGlobalToken();
    }

}
