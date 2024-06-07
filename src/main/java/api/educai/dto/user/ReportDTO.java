package api.educai.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportDTO {

    private byte[] csv;
    private String fileName;

}
