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
    public static final String ASSIGNMENT_NOT_FOUND = "Assignment not found";
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
    public List<Assignment> getByTeacherForGrade(int teacherId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Assignment> query = session.createQuery("from Assignment where lecture.teacher.id= :teacherId", Assignment.class);
            query.setParameter("teacherId", teacherId);
            List<Assignment> assignments=query.list();
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
    public List<Assignment> getByUserSubmitted(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Assignment> query = session.createQuery("from Assignment where user.id= :userId", Assignment.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    @Override
    public List<Assignment> getByUserSubmittedToCourse(int userId, int courseId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Assignment> query = session.createQuery("from Assignment a where a.user.id= :userId AND a.lecture.course.id =: courseId", Assignment.class);
            query.setParameter("userId", userId);
            query.setParameter("courseId", courseId);
            return query.list();
        }
    }

    @Override
    public Assignment getByUserSubmittedToLecture(int userId, int lectureId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Assignment> query = session.createQuery("from Assignment a where a.user.id= :userId AND a.lecture.id =: lectureId", Assignment.class);
            query.setParameter("userId", userId);
            query.setParameter("lectureId", lectureId);
            List<Assignment> result=query.list();
            if (result.isEmpty()){
                throw new EntityNotFoundException(ASSIGNMENT_NOT_FOUND);
            }
            return result.get(0);
        }
    }

    @Override
    public List<Assignment> getAllAssignmentsForCourse(int courseId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Assignment> query = session.createQuery("from Assignment a where a.lecture.course.id =: courseId", Assignment.class);
            query.setParameter("courseId", courseId);

            return query.list();
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

    @Override
    public Lecture update(Assignment assignment) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(assignment);
            session.getTransaction().commit();
        }
        return assignment.getLecture();
    }

    @Override
    public Assignment grade(Assignment assignment) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(assignment);
            session.getTransaction().commit();
        }
        return assignment;
    }
}
