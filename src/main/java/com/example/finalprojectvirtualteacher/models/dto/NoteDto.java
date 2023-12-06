package com.example.finalprojectvirtualteacher.models.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

public class NoteDto {

    @Column(name = "note")
    @NotNull
    private String note;

    public NoteDto(){

    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
