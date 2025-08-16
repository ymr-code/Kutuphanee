package com.example.ktphane;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "kutuphane.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_BOOKS = "Books";
    public static final String COL_BOOK_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_AUTHOR = "author";
    public static final String COL_GENRE = "genre";
    public static final String COL_FAVORITE = "favorite";

    public static final String TABLE_NOTES = "Notes";
    public static final String COL_NOTE_ID = "id";
    public static final String COL_BOOK_ID_FK = "book_id";
    public static final String COL_NOTE_TEXT = "note_text";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBooks = "CREATE TABLE " + TABLE_BOOKS + " (" +
                COL_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_AUTHOR + " TEXT, " +
                COL_GENRE + " TEXT, " +
                COL_FAVORITE + " INTEGER DEFAULT 0)";
        db.execSQL(createBooks);

        String createNotes = "CREATE TABLE " + TABLE_NOTES + " (" +
                COL_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_BOOK_ID_FK + " INTEGER, " +
                COL_NOTE_TEXT + " TEXT, " +
                "FOREIGN KEY(" + COL_BOOK_ID_FK + ") REFERENCES " + TABLE_BOOKS + "(" + COL_BOOK_ID + "))";
        db.execSQL(createNotes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }

    // Kitap ekleme
    public long addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, book.getTitle());
        values.put(COL_AUTHOR, book.getAuthor());
        values.put(COL_GENRE, book.getGenre());
        values.put(COL_FAVORITE, book.isFavorite() ? 1 : 0);
        long id = db.insert(TABLE_BOOKS, null, values);
        db.close();
        return id;
    }

    // Tüm kitapları çek
    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKS, null);
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTHOR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_GENRE))
                );
                // favorite değerini set et
                int favoriteInt = cursor.getInt(cursor.getColumnIndexOrThrow(COL_FAVORITE));
                book.setFavorite(favoriteInt == 1);

                list.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // Tüm yazarları çek
    public ArrayList<String> getAllAuthors() {
        ArrayList<String> authors = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + COL_AUTHOR + " FROM " + TABLE_BOOKS, null);
        if (cursor.moveToFirst()) {
            do {
                authors.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return authors;
    }

    // Yazarına göre kitapları çek
    public ArrayList<Book> getBooksByAuthor(String author) {
        ArrayList<Book> books = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKS + " WHERE " + COL_AUTHOR + " = ?", new String[]{author});
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTHOR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_GENRE))
                );
                // favorite değerini set et
                int favoriteInt = cursor.getInt(cursor.getColumnIndexOrThrow(COL_FAVORITE));
                book.setFavorite(favoriteInt == 1);

                books.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return books;
    }

    // Not ekleme
    public long addNote(int bookId, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BOOK_ID_FK, bookId);
        values.put(COL_NOTE_TEXT, note);
        long id = db.insert(TABLE_NOTES, null, values);
        db.close();
        return id;
    }

    // Tüm notları çek
    public ArrayList<String> getAllNotes() {
        ArrayList<String> notesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES, null);
        if (cursor.moveToFirst()) {
            do {
                notesList.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE_TEXT)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notesList;
    }

    // Not güncelleme
    public void updateNote(int noteId, String newText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOTE_TEXT, newText);
        db.update(TABLE_NOTES, values, COL_NOTE_ID + "=?", new String[]{String.valueOf(noteId)});
        db.close();
    }

    // Kitaba özel notları çek
    public ArrayList<String> getNotesForBook(int bookId) {
        ArrayList<String> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES + " WHERE " + COL_BOOK_ID_FK + "=?", new String[]{String.valueOf(bookId)});
        if (cursor.moveToFirst()) {
            do {
                notes.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE_TEXT)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

    // Favorite güncelle
    public void updateFavorite(int bookId, boolean isFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_FAVORITE, isFavorite ? 1 : 0);
        db.update(TABLE_BOOKS, cv, COL_BOOK_ID + " = ?", new String[]{String.valueOf(bookId)});
        db.close();
    }
    public ArrayList<Book> getFavoriteBooks() {
        ArrayList<Book> favoriteBooks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKS,
                null,
                COL_FAVORITE + "=?",
                new String[]{"1"},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOK_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTHOR));
                String genre = cursor.getString(cursor.getColumnIndexOrThrow(COL_GENRE));
                boolean favorite = cursor.getInt(cursor.getColumnIndexOrThrow(COL_FAVORITE)) == 1;

                Book book = new Book(id, title, author, genre);
                book.setFavorite(favorite);
                favoriteBooks.add(book);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return favoriteBooks;
    }

}
