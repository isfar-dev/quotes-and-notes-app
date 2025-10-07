package com.isfar.quotesnotes.notesection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.isfar.quotesnotes.R;
import java.util.ArrayList;


public class NotesFragment extends Fragment {

    private RecyclerView notesRecyclerView;
    private FloatingActionButton mainFab;
    private ExtendedFloatingActionButton fabICan, fabIHaveTo;
    TextView tvNoNotes;
    private NoteAdapter noteAdapter;
    private NoteViewModel noteViewModel;
    boolean isFabExpanded;
    String PREFS_NAME = "AppPrefs";
    String KEY_NOTES_DIALOG_SHOWN = "NotesDialogShown";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        //================================================
        notesRecyclerView = view.findViewById(R.id.notesRecyclerView);
        mainFab = view.findViewById(R.id.mainFab);
        fabICan = view.findViewById(R.id.fabICan);
        fabIHaveTo = view.findViewById(R.id.fabIHaveTo);
        tvNoNotes = view.findViewById(R.id.tv_no_notes);
        //================================================






        // Showing the Dialog Of Swipe to Delete ====================
        if (shouldShowDialog()) {
            showWelcomeDialog();
        }


        //Fab Icon =====================
        isFabExpanded = false;
        fabICan.setVisibility(View.GONE);
        fabIHaveTo.setVisibility(View.GONE);

        // Handle FloatingActionButton animations and clicks
        setupFabBehavior();


        //=======================================
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);


        //============ set Adapter =========================================
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        noteAdapter = new NoteAdapter(new ArrayList<>(), note -> {

            // Navigate to EditNoteActivity
            Intent intent = new Intent(getActivity(), EditNoteActivity.class);
            intent.putExtra("note", note);
            startActivity(intent);

        });
        notesRecyclerView.setAdapter(noteAdapter);



        //======== Delete Note ===========================
        setupSwipeToDelete(notesRecyclerView, noteAdapter, requireContext());


        // Observe notes from ViewModel ===============================
        noteViewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> {

            noteAdapter.setNotes(notes);

            if (notes.isEmpty()) {
                tvNoNotes.setVisibility(View.VISIBLE);
            }else {
                tvNoNotes.setVisibility(View.GONE);
            }

        });
        //============================================================




        return view;


    }//onCreate End...


//============== Show Dialog of Swipe to Delete One Time =======================================
    private boolean shouldShowDialog() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return !sharedPreferences.getBoolean(KEY_NOTES_DIALOG_SHOWN, false);
    }

    private void showWelcomeDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Welcome to Notes")
                .setMessage("Swipe left to delete notes")
                .setPositiveButton("Got it", (dialog, which) -> {
                    // Update the SharedPreferences flag
                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    sharedPreferences.edit().putBoolean(KEY_NOTES_DIALOG_SHOWN, true).apply();
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }
//============== Show Dialog of Swipe to Delete One Time =======================================


//========== FAB ONCLICK =================================================================
    private void setupFabBehavior() {

        Animation animationOpen = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open);
        Animation animationClose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close);


        // main fab onClick....
        mainFab.setOnClickListener(v -> {
            if (!isFabExpanded) {
                fabICan.show();
                fabIHaveTo.show();
                mainFab.startAnimation(animationOpen);
                isFabExpanded = true;
            } else {
                fabICan.hide();
                fabIHaveTo.hide();
                mainFab.startAnimation(animationClose);
                isFabExpanded = false;
            }

        });// main fab onClick end...


        // fab I Can onClick...
        fabICan.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), EditNoteActivity.class);
            intent.putExtra("defaultText", "I can...");
            startActivity(intent);

            fabICan.hide();
            fabIHaveTo.hide();
            mainFab.startAnimation(animationClose);
            isFabExpanded = false;

        });// fab I Can onClick end...


        // fab I Have To onClick...
        fabIHaveTo.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), EditNoteActivity.class);
            intent.putExtra("defaultText", "I have to...");
            startActivity(intent);

            fabICan.hide();
            fabIHaveTo.hide();
            mainFab.startAnimation(animationClose);
            isFabExpanded = false;

        });// fab I Have To onClick end...


    }
//========== FAB ONCLICK End =============================================================


// ========= SWIPE TO DELETE =============================================================
    public void setupSwipeToDelete(RecyclerView recyclerView, NoteAdapter noteAdapter, Context context) {
        // Define the swipe callback
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            private final Drawable deleteIcon = ContextCompat.getDrawable(context, R.drawable.delete); // Your dustbin icon
            private final ColorDrawable background = new ColorDrawable(context.getResources().getColor(android.R.color.holo_red_dark));

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; // No drag-and-drop support
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Handle note deletion
                int position = viewHolder.getAdapterPosition();
                NoteEntity note = noteAdapter.getNoteAt(position); // Assume a method in adapter
                noteViewModel.delete(note); // Delete from database
                noteAdapter.notifyItemRemoved(position);
                Toast.makeText(requireContext(), "Note deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                // Draw background and icon
                View itemView = viewHolder.itemView;
                int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;

                if (dX < 0) { // Swiping left
                    background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    deleteIcon.setBounds(itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth(),
                            itemView.getTop() + iconMargin,
                            itemView.getRight() - iconMargin,
                            itemView.getBottom() - iconMargin);
                } else {
                    background.setBounds(0, 0, 0, 0); // No background when swiping right
                }

                background.draw(c);
                deleteIcon.draw(c);
            }
        };

        // Attach the ItemTouchHelper to the RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(notesRecyclerView);
    }
// ========= SWIPE TO DELETE End =============================================================


}//Main class End...