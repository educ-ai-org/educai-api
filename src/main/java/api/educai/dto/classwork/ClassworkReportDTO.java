package api.educai.dto.classwork;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassworkReportDTO {
    String title;
    Double correctPercentage;
    LocalDate datePosting;
}
