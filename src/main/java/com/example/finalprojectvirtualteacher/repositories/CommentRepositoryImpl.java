package com.example.finalprojectvirtualteacher.repositories;

import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.Comment;
import com.example.finalprojectvirtualteacher.repositories.contracts.CommentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Comment> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery("from Comment", Comment.class);
            return query.list();
        }
    }

    @Override
    public Comment getByd(int commentId) {
        try (Session session = sessionFactory.openSession()) {
            Comment comment = session.get(Comment.class, commentId);
            if (comment == null) {
                throw new EntityNotFoundException("Comment", commentId);
            }
            return comment;
        }
    }


    @Override
    public Comment create(Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(comment);
            session.getTransaction().commit();
        }
        return comment;
    }

    @Override
    public Comment update(Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(comment);
            session.getTransaction().commit();
            return comment;
        }
    }

    @Override
    public void delete(int id) {
        Comment commentToDelete = getByd(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(commentToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Comment> getByCourseId(int lectureId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery
                    ("from Comment c where c.lecture =:lectureId", Comment.class);
            query.setParameter("lectureId", lectureId);
            return query.list();
        }
    }
    @Override
    public long getCourseCommentsCount() {
        try (Session session = sessionFactory.openSession()) {
            Query count = session.createQuery("SELECT count (u) from Comment u");
            return (long) count.getSingleResult();
        }
    }

}



