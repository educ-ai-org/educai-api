package api.educai.services;

import api.educai.dto.classroom.AddStudentInClassroomDTO;
import api.educai.dto.classroom.ClassroomInfoDTO;
import api.educai.dto.classroom.PatchClassroomTitleAndCourse;
import api.educai.dto.classroom.UserScoreDTO;
import api.educai.dto.classwork.ClassworkDTO;
import api.educai.dto.classwork.ClassworkUserDTO;
import api.educai.dto.post.PostDTO;
import api.educai.dto.user.NewStudentEmailDTO;
import api.educai.dto.user.ReportDTO;
import api.educai.dto.user.UserDTO;
import api.educai.entities.Answer;
import api.educai.entities.Classroom;
import api.educai.entities.Post;
import api.educai.entities.User;
import api.educai.repositories.AnswerRepository;
import api.educai.repositories.ClassroomRepository;
import api.educai.utils.CSVGenerator;
import api.educai.utils.PasswordGenerator;
import api.educai.utils.email.EmailService;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ClassroomService {
    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AzureBlobService azureBlobService;

    public ClassroomInfoDTO createClassroom(Classroom classroom, ObjectId ownerId) {
        User user = userService.getUserById(ownerId);

        addUserInClassroom(classroom, user);

        return new ClassroomInfoDTO(classroom);
    }

    public ClassroomInfoDTO getClassroomDataById(ObjectId id) {
        Classroom classroom = getClassroomById(id);

        ClassroomInfoDTO classroomInfo = mapper.map(classroom, ClassroomInfoDTO.class);
        classroomInfo.setPosts(mapper.map(classroom.getPosts(), new TypeToken<List<PostDTO>>() {}.getType()));
        classroomInfo.setParticipants(mapper.map(classroom.getParticipants(), new TypeToken<List<UserDTO>>(){}.getType()));
        classroomInfo.setClassworks(classroom.getClassworks().stream().map(ClassworkDTO::new).toList());

        return classroomInfo;
    }

    public void inviteUser(ObjectId id, UserDTO newUser) {
        if (!userService.userEmailAlreadyExists(newUser.getEmail())) {
            String userPassword = PasswordGenerator.generate(8);
            User user = new User(newUser.getName(), newUser.getEmail(), userPassword, newUser.getRole());
            Classroom classroom = getClassroomById(id);

            userService.createUser(user);
            addUserInClassroom(classroom, user);

            NewStudentEmailDTO newStudentEmailDTO = new NewStudentEmailDTO(
                    user.getEmail(),
                    userPassword,
                    classroom.getCourse() + " - " + classroom.getTitle()
            );

            try {
                EmailService.sendEmailSuccessfulCreateAccount(newStudentEmailDTO);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            User user = userService.getUserByEmail(newUser.getEmail());
            Classroom classroom = getClassroomById(id);

            if (user.isUserEnrolledInClassroom(id)) {
                throw new ResponseStatusException(HttpStatusCode.valueOf(409), "This user is already registered in this classroom");
            }

            addUserInClassroom(classroom, user);

            AddStudentInClassroomDTO addStudentInClassroomDTO = new AddStudentInClassroomDTO(
                    user.getEmail(),
                    classroom.getCourse() + " - " + classroom.getTitle()
            );

            try {
                EmailService.sendEmailAddUserInClassroom(addStudentInClassroomDTO);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<UserDTO> getClassroomParticipants(ObjectId id) {
        Classroom classroom = getClassroomById(id);

        return classroom.getParticipants().stream().map(UserDTO::new).toList();
    }

    public Classroom getClassroomById(ObjectId id) {
        Classroom classroom = classroomRepository.findById(id);

        if (classroom == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Classroom not found!");
        }

        return classroom;
    }

    private void addUserInClassroom(Classroom classroom, User user) {
        classroom.addParticipant(user);
        classroomRepository.save(classroom);
        userService.addClassroom(user.getId(), classroom.getId());
    }

    public List<ClassworkDTO> getClassworks(ObjectId id) {
        Classroom classroom = getClassroomById(id);
        return classroom.getClassworks().stream().map(ClassworkDTO::new).toList();
    }

    public List<ClassworkUserDTO> getClassworksByUser(ObjectId id, ObjectId userId) {
        Classroom classroom = getClassroomById(id);
        User user = userService.getUserById(userId);
        return classroom.getClassworks().stream().map(classwork -> {
            ClassworkUserDTO classworkUserDTO = new ClassworkUserDTO(classwork);
            classworkUserDTO.setHasAnswered(classwork.getAnswers().stream()
                    .anyMatch(answer -> answer.getUser().equals(user)));
            return classworkUserDTO;
        }).toList();
    }

    public ReportDTO getUserReport(ObjectId classroomId, ObjectId userId) {
        User user = userService.getUserById(userId);
        Classroom classroom = getClassroomById(classroomId);

        List<Answer> userAnswers = classroom.getClassworks().stream()
                .flatMap(classwork -> classwork.getAnswers().stream())
                .filter(answer -> answer.getUser().equals(user))
                .toList();

        String fileName = user.getName().toUpperCase() + "-" + LocalDate.now() + ".csv";
        return new ReportDTO(CSVGenerator.generateClassworksReport(user, classroom, userAnswers), fileName);

    }

    public List<Post> getPostsByClassroom(ObjectId classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId);
        List<Post> posts = classroom.getPosts();
        posts.forEach(p -> p.setFile(azureBlobService.getBlobUrl(p.getFile())));
        return posts;
    }

    public void deleteClassroom(ObjectId id, ObjectId userId) {
        Classroom classroom = getClassroomById(id);
        User user = userService.getUserById(userId);

        if (classroom.getParticipants().stream().noneMatch(participant -> participant.equals(user))) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(403), "You are not the teacher of this classroom");
        }

        classroomRepository.delete(classroom);
    }

    public ClassroomInfoDTO updateClassroom(ObjectId id, PatchClassroomTitleAndCourse patchClassroomTitleAndCourse, ObjectId userId) {
        Classroom classroom = getClassroomById(id);
        User user = userService.getUserById(userId);

        if (classroom.getParticipants().stream().noneMatch(participant -> participant.equals(user))) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(403), "You are not the teacher of this classroom");
        }

        if(patchClassroomTitleAndCourse.getTitle() != null) {
            classroom.setTitle(patchClassroomTitleAndCourse.getTitle());
        }

        if(patchClassroomTitleAndCourse.getCourse() != null) {
            classroom.setCourse(patchClassroomTitleAndCourse.getCourse());
        }

        classroomRepository.save(classroom);

        return new ClassroomInfoDTO(classroom);
    }

    public List<UserScoreDTO> getLeaderBoard(ObjectId classroomId) {

        List<User> usersScore = userService.getUsersScore(classroomId);
        return mapper.map(usersScore, new TypeToken<List<UserScoreDTO>>(){}.getType());

    }

    public Classroom getClassroomByClassworkId(ObjectId classworkId) {
        List<Classroom> classrooms = classroomRepository.findAll();
        return classrooms.stream()
                .filter(classroom -> classroom.getClassworks().stream()
                        .anyMatch(classwork -> classwork.getId().equals(classworkId)))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "Classroom not found!"));
    }

}
