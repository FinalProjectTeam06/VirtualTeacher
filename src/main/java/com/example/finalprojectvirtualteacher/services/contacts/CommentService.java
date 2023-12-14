package com.example.finalprojectvirtualteacher.services.contacts;

import com.example.finalprojectvirtualteacher.models.Comment;
import com.example.finalprojectvirtualteacher.models.User;

import java.util.List;

public interface CommentService {
    List<Comment> getAll();

    Comment getById(int id);

    Comment create(Comment comment);

    Comment update(User creator, Comment comment);

    void delete(User user, int id);

    List<Comment> getByCourseId(int courseId);

    void deleteAllCommentsFromUserAndLecture(int userId);
    void deleteAllCommentsFromCourse(int courseId);
}
