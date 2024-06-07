package api.educai.utils.email;

import api.educai.dto.classroom.AddStudentInClassroomDTO;
import api.educai.dto.user.NewStudentEmailDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.springframework.http.ResponseEntity.status;

public class EmailService {
    public static void sendEmailSuccessfulCreateAccount(NewStudentEmailDTO newStudentEmailDTO) throws IOException, InterruptedException {
        HttpClient http = HttpClient.newHttpClient();

        ObjectMapper mapper = new ObjectMapper();
        EmailPayload emailPayload = new EmailPayload("template_lc3ek7n", newStudentEmailDTO);
        String payload = mapper.writeValueAsString(emailPayload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.emailjs.com/api/v1.0/email/send"))
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 201) {
            status(200).build();
        } else {
            throw new ResponseStatusException(HttpStatusCode.valueOf(500), "User registered, but email could not be sent");
        }
    }

    public static void sendEmailAddUserInClassroom(AddStudentInClassroomDTO addStudentInClassroomDTO) throws IOException, InterruptedException {
        HttpClient http = HttpClient.newHttpClient();

        ObjectMapper mapper = new ObjectMapper();
        EmailPayload emailPayload = new EmailPayload("template_fy99jke", addStudentInClassroomDTO);
        String payload = mapper.writeValueAsString(emailPayload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.emailjs.com/api/v1.0/email/send"))
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 201) {
            status(200).build();
        } else {
            System.out.println(response);
            throw new ResponseStatusException(HttpStatusCode.valueOf(500), "User registered, but email could not be sent");
        }
    }
}
