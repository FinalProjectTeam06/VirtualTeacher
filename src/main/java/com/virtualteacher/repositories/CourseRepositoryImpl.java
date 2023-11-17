package com.virtualteacher.repositories;

import com.virtualteacher.exceptions.EntityNotFoundException;
import com.virtualteacher.models.Course;
import com.virtualteacher.repositories.contracts.CourseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.mapping.Component;
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
                throw new EntityNotFoundException("Tag", id);
            }
            return course;
        }
    }


}
