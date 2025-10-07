package com.isfar.quotesnotes.notesection;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.isfar.quotesnotes.R;

public class EditNoteActivity extends AppCompatActivity {

    EditText noteEditText;
    Button saveButton;
    private NoteViewModel noteViewModel;
    private NoteEntity note;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.edit_note_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //===========================================================================


        //===========================================
        noteEditText = findViewById(R.id.noteEditText);
        saveButton = findViewById(R.id.saveButton);
        //===========================================


        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);


        // Check if we are editing or creating a note
        Intent intent = getIntent();
        if (intent.hasExtra("note")) {
            // Editing an existing note
            note = (NoteEntity) intent.getSerializableExtra("note");
            noteEditText.setText(note.getText());
            isEditMode = true;
        } else if (intent.hasExtra("defaultText")) {
            // Creating a new note with default text
            String defaultText = intent.getStringExtra("defaultText");
            note = new NoteEntity(defaultText);
            noteEditText.setText(defaultText);
        } else {
            // Creating a new empty note
            note = new NoteEntity("");
        }


        saveButton.setOnClickListener(v -> {
            String updatedText = noteEditText.getText().toString().trim();

            if (!updatedText.isEmpty()) {
                note.setText(updatedText);
                noteViewModel.insert(note);
                Toast.makeText(EditNoteActivity.this, isEditMode ? "Note updated" : "Note created", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(EditNoteActivity.this, "Please enter text", Toast.LENGTH_SHORT).show();
            }

        });


        // =========== Back Press Handling...========================
        getOnBackPressedDispatcher().addCallback(EditNoteActivity.this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                String updatedText = noteEditText.getText().toString().trim();

                if (!updatedText.isEmpty()) {
                    note.setText(updatedText);
                    noteViewModel.insert(note);
                    Toast.makeText(EditNoteActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    finish();
                }

            }
        });



    }// onCreate End here....



}// Main Class End here.......