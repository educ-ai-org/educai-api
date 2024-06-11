package api.educai.utils;

import api.educai.dto.classwork.ClassworkReportDTO;
import api.educai.entities.Answer;
import api.educai.entities.Classroom;
import api.educai.entities.User;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;

public class CSVGenerator {

    // método para salvar arquivo csv no projeto
    public static void saveClassworksReportCsv(User user, Classroom classroom, List<Answer> answers) throws IOException {
        FileWriter file;
        Formatter saida;
        String fileName = user.getName().toUpperCase() + "-" + LocalDate.now();

        fileName += ".csv";

        try {
            file = new FileWriter(fileName);
            saida = new Formatter(file);
        } catch (IOException ex) {
            throw new IOException();
        }

        try {
            saida.format("%s;Course: %s;Classroom Name: %s\n", user.getName(), classroom.getCourse(), classroom.getTitle());
            saida.format("Nome da atividade;Porcentagem de acerto;Data de postagem\n");

            for (Answer answer : answers) {
                saida.format("%s;%.1f;%s\n", answer.getClasswork().getTitle(), answer.getCorrectPercentage(), answer.getDatePosting());
            }

        } catch (FormatterClosedException ex) {
            throw new FormatterClosedException();
        } finally {
            saida.close();
            try {
                file.close();
            } catch (IOException ex) {
                throw new IOException();
            }
        }
    }

    public static byte[] generateClassworksReport(User user, Classroom classroom, List<Answer> answers) throws FormatterClosedException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Formatter saida = new Formatter(outputStream);

        ClassworkReportDTO[][] classworkReport = new ClassworkReportDTO[answers.size()][3];
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            classworkReport[i][0] = new ClassworkReportDTO(answer.getClasswork().getTitle(), answer.getCorrectPercentage(), answer.getDatePosting());
        }

        try {
            saida.format("Student: %s;Course: %s;Classroom Name: %s\n", user.getName(), classroom.getCourse(), classroom.getTitle());
            saida.format("Nome da atividade;Porcentagem de acerto;Data de postagem\n");

            for (ClassworkReportDTO[] classworkReportDTOS : classworkReport) {
                ClassworkReportDTO data = classworkReportDTOS[0];
                saida.format("%s;%.1f%%;%s\n", data.getTitle(), data.getCorrectPercentage(), data.getDatePosting());
            }

        } catch (FormatterClosedException ex) {
            throw new FormatterClosedException();
        } finally {
            saida.flush();
            saida.close();
        }

        return outputStream.toByteArray();

    }

}
