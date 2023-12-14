package com.example.finalprojectvirtualteacher.services.contacts;

import com.example.finalprojectvirtualteacher.models.Note;
import com.example.finalprojectvirtualteacher.models.User;

import java.util.List;

public interface NoteService {
    List<Note> getAll();

    Note getById(int id);

    Note create(Note note);

    Note update(Note note, User author);

    Note delete(User authUser, int id);

    List<Note> getByUserId(int userId);

    void deleteAllNotesByUser(int userId);
    void deleteAllNotesByCourse(int courseId);
}
