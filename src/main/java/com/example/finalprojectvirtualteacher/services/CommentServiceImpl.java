package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.models.Comment;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.repositories.contracts.CommentRepository;
import com.example.finalprojectvirtualteacher.services.contacts.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    public static final String PERMISSION_ERROR = "You don't have permission.";
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.getAll();
    }

    @Override
    public Comment getById(int id) {
        return commentRepository.getById(id);
    }

    @Override
    public Comment create(Comment comment) {
        commentRepository.create(comment);
        return comment;
    }

    @Override
    public Comment update(User creator, Comment comment) {
        checkPermission(comment, creator);
       return commentRepository.update(comment);

    }
    @Override
    public void delete(User user, int id){
        Comment commentToDelete = commentRepository.getById(id);
        checkPermission(commentToDelete,user);
        commentRepository.delete(id);
    }

    @Override
    public void deleteAllCommentsFromUserAndLecture(int userId){
        commentRepository.deleteAllCommentsFromUserAndLecture(userId);
    }

    @Override
    public List<Comment> getByCourseId(int courseId){
        return commentRepository.getByCourseId(courseId);
    }

    public void checkPermission(Comment comment, User user) {
        if (!comment.getCreator().equals(user) && user.getRole().getId()!=3) {
            throw new AuthorizationException(PERMISSION_ERROR);
        }
    }
}
