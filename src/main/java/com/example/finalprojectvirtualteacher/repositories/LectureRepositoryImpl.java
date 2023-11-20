package com.example.finalprojectvirtualteacher.repositories;

import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.Note;
import com.example.finalprojectvirtualteacher.repositories.contracts.LectureRepository;
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
    public Note getNote(int lectureId, int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Note> query = session.createQuery("from Note where lecture.id=:lectureId AND user.id=:userId", Note.class);
            query.setParameter("lectureId", lectureId);
            query.setParameter("userId", userId);
            if (query.list().isEmpty()){
                throw new EntityNotFoundException("Note not found");
            }
            return query.list().get(0);
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
    public Lecture update (Lecture updatedLecture) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(updatedLecture);
            session.getTransaction().commit();
        }

        return updatedLecture;
    }

    @Override
    public void delete(int id) {
        Lecture lecture = getById(id);
        try (Session session = sessionFactory.openSession()){
            deleteLecture(id);
            session.beginTransaction();
            session.remove(lecture);
            session.getTransaction().commit();
        }
    }

    private void deleteLecture(int id){
        try(Session session = sessionFactory.openSession()){
    session.beginTransaction();
    Query<?>query = session.createNativeQuery(
            "delete from virtual_teacher.course_lectures where lecture_id=:id",Lecture.class);
    query.setParameter("id",id);
    query.executeUpdate();
    session.getTransaction().commit();

        }
    }



    @Override
    public Note createNote(Note note) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(note);
            session.getTransaction().commit();
        }
        return note;
    }

    @Override
    public Note updateNote(Note note) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(note);
            session.getTransaction().commit();
        }
        return note;
    }
}
