package com.example.ktphane;

import java.util.ArrayList;

public class Book {

    private int id;
    private String title;
    private String author;
    private String genre;
    private ArrayList<String> notes;
    private boolean isFavorite;


    public Book(int id, String title, String author, String genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isFavorite = false;
        this.notes = new ArrayList<>();

    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public ArrayList<String> getNotes() { return notes; }

    public void addNote(String note) { notes.add(note); }


    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { this.isFavorite = favorite; }
}
