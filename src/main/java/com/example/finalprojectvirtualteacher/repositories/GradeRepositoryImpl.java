package com.example.finalprojectvirtualteacher.repositories;

import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.Grade;
import com.example.finalprojectvirtualteacher.repositories.contracts.GradeRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class GradeRepositoryImpl implements GradeRepository {
    private final SessionFactory sessionFactory;

    public GradeRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Grade getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Grade grade = session.get(Grade.class, id);
            if (grade == null) {
                throw new EntityNotFoundException("Grade", id);
            }
            return grade;
        }
    }
}
