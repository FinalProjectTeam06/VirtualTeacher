package com.example.finalprojectvirtualteacher.service;


import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.helpers.mappers.NoteMapper;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.Note;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.repositories.contracts.NoteRepository;
import com.example.finalprojectvirtualteacher.services.NoteServiceImpl;
import org.aspectj.weaver.ast.Not;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.finalprojectvirtualteacher.Helpers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceImplTest {


    @InjectMocks
    NoteServiceImpl noteService;
    @Mock
    NoteRepository noteRepository;
    @Mock
    NoteMapper noteMapper;



    @Test
    void getAll_Should_CallRepository(){
        Note note = createNote();
        Note note1 = createNote();
        List<Note> list = new ArrayList<>();
        list.add(note);
        list.add(note1);

        when(noteRepository.getAll()).thenReturn(list);

        List<Note> result = noteService.getAll();

        assertEquals(result,list);
        verify(noteRepository,times(1)).getAll();


    }

    @Test
    void getById_Should_CallRepository_And_Return_Note(){
        Note note = createNote();

        when(noteRepository.getById(note.getNoteId())).thenReturn(note);

        noteService.getById(note.getNoteId());

        verify(noteRepository,times(1)).getById(note.getNoteId());
    }

    @Test
    void getByUserId_Should_Return_ListOfNotes(){
        User user = createMockUser();
        Note note = createNote();
        Note note1 = createNote();
        note.setUserId(user.getId());
        note1.setUserId(user.getId());
        List<Note> list = new ArrayList<>();
        list.add(note);
        list.add(note1);

        when(noteRepository.getByUserId(user.getId())).thenReturn(list);

        noteService.getByUserId(user.getId());

        verify(noteRepository,times(1)).getByUserId(user.getId());



    }
    @Test
    void create_Note_Should_CallRepository(){
        Note note = createNote();

        when(noteRepository.createNote(note)).thenReturn(note);

        noteService.create(note);

        verify(noteRepository,times(1)).createNote(note);
    }

    @Test
    void update_Note_Should_CallRepository(){
        Note note = createNote();
        User user = createMockUser();
        note.setUserId(user.getId());

        when(noteRepository.updateNote(note)).thenReturn(note);

        noteService.update(note,user);

        verify(noteRepository,times(1)).updateNote(note);
    }

    @Test
    void delete_note_Should_Successful_Delete(){
        Note note = createNote();
        User user = createMockUser();
        note.setUserId(user.getId());

        when(noteRepository.deleteNote(note.getNoteId())).thenReturn(note);

        noteService.delete(user, note.getNoteId());

        verify(noteRepository,times(1)).deleteNote(note.getNoteId());
    }


}
