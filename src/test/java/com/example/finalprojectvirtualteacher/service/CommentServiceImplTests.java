package com.example.finalprojectvirtualteacher.service;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.models.Comment;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.repositories.contracts.CommentRepository;
import com.example.finalprojectvirtualteacher.services.CommentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.finalprojectvirtualteacher.Helpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTests {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    public void setUp() {
        commentService = new CommentServiceImpl(commentRepository);

    }

    @Test
    void getAll_Should_Return_ListOf_AllComments() {
        Comment comment = createMockComment();
        Comment comment1 = createMockComment();
        List<Comment> commentList = Arrays.asList(comment, comment1);

        when(commentRepository.getAll()).thenReturn(commentList);

        List<Comment> result = commentService.getAll();
        assertEquals(commentList, result);
        Mockito.verify(commentRepository, Mockito.times(1)).getAll();
    }

    @Test
    void getById_Should_Return_Comment() {
        Comment comment = createMockComment();
        comment.setId(1);

        when(commentRepository.getById(comment.getId())).thenReturn(comment);

        Comment result = commentService.getById(comment.getId());

        assertEquals(comment, result);

        Mockito.verify(commentRepository, Mockito.times(1)).getById(comment.getId());
    }

    @Test
    void create_ShouldCallRepository() {
        Comment comment = createMockComment();
        when(commentRepository.create(comment)).thenReturn(comment);

        Comment createdComment = commentService.create(comment);

        assertEquals(comment, createdComment);
    }

    @Test
    void update_Should_CallRepository_When_UserIsAdmin() {
        User adminUser = createMockAdmin();
        User user = createMockUser();
        Comment comment = createMockComment();
        comment.setCreator(user);

        when(commentRepository.update(comment))
                .thenReturn(comment);

        commentService.update(adminUser, comment);
        verify(commentRepository, times(1)).update(comment);


    }

    @Test
    void update_Should_CallRepository_When_UserIsCreator() {
        User user = createMockUser();
        Comment comment = new Comment();
        comment.setCreator(user);

        when(commentRepository.update(comment))
                .thenReturn(comment);

        Comment updatedComment = commentService.update(user, comment);

        assertEquals(comment, updatedComment);

    }

    @Test
    void update_Should_ThrowException_When_UserIsNotCreatorOrAdmin() {
        User user = createMockUser();
        Comment comment = createMockComment();
        comment.setCreator(user);
        User user1 = createMockUser();
        user1.setId(2);
        user1.setRole(createMockUserRole());


        Assertions.assertThrows(AuthorizationException.class, () -> commentService.update(user1, comment));
    }


    @Test
    void delete_Should_CallRepository_When_UserIsAdmin() {
        User admin = createMockAdmin();
        Comment commentToDelete = createMockComment();
        commentToDelete.setCreator(admin);

        when(commentRepository.getById(Mockito.anyInt()))
                .thenReturn(commentToDelete);

        commentService.delete(admin, commentToDelete.getId());

        Mockito.verify(commentRepository, Mockito.times(1))
                .delete(commentToDelete.getId());
    }

    @Test
    void delete_Should_CallRepository_WhenUserIsCreator() {
        User creator = createMockUser();
        Comment commentToDelete = createMockComment();
        commentToDelete.setCreator(creator);

        when(commentRepository.getById(Mockito.anyInt()))
                .thenReturn(commentToDelete);

        commentService.delete(creator, commentToDelete.getId());

        Mockito.verify(commentRepository, Mockito.times(1))
                .delete(commentToDelete.getId());
    }

    @Test
    void delete_Should_ThrowException_WhenUserIsNotCreatorOrAdmin() {
        User user = createMockUser(); // student
        Comment comment = createMockComment();
        comment.setCreator(user);
        User user1 = new User();
        user1.setRole(createMockUserRole());

        Mockito.when(commentRepository.getById(Mockito.anyInt()))
                .thenReturn(comment);

        assertThrows(AuthorizationException.class, () -> commentService.delete(user1, comment.getId()));
    }

    @Test
    void getByCourseId_Should_Return_ListOfComments() {
        Course course = createMockCourse();
        List<Comment> allComments = new ArrayList<>();

        when(commentRepository.getByCourseId(course.getId()))
                .thenReturn(allComments);
        List<Comment> result = commentService.getByCourseId(course.getId());

        assertEquals(allComments, result);

    }

    @Test
    void testCheckPermission_WithAdminRole_Success() {
        User adminUser = createMockAdmin();
        Comment comment = createMockComment();
        comment.setCreator(adminUser);

        // No exception should be thrown for an admin user
        assertDoesNotThrow(() -> commentService.checkPermission(comment, adminUser));
    }
}

