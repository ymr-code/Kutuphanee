package com.example.ktphane;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button bookButton, notesButton,favoritesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookButton = findViewById(R.id.bookButton);
        notesButton = findViewById(R.id.notesButton);
        favoritesButton = findViewById(R.id.favoritesButton);


        bookButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BooksActivity.class);
            startActivity(intent);
        });

        notesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotesActivity.class);
            startActivity(intent);
        });

        favoritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });
    }
}
