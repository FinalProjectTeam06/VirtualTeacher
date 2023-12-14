package com.example.finalprojectvirtualteacher.repositories;

import com.example.finalprojectvirtualteacher.models.Enrollment;
import com.example.finalprojectvirtualteacher.repositories.contracts.EnrollmentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnrollmentRepositoryImpl implements EnrollmentRepository {
    private final SessionFactory sessionFactory;

    public EnrollmentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Enrollment> getAllFinished(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Enrollment> query = session.createQuery("from Enrollment e where e.isFinished= true AND e.user.id=: userId", Enrollment.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }
}
