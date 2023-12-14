package com.example.finalprojectvirtualteacher.repositories.contracts;

import com.example.finalprojectvirtualteacher.models.Comment;

import java.util.List;

public interface CommentRepository {
    List<Comment> getAll();

    Comment getById(int commentId);

    Comment create(Comment comment);

    Comment update(Comment comment);

    void delete(int id);

    List<Comment> getByCourseId(int courseId);

    long getCourseCommentsCount();

    void deleteAllCommentsFromUserAndLecture(int userId);

    void deleteAllCommentsFromCourse(int courseId);
}
