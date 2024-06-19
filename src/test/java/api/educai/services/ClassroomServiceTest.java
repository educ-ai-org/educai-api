package api.educai.services;


import api.educai.dto.classroom.ClassroomInfoDTO;
import api.educai.dto.user.UserDTO;
import api.educai.entities.Classroom;
import api.educai.entities.User;
import api.educai.enums.Role;
import api.educai.repositories.ClassroomRepository;
import api.educai.repositories.UserRespository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClassroomServiceTest {

    @InjectMocks
    private ClassroomService classroomService;

    @Mock
    private ClassroomRepository classroomRepository;
    @Mock
    private UserRespository userRespository;

    @Mock
    private UserService userService;

    private ModelMapper mapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Create Classroom - Happy Path")
    @Test
    public void createClassroom_HappyPath() {
        Classroom classroom = new Classroom();
        classroom.setTitle("name");
        classroom.setCourse("description");
        classroom.setId(new ObjectId());

        ObjectId ownerId = new ObjectId();

        User user = new User("name", "email", "password", Role.TEACHER);
        user.setId(ownerId);

        when(userService.getUserById(ownerId)).thenReturn(user);
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        ClassroomInfoDTO result = classroomService.createClassroom(classroom, ownerId);

        assertEquals(new ClassroomInfoDTO(classroom), result);
        verify(classroomRepository, times(1)).save(classroom);
    }

    @DisplayName("Get Classroom Data By Id - Classroom Does Not Exists")
    @Test
    public void getClassroomDataById_NotFound() {
        ObjectId id = new ObjectId();
        when(classroomRepository.findById(id)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> classroomService.getClassroomDataById(id));
    }

    @DisplayName("Invite User - User Already Exists")
    @Test
    public void inviteUser_UserAlreadyExists() {
        ObjectId id = new ObjectId();
        mapper = new ModelMapper();
        User user = new User("name", "email", "password", Role.STUDENT);
        UserDTO newUser = mapper.map(user, UserDTO.class);

        when(userService.userEmailAlreadyExists(newUser.getEmail())).thenReturn(true);
        assertThrows(ResponseStatusException.class, () -> classroomService.inviteUser(id, newUser));
    }

    @DisplayName("Get Classroom Participants - Empty List")
    @Test
    public void getClassroomParticipants_EmptyList() {
        ObjectId id = new ObjectId();
        Classroom classroom = new Classroom();

        when(classroomRepository.findById(id)).thenReturn(classroom);
        assertTrue(classroomService.getClassroomParticipants(id).isEmpty());
    }

    @DisplayName("Get Classworks - Empty List")
    @Test
    public void getClassworks_EmptyList() {
        ObjectId id = new ObjectId();
        Classroom classroom = new Classroom();

        when(classroomRepository.findById(id)).thenReturn(classroom);
        assertTrue(classroomService.getClassworks(id).isEmpty());
    }

    @DisplayName("Delete Classroom - User is Not a Teacher")
    @Test
    public void deleteClassroom_NotTeacher() {
        ObjectId id = new ObjectId();
        ObjectId userId = new ObjectId();
        Classroom classroom = new Classroom();
        User user = new User("name", "email", "password", Role.STUDENT);

        when(classroomRepository.findById(id)).thenReturn(classroom);
        when(userService.getUserById(userId)).thenReturn(user);

        when(classroomRepository.findById(id)).thenReturn(classroom);
        when(userService.getUserById(userId)).thenReturn(user);
        assertThrows(ResponseStatusException.class, () -> classroomService.deleteClassroom(id, userId));
    }

    @DisplayName("Remove User - Happy Path")
    @Test
    public void removeUser_UserExistsInClassroom() {
        ObjectId classroomId = new ObjectId();
        ObjectId reqUserId = new ObjectId();
        ObjectId userId = new ObjectId();
        Classroom classroom = new Classroom();
        User user = new User("name", "email", "password", Role.STUDENT);
        user.setId(userId);
        user.getClassrooms().add(classroom);
        classroom.addParticipant(user);
        classroom.setId(classroomId);

        when(classroomRepository.findById(classroomId)).thenReturn(classroom);
        when(userService.getUserById(userId)).thenReturn(user);

        classroomService.removeUser(classroomId, userId, reqUserId);

        assertFalse(classroom.getParticipants().contains(user));
        assertFalse(user.getClassrooms().contains(classroom));
        verify(classroomRepository, times(1)).save(classroom);
    }

    @DisplayName("Remove User - Classroom Does Not Exist")
    @Test
    public void removeUser_ClassroomDoesNotExist() {
        ObjectId classroomId = new ObjectId();
        ObjectId userId = new ObjectId();
        ObjectId reqUserId = new ObjectId();

        when(classroomRepository.findById(classroomId)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> classroomService.removeUser(classroomId, userId, reqUserId));
    }
}
