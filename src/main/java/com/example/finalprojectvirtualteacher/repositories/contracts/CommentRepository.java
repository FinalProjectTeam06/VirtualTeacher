package com.example.finalprojectvirtualteacher.repositories.contracts;

import com.example.finalprojectvirtualteacher.models.Comment;

import java.util.List;

public interface CommentRepository {
    List<Comment> getAll();

    Comment getByd(int commentId);

    Comment create(Comment comment);

    Comment update(Comment comment);

    void delete(int id);

    List<Comment> getByCourseId(int courseId);

    long getCourseCommentsCount();
}
