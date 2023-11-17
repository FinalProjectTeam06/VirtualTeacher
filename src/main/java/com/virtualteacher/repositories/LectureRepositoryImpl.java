package com.virtualteacher.repositories;

import com.virtualteacher.exceptions.EntityNotFoundException;
import com.virtualteacher.models.Lecture;
import com.virtualteacher.repositories.contracts.LectureRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LectureRepositoryImpl implements LectureRepository {

    private final SessionFactory sessionFactory;


    @Autowired
    public LectureRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Lecture> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Lecture> query = session.createQuery("from Lecture", Lecture.class);
            return query.list();
        }
    }
    @Override
    public Lecture getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Lecture lecture = session.get(Lecture.class, id);
            if (lecture == null) {
                throw new EntityNotFoundException("Lecture", id);
            }
            return lecture;
        }
    }

    @Override
    public Lecture create(Lecture lecture) {
            try (Session session = sessionFactory.openSession()){
                session.beginTransaction();
                session.persist(lecture);
                session.getTransaction().commit();
            }
        return lecture;
    }

    @Override
    public void delete(int id) {
        Lecture lectureToDelete = getById(id);
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(lectureToDelete);
            session.getTransaction().commit();
        }

    }

    @Override
    public Lecture update (Lecture updatedLecture) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(updatedLecture);
            session.getTransaction().commit();
        }

        return updatedLecture;
    }
}
