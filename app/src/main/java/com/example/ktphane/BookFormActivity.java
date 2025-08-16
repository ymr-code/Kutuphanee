package com.example.ktphane;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class BookFormActivity extends AppCompatActivity {

    private EditText titleInput, authorInput;
    private Spinner genreSpinner;
    private Button saveButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_form);

        titleInput = findViewById(R.id.titleInput);
        authorInput = findViewById(R.id.authorInput);
        genreSpinner = findViewById(R.id.genreSpinner);
        saveButton = findViewById(R.id.saveButton);

        dbHelper = new DatabaseHelper(this);

        String[] genres = {"Roman", "Hikaye", "Bilim", "Felsefe"};
        genreSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genres));

        saveButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String author = authorInput.getText().toString().trim();
            String genre = genreSpinner.getSelectedItem().toString();

            if (title.isEmpty() || author.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                return;
            }

            Book newBook = new Book(0, title, author, genre);
            long id = dbHelper.addBook(newBook);

            if (id != -1) {
                Toast.makeText(this, "Kitap eklendi!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Kitap eklenemedi!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
