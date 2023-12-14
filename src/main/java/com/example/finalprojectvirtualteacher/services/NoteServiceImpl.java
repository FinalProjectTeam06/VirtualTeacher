package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.models.Note;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.repositories.contracts.NoteRepository;
import com.example.finalprojectvirtualteacher.services.contacts.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

private final NoteRepository noteRepository;

@Autowired
    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public List<Note> getAll(){
    return noteRepository.getAll();
    }
    @Override
    public Note getById(int id) {
        return noteRepository.getById(id);
    }

    @Override
    public Note create(Note note) {
        return noteRepository.createNote(note);
    }

    @Override
    public Note update(Note note, User author) {
        return noteRepository.updateNote(note);
    }

    @Override
    public Note delete(User authUser, int id) {
        return noteRepository.deleteNote(id);
    }

    @Override
    public List<Note> getByUserId (int userId) {
    return noteRepository.getByUserId(userId);
    }

    @Override
    public void deleteAllNotesByUser(int userId) {
        noteRepository.deleteAllNotesByUser(userId);
    }

    @Override
    public void deleteAllNotesByCourse(int courseId) {
        noteRepository.deleteAllNotesByCourse(courseId);
    }
}
