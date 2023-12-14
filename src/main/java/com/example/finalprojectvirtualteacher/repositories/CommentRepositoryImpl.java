package com.example.finalprojectvirtualteacher.repositories;

import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.Assignment;
import com.example.finalprojectvirtualteacher.models.Comment;
import com.example.finalprojectvirtualteacher.repositories.contracts.CommentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    public Comment getById(int commentId) {
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
        Comment commentToDelete = getById(id);
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
    public void deleteAllCommentsFromUserAndLecture(int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            MutationQuery query1 = session.createMutationQuery(
                    "delete from Comment c where c.creator.id= :userId or c.lecture.teacher.id= :userId");
            query1.setParameter("userId", userId);
            query1.executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteAllCommentsFromCourse(int courseId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            MutationQuery query1 = session.createMutationQuery(
                    "delete from Comment c where c.lecture.course.id= :courseId");
            query1.setParameter("courseId", courseId);
            query1.executeUpdate();
            session.getTransaction().commit();
        }
    }
}



