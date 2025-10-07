package com.isfar.quotesnotes.notesection;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository repository;
    private LiveData<List<NoteEntity>> allNotes;

    public NoteViewModel(Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public void insert(NoteEntity note) {
        repository.insert(note);
    }

    public void update(NoteEntity note) {
        repository.update(note);
    }

    public void delete(NoteEntity note) {
        repository.delete(note);
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }
}
