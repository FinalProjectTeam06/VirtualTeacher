package com.example.finalprojectvirtualteacher.repositories;

import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.Assignment;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.Grade;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.repositories.contracts.AssignmentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AssignmentRepositoryImpl implements AssignmentRepository {
    private final SessionFactory sessionFactory;

    public AssignmentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Assignment> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Assignment> query = session.createQuery("from Assignment ", Assignment.class);
            return query.list();
        }
    }

    @Override
    public Assignment getById(int assignmentId) {
        try (Session session = sessionFactory.openSession()) {
            Assignment assignment = session.get(Assignment.class, assignmentId);
            if (assignment == null) {
                throw new EntityNotFoundException("Assignment", assignmentId);
            }
            return assignment;
        }
    }

    @Override
    public Lecture submitAssignment(Assignment assignment) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(assignment);
            session.getTransaction().commit();
        }
        return assignment.getLecture();
    }
}
