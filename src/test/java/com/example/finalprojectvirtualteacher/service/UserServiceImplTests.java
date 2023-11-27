package com.example.finalprojectvirtualteacher.service;


import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.exceptions.EntityDuplicateException;
import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.UserFilterOptions;
import com.example.finalprojectvirtualteacher.models.dto.UserDtoUpdate;
import com.example.finalprojectvirtualteacher.repositories.contracts.UserRepository;
import com.example.finalprojectvirtualteacher.services.UserServiceImpl;
import com.example.finalprojectvirtualteacher.services.contacts.CourseService;
import com.example.finalprojectvirtualteacher.services.contacts.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.example.finalprojectvirtualteacher.Helpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTests {

    private UserServiceImpl userService;
    private UserRepository userRepository;
    private CourseService courseServiceMock;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        courseServiceMock = mock(CourseService.class);
        userService = new UserServiceImpl(userRepository, courseServiceMock);
    }


    @Test
    void getAll_ShouldReturnListOfUsers_WhenFilterOptionsProvided() {
        // Arrange
        UserFilterOptions filterOptions = new UserFilterOptions("Doe", "john.doe@example.com", "John");
        List<User> mockUsers = new ArrayList<>();
        when(userRepository.getAll(filterOptions)).thenReturn(mockUsers);

        // Act
        List<User> result = userService.getAll(filterOptions);

        // Assert
        assertEquals(mockUsers, result);
    }
    @Test
    void getAll_ShouldReturnListOfUsers_WhenFilterOptionsIsNull() {
        // Arrange
        UserFilterOptions filterOptions = null;
        List<User> mockUsers = new ArrayList<>();
        when(userRepository.getAll(filterOptions)).thenReturn(mockUsers);

        // Act
        List<User> result = userService.getAll(filterOptions);

        // Assert
        assertEquals(mockUsers, result);
    }

    @Test
    void updateUser_ShouldUpdateLastName_WhenDtoLastNameIsNotNull() {
        // Arrange
        User user = createMockUser();
        User updatedUser = createMockUser();
        UserDtoUpdate userDtoUpdate = new UserDtoUpdate();
        userDtoUpdate.setLastName("NewLastName");

        // Act
        User user1 = userService.updateUser(user, updatedUser, userDtoUpdate);


    }

    @Test
    void updateUser_ShouldUpdatePassword_WhenDtoPasswordAndConfirmMatch() {
        // Arrange
        User user = createMockUser();
        User updatedUser = createMockUser();
        UserDtoUpdate userDtoUpdate = new UserDtoUpdate();
        userDtoUpdate.setPassword("newPassword");
        userDtoUpdate.setPasswordConfirm("newPassword");

        // Act
        User result = userService.updateUser(user, updatedUser, userDtoUpdate);


    }

    @Test
    void updateUser_ShouldNotUpdatePassword_WhenDtoPasswordAndConfirmDoNotMatch() {
        // Arrange
        User user = createMockUser();
        User updatedUser = createMockUser();
        UserDtoUpdate userDtoUpdate = new UserDtoUpdate();
        userDtoUpdate.setPassword("newPassword");
        userDtoUpdate.setPasswordConfirm("mismatchedPassword");

        // Act
        User result = userService.updateUser(user, updatedUser, userDtoUpdate);

    }


    @Test
    public void testGetAll() {
        // Mocking
        UserFilterOptions filterOptions = new UserFilterOptions(null, null, null);
        List<User> mockUsers = new ArrayList<>();
        when(userRepository.getAll(filterOptions)).thenReturn(mockUsers);

        // Testing
        List<User> result = userService.getAll(filterOptions);

        // Assertions
        assertEquals(mockUsers, result);
    }


    @Test
    public void get_Should_Return_Exception_When_Id_is_Null() {
        User user1 = createMockUser();
        int id = user1.getId();
        //    User user = createMockAdmin();
        when(userRepository.getById(id)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> userService.getById(id));
    }






    @Test
    public void get_Should_ReturnListOfUsers_When_UserHasPermissions() {
        // Mocking
        List<User> users = new ArrayList<>();
        users.add(createMockUser());
        UserFilterOptions filterOptions = new UserFilterOptions(null, null, null);
        when(userRepository.getAll(filterOptions)).thenReturn(users);

        // Testing
        List<User> result = userService.getAll(filterOptions);

        // Assertions
        assertEquals(users, result);

        // Verify that the getAll method was called with the correct filter options
        verify(userRepository, times(1)).getAll(filterOptions);
    }

    @Test
    public void testGetById_UserExists() {
        // Mocking
        int userId = 1;
        User mockUser = new User();
        when(userRepository.getById(userId)).thenReturn(mockUser);

        // Testing
        User result = userService.getById(userId);

        // Assertions
        assertEquals(mockUser, result);
    }

    @Test
    public void testGetById_UserNotExists() {
        // Mocking
        int userId = 1;
        when(userRepository.getById(userId)).thenReturn(null);

        // Testing
        assertThrows(EntityNotFoundException.class, () -> userService.getById(userId));
    }



    @Test
    public void createUser_Should_ThrowEntityDuplicateException_When_UsernameAlreadyExists() {
        // Mocking
        User existingUser = new User();
        existingUser.setEmail("existinguser@example.com");

        when(userRepository.getByEmail(existingUser.getEmail())).thenReturn(existingUser);

        // Testing and Assertions
        EntityDuplicateException exception = assertThrows(EntityDuplicateException.class,
                () -> userService.create(existingUser));

        assertEquals("User with username existinguser@example.com already exists.", exception.getMessage());

    }

    @Test
    void getByEmail_ShouldReturnUser_WhenEmailExists() {
        // Arrange
        String userEmail = "john.doe@example.com";
        User mockUser = new User();
        when(userRepository.getByEmail(userEmail)).thenReturn(mockUser);

        // Act
        User result = userService.getByEmail(userEmail);

        // Assert
        assertEquals(mockUser, result);
    }


    @Test
    public void testEnrollCourse() {
        // Mocking
        int courseId = 1;
        int userId = 1;
        User user = new User();
        Course mockCourse = new Course();

        // Initialize the courses set in the user
        user.setCourses(new HashSet<>());

        when(courseServiceMock.getById(courseId)).thenReturn(mockCourse);
        when(userRepository.updateUser(user)).thenReturn(user);

        // Testing
        User result = userService.enrollCourse(user, courseId);

        // Assertions
        assertTrue(result.getCourses().contains(mockCourse));
    }

    @Test
    void create_ShouldThrowEntityDuplicateException_WhenEmailAlreadyExists() {
        // Arrange
        User existingUser = new User();
        existingUser.setEmail("existinguser@example.com");

        when(userRepository.getByEmail(existingUser.getEmail())).thenReturn(existingUser);

        // Assert
        EntityDuplicateException exception = assertThrows(EntityDuplicateException.class,
                () -> userService.create(existingUser));

        assertEquals("User with username existinguser@example.com already exists.", exception.getMessage());
    }

    


    @Test
    public void deleteUser_Should_ThrowAuthorizationException_When_UserIsAdmin(){
        User user = createMockAdmin();
        int idToAdmin = user.getId();
        User userToDelete = createMockUserWithId(idToAdmin);

        when(userRepository.getById(idToAdmin)).thenReturn(userToDelete);

        userService.deleteUser(idToAdmin,user);

    }
    @Test
    void delete_Should_CallRepository_When_UserIsCreator() {
        // Arrange
        User user = createMockUser();
        User mock1 = createMockAdmin();

        when(userRepository.getById(Mockito.anyInt()))
                .thenReturn(user);

        // Act
        userService.deleteUser(user.getId(), mock1);

        // Assert
        Mockito.verify(userRepository, times(1))
                .deleteUser(user);
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
    void getAllTeachers_ShouldReturnListOfTeachers() {

        List<User> teachers = new ArrayList<>();
        when(userRepository.getAllTeachers()).thenReturn(teachers);

        List<User> result = userService.getAllTeachers();

        assertEquals(teachers, result);
    }

    @Test
    void create_userAlreadyExists() {
        // Arrange
        User existingUser = new User();
        when(userRepository.getByEmail(existingUser.getEmail())).thenReturn(existingUser);

        // Act & Assert
        assertThrows(EntityDuplicateException.class, () -> userService.create(existingUser));
    }

}