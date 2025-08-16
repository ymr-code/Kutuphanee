package com.example.ktphane;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class BookDetailActivity extends AppCompatActivity {
    private TextView titleText, authorText;
    private EditText noteInput;
    private Button addNoteButton;
    private ListView noteListView;
    private Book book;
    private ArrayAdapter<String> adapter;
    private DatabaseHelper dbHelper;
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        titleText = findViewById(R.id.titleText);
        authorText = findViewById(R.id.authorText);
        noteInput = findViewById(R.id.noteInput);
        addNoteButton = findViewById(R.id.addNoteButton);
        noteListView = findViewById(R.id.noteListView);
        dbHelper = new DatabaseHelper(this);
        bookId = getIntent().getIntExtra("bookId", -1);
        loadBook();
        addNoteButton.setOnClickListener(v -> {
            String note = noteInput.getText().toString();
            if (!note.isEmpty()) {
                dbHelper.addNote(bookId, note);
                noteInput.setText("");
                loadBook();


            }
        });
    }

    private void loadBook() {
        ArrayList<Book> books = dbHelper.getAllBooks();
        for (Book b : books) {
            if (b.getId() == bookId) {
                book = b;
                break;
            }
        }
        titleText.setText(book.getTitle());
        authorText.setText(book.getAuthor());
        ArrayList<String> notes = dbHelper.getNotesForBook(bookId);
        book.getNotes().addAll(notes);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, book.getNotes());
        noteListView.setAdapter(adapter);
    }
}

