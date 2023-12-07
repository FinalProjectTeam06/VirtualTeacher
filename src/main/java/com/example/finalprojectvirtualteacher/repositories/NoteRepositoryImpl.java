package com.example.finalprojectvirtualteacher.repositories;

import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.Note;
import com.example.finalprojectvirtualteacher.repositories.contracts.NoteRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoteRepositoryImpl implements NoteRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public NoteRepositoryImpl(SessionFactory session) {
        this.sessionFactory = session;
    }


    @Override
    public List<Note> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Note> query = session.createQuery("from Note", Note.class);
            return query.list();
        }
    }

    @Override
    public Note getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Note note = session.get(Note.class, id);
            if (note == null){
                throw new EntityNotFoundException("Note",id);
            }
            return note;
        }
    }
    @Override
    public Note createNote(Note note) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(note);
            session.getTransaction().commit();
        }
        return note;
    }
    @Override
    public Note updateNote(Note note) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(note);
            session.getTransaction().commit();
        }
        return note;
    }

    @Override
    public Note deleteNote(int id) {
        try (Session session = sessionFactory.openSession()) {
            Note note = session.get(Note.class, id);
            if (note == null) {
                throw new EntityNotFoundException("Note", id);
            }
            session.beginTransaction();
            session.remove(note);
            session.getTransaction().commit();
            return note;
        }
    }

    @Override
    public List<Note> getByUserId(int userId) {
        try (Session session = sessionFactory.openSession()){
            Query<Note> query = session.createQuery("from Note where " +
                    "userId = :userId", Note.class);
            query.setParameter("userId",userId);
            return query.getResultList();
        }
    }




}