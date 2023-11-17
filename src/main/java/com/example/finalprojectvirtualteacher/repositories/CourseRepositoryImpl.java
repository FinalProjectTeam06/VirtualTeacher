package com.example.finalprojectvirtualteacher.repositories;

import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.repositories.contracts.CourseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseRepositoryImpl implements CourseRepository {
    private final SessionFactory sessionFactory;

    public CourseRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Course> getAll() {
        try (Session session= sessionFactory.openSession()){
            Query<Course> query=session.createQuery("from Course", Course.class);
            return query.list();
        }
    }

    @Override
    public Course getById(int id) {
        try (Session session= sessionFactory.openSession()){
            Course course=session.get(Course.class, id);
            if (course== null) {
                throw new EntityNotFoundException("Course", id);
            }
            return course;
        }
    }

    @Override
    public Course create(Course course) {
        try (Session session= sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(course);
            session.getTransaction().commit();
        }
        return course;
    }

    @Override
    public Course update(Course course) {
        try (Session session= sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(course);
            session.getTransaction().commit();
        }
        return course;
    }

    @Override
    public void delete(Course course) {
        try (Session session= sessionFactory.openSession()){
            session.beginTransaction();
            session.remove(course);
            session.getTransaction().commit();
        }
    }
}
