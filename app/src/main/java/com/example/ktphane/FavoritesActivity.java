package com.example.ktphane;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    private ListView listView;
    private BookAdapter adapter;
    private DatabaseHelper dbHelper;
    private ArrayList<Book> favoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        listView = findViewById(R.id.listViewFavorites);
        dbHelper = new DatabaseHelper(this);

        favoriteList = dbHelper.getFavoriteBooks();
        adapter = new BookAdapter(this, favoriteList, dbHelper);
        listView.setAdapter(adapter);
    }
}
