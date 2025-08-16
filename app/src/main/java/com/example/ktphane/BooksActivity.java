package com.example.ktphane;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class BooksActivity extends AppCompatActivity {

    private EditText titleInput, authorInput;
    private Spinner genreSpinner;
    private Button addButton;
    private ListView listView;

    private ArrayList<Book> bookList;
    private ArrayAdapter<String> adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        titleInput = findViewById(R.id.titleInput);
        authorInput = findViewById(R.id.authorInput);
        genreSpinner = findViewById(R.id.genreSpinner);
        addButton = findViewById(R.id.addButton);
        listView = findViewById(R.id.listView);

        dbHelper = new DatabaseHelper(this);

        String[] genres = {"Roman", "Hikaye", "Bilim", "Felsefe"};
        genreSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genres));

        loadBooks();

        addButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString();
            String author = authorInput.getText().toString();
            String genre = genreSpinner.getSelectedItem().toString();

            if(!title.isEmpty() && !author.isEmpty()){
                Book newBook = new Book(0, title, author, genre);
                long id = dbHelper.addBook(newBook);
                newBook = new Book((int)id, title, author, genre);
                bookList.add(newBook);
                adapter.add(title + " - " + author);
                adapter.notifyDataSetChanged();

                titleInput.setText("");
                authorInput.setText("");
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Book selectedBook = bookList.get(position);
            Intent intent = new Intent(BooksActivity.this, BookDetailActivity.class);
            intent.putExtra("bookId", selectedBook.getId());
            startActivity(intent);
        });
    }

    private void loadBooks() {
        bookList = dbHelper.getAllBooks();
        ArrayList<String> titles = new ArrayList<>();
        for(Book b : bookList) titles.add(b.getTitle() + " - " + b.getAuthor());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks();
    }
}
