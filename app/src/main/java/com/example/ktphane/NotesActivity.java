package com.example.ktphane;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    private ListView notesListView;
    private EditText editNote;
    private Button updateButton;

    private ArrayList<String> notes;
    private ArrayAdapter<String> adapter;
    private DatabaseHelper dbHelper;
    private int selectedNoteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        notesListView = findViewById(R.id.notesListView);
        editNote = findViewById(R.id.editNote);
        updateButton = findViewById(R.id.updateButton);

        dbHelper = new DatabaseHelper(this);

        loadNotes();

        notesListView.setOnItemClickListener((parent, view, position, id) -> {
            selectedNoteId = position;
            editNote.setText(notes.get(position));
        });

        updateButton.setOnClickListener(v -> {
            if(selectedNoteId != -1){
                String newNote = editNote.getText().toString();


                dbHelper.updateNote(selectedNoteId + 1, newNote);
                notes.set(selectedNoteId, newNote);
                adapter.notifyDataSetChanged();
                editNote.setText("");
            }
        });
    }

    private void loadNotes(){
        notes = dbHelper.getAllNotes();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        notesListView.setAdapter(adapter);
    }
}
