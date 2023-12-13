package com.example.finalprojectvirtualteacher.repositories.contracts;

import com.example.finalprojectvirtualteacher.models.Note;

import java.util.List;

public interface NoteRepository {
    List<Note> getAll();

    Note getById(int id);

    Note createNote(Note note);

    Note updateNote(Note note);

    Note deleteNote(int id);

    List<Note> getByUserId(int userId);

    void deleteAllNotesByUser(int userId);
}
