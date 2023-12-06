package com.example.finalprojectvirtualteacher.helpers.mappers;

import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.Note;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.NoteDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NoteMapper {

    public NoteMapper() {
    }

    public Note dtoToObj(NoteDto noteDto, Lecture lecture, User authUser) {
        Note note = new Note();
        note.setText(noteDto.getNote());
        note.setLecture(lecture);
        note.setUserId(authUser.getId());
        return note;
    }

    public NoteDto objToDto(Note note) {
        List<Note> notes = new ArrayList<>();
        notes.add(note);
        return listObjToDto(notes).get(0);
    }

    private List<NoteDto> listObjToDto(List<Note> notes) {
        List<NoteDto> result = new ArrayList<>();
        for (Note n : notes) {
            NoteDto noteDto = new NoteDto();
            noteDto.setNote(n.getText());
            result.add(noteDto);
        }
        return result;
    }

    public Note dtoToObjForUpdate(Note originalNote, NoteDto noteDto) {
        originalNote.setText(noteDto.getNote());
        return originalNote;
    }

}
