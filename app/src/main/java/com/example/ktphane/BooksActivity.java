package com.example.ktphane;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class BooksActivity extends AppCompatActivity {

    private ImageButton filterButton;
    private Button bookFormButton;
    private ListView listView;
    private ArrayList<Book> bookList;
    private BookAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        bookFormButton = findViewById(R.id.bookFormButton);
        filterButton = findViewById(R.id.filterButton);
        listView = findViewById(R.id.listView);
        dbHelper = new DatabaseHelper(this);

        // Kitap listesi ve adapter
        bookList = new ArrayList<>();
        adapter = new BookAdapter(this, bookList, dbHelper);
        listView.setAdapter(adapter);

        // Kitap ekleme butonu
        bookFormButton.setOnClickListener(v -> {
            Intent intent = new Intent(BooksActivity.this, BookFormActivity.class);
            startActivity(intent);
        });

        // Liste elemanına tıklama -> detay sayfasına git
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Book selectedBook = bookList.get(position);
            Intent intent = new Intent(BooksActivity.this, BookDetailActivity.class);
            intent.putExtra("bookId", selectedBook.getId());
            startActivity(intent);
        });

        // Filtreleme butonu
        filterButton.setOnClickListener(v -> showFilterDialog());

        // İlk açılışta kitapları yükle
        loadBooks();
    }
    private void loadBooks() {
        ArrayList<Book> fromDb = dbHelper.getAllBooks();


        bookList.clear();
        bookList.addAll(fromDb);
        adapter.notifyDataSetChanged();
    }


    private void showFilterDialog() {
        ArrayList<String> authors = dbHelper.getAllAuthors();
        authors.add(0, "Tümü");
        String[] authorsArray = authors.toArray(new String[0]);

        new AlertDialog.Builder(this)
                .setTitle("Filtrele")
                .setItems(authorsArray, (dialog, which) -> {
                    String selectedAuthor = authorsArray[which];
                    if (selectedAuthor.equals("Tümü")) {
                        loadBooks();
                    } else {
                        filterByAuthor(selectedAuthor);
                    }
                })
                .show();
    }

    private void filterByAuthor(String author) {
        bookList.clear();
        bookList.addAll(dbHelper.getBooksByAuthor(author));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks();
    }
}
