package com.example.finalprojectvirtualteacher.service;

import com.example.finalprojectvirtualteacher.Helpers;
import com.example.finalprojectvirtualteacher.exceptions.*;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.UserFilterOptions;
import com.example.finalprojectvirtualteacher.models.dto.UserDtoUpdate;
import com.example.finalprojectvirtualteacher.repositories.contracts.AssignmentRepository;
import com.example.finalprojectvirtualteacher.repositories.contracts.CourseRepository;
import com.example.finalprojectvirtualteacher.repositories.contracts.UserRepository;
import com.example.finalprojectvirtualteacher.services.UserServiceImpl;
import com.example.finalprojectvirtualteacher.services.contacts.CommentService;
import com.example.finalprojectvirtualteacher.services.contacts.CourseService;
import com.example.finalprojectvirtualteacher.services.contacts.EmailService;
import com.example.finalprojectvirtualteacher.services.contacts.LectureService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Timestamp;
import java.time.Instant;
import java.util.*;

import static com.example.finalprojectvirtualteacher.Helpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.MockUtil.createMock;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTests {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseService courseServiceMock;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private LectureService lectureService;

    @Mock
    private CommentService commentService;

    @Mock
    EmailService emailService;


    @Test
    void getAll_ShouldReturnListOfUsers_WhenFilterOptionsProvided() {
        UserFilterOptions filterOptions = createUserFilterOptions();
        List<User> mockUsers = new ArrayList<>();
        when(userRepository.getAll(filterOptions)).thenReturn(mockUsers);


        List<User> result = userService.getAll(filterOptions);

        assertEquals(mockUsers, result);
        verify(userRepository,times(1)).getAll(filterOptions);
    }
    @Test
    void getAll_ShouldReturnListOfUsers() {
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(createMockUser());
        mockUsers.add(createMockUser());

        when(userRepository.getAll()).thenReturn(mockUsers);

        List<User> result = userService.getAll();

        assertEquals(result,mockUsers);
        verify(userRepository,times(1)).getAll();
    }


    @Test
    void getAllTeacher_Should_CallRepository(){
        List<User> teachers = new ArrayList<>();
        teachers.add(createMockTeacher());
        teachers.add(createMockTeacher());

        when(userRepository.getAllTeachers()).thenReturn(teachers);

       List<User> result= userService.getAllTeachers();
        assertEquals(result,teachers);
        verify(userRepository,times(1)).getAllTeachers();
    }
    @Test
    public void getById_Should_Return_User() {
       User user = createMockUser();

        when(userRepository.getById(user.getId()))
                .thenReturn(user);

        User result = userService.getById(user.getId());

        assertEquals(result, user);
        verify(userRepository,times(1)).getById(user.getId());
    }


    @Test
    void getAllStudent_Should_CallRepository(){
        List<User> list = new ArrayList<>();
        list.add(createMockUser());
        list.add(createMockUser());

        when(userRepository.getAllStudents()).thenReturn(list);
        userService.getAllStudents();

        verify(userRepository,times(1)).getAllStudents();
    }

    @Test
    void getByEmail_ShouldReturnUser_WhenEmailExists() {
        User user = createMockUser();

        when(userRepository.getByEmail(user.getEmail())).thenReturn(user);

        User result = userService.getByEmail(user.getEmail());

        assertEquals(result,user);
        verify(userRepository,times(1)).getByEmail(user.getEmail());
    }


@Test
void createUser_Should_CallRepository() {
    // Arrange
    User userToCreate = Helpers.createMockUser();

    when(userRepository.getByEmail(userToCreate.getEmail())).thenThrow(new EntityNotFoundException("User not found"));
    when(userRepository.create(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    User createdUser = userService.create(userToCreate);

    // Assert
    assertNotNull(createdUser);
    verify(userRepository, times(1)).getByEmail(userToCreate.getEmail());
    verify(userRepository, times(1)).create(userToCreate);
    verify(emailService, times(1)).sendUserCreationVerificationCode(eq(userToCreate), anyInt());
}


    @Test
    void enrollCourse_Should_CallRepository_When_CourseIsEnrolled() {
        // Arrange
        User user = Helpers.createMockUser();
        int courseId = 1;
        Course course = Helpers.createMockCourse();

        when(courseRepository.getById(courseId)).thenReturn(course);
        when(userRepository.updateUser(user)).thenReturn(user);

        // Act
        User result = userService.enrollCourse(user, courseId);

        // Assert
        assertTrue(result.getCourses().contains(course));

        // Verify that the repository methods are called
        verify(courseRepository, times(1)).getById(courseId);
        verify(userRepository, times(1)).updateUser(user);
    }


    @Test
    public void createUser_Should_ThrowEntityDuplicateException_When_UsernameAlreadyExists() {
        User existingUser = createMockUser();

        when(userRepository.getByEmail(existingUser.getEmail())).thenThrow(EntityDuplicateException.class);

        assertThrows(EntityDuplicateException.class, () -> userService.create(existingUser));
    }

    @Test
    void updateUser_Should_CallRepository_When_UpdateIsSuccessful() {
        User user = createMockUser();
        User updatedUser = createMockUser();
        UserDtoUpdate userDtoUpdate = createMockUserDtoUpdate();

        when(userRepository.updateUser(updatedUser)).thenReturn(updatedUser);

        User result = userService.updateUser(user, updatedUser, userDtoUpdate);

        assertEquals(result,updatedUser);
        verify(userRepository,times(1)).updateUser(updatedUser);
    }




    @Test
    public void testEnrollCourse() {
        // Mocking
        int courseId = 1;
        int userId = 1;
        User user = new User();
        Course mockCourse = new Course();

        user.setCourses(new HashSet<>());

        when(courseRepository.getById(courseId)).thenReturn(mockCourse);
        when(userRepository.updateUser(any(User.class))).thenReturn(user);

        // Act
        User result = userService.enrollCourse(user, courseId);

        // Assert
        assertTrue(result.getCourses().contains(mockCourse));

        // Verify that the updateUser method is called once with the correct arguments
        verify(userRepository, times(1)).updateUser(user);
    }

    @Test
    void create_ShouldThrowEntityDuplicateException_WhenEmailAlreadyExists() {
        User existingUser = createMockUser();

        when(userRepository.getByEmail(existingUser.getEmail())).thenReturn(existingUser);

        assertThrows(EntityDuplicateException.class,
                () -> userService.create(existingUser));

    }

    @Test
    void resendActivationCode_shouldThrowExceptionForActivatedUser() {
        // Arrange
        String userEmail = "test@example.com";
        User activatedUser = new User();
        activatedUser.setActivated(true);
        when(userRepository.getByEmail(userEmail)).thenReturn(activatedUser);

        // Act and Assert
        assertThrows(ForbiddenOperationException.class, () -> userService.resendActivationCode(userEmail));

        // Verify that userRepository.getByEmail is called
        verify(userRepository).getByEmail(userEmail);

        // Verify that emailService.sendActivationEmail is never called
        verify(emailService, never()).sendMessage(userEmail,"confirm",userEmail);
    }





    @Test
    void addProfilePhoto_ShouldSetProfilePhotoUrl_WhenValidUrl() {

        User user = createMockUser();
        String newUrl = "newprofilephoto.jpg";
        when(userRepository.updateUser(user)).thenReturn(user);

        User result = userService.addProfilePhoto(user, newUrl);

        assertEquals(newUrl, result.getProfilePictureUrl());
        verify(userRepository, times(1)).updateUser(user);
    }



    @Test
    public void testActivateAccountWithNonexistentCode() {
        // Arrange
       User user = createMockUser();
        UserRepository userRepository = mock(UserRepository.class);

        // Act and Assert
        WrongActivationCodeException exception = assertThrows(WrongActivationCodeException.class, () -> {
            userService.activateAccount(789);
        });

        assertEquals("Code not active. Maybe user is activated already?", exception.getMessage());
        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).updateUser(any(User.class));
    }



    @Test
    void inviteFriend_Should_SendInvitationEmail_When_InvitationSent() {
        // Arrange
        User inviter = createMockUser();
        String friendEmail = "friend@example.com";
        String expectedInvitationMessage = String.format(
                "Hello! You have been invited by %s %s to join our site. Click the following link to register: %s",
                inviter.getFirstName(), inviter.getLastName(), "http://localhost:8080/auth/register");

        // Act
        userService.inviteFriend(inviter, friendEmail);

        // Assert
        verify(emailService, times(1)).sendMessage(eq(friendEmail), eq("Invitation to Join"), eq(expectedInvitationMessage));
    }



}