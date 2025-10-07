package com.isfar.quotesnotes;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.isfar.quotesnotes.notesection.NoteDao;
import com.isfar.quotesnotes.notesection.NoteEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// Define the database with entities and version
@Database(entities = {NoteEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Abstract method for NoteDao
    public abstract NoteDao noteDao();

    // Singleton instance
    private static volatile AppDatabase INSTANCE;

    // Executor service for background tasks
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Get the instance of the database
    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "notes_database"
                            )
                            .fallbackToDestructiveMigration() // Clears database if schema changes
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
