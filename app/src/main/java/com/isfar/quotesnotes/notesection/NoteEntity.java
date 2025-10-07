package com.isfar.quotesnotes.notesection;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "notes")
public class NoteEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String text;

    public NoteEntity(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
