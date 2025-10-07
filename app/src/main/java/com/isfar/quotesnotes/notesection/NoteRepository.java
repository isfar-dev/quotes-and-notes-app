package com.isfar.quotesnotes.notesection;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.isfar.quotesnotes.AppDatabase;

import java.util.List;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<NoteEntity>> allNotes;

    public NoteRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(NoteEntity note) {
        AppDatabase.executor.execute(() -> noteDao.insert(note));
    }

    public void update(NoteEntity note) {
        AppDatabase.executor.execute(() -> noteDao.update(note));
    }

    public void delete(NoteEntity note) {
        AppDatabase.executor.execute(() -> noteDao.delete(note));
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }
}
