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

    public static final String TABLE_NOTES = "Notes";
    public static final String COL_NOTE_ID = "id";
    public static final String COL_BOOK_ID_FK = "book_id";
    public static final String COL_NOTE_TEXT = "note_text";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBooks = "CREATE TABLE " + TABLE_BOOKS + "("
                + COL_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT, "
                + COL_AUTHOR + " TEXT, "
                + COL_GENRE + " TEXT)";
        db.execSQL(createBooks);

        String createNotes = "CREATE TABLE " + TABLE_NOTES + "("
                + COL_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_BOOK_ID_FK + " INTEGER, "
                + COL_NOTE_TEXT + " TEXT, "
                + "FOREIGN KEY(" + COL_BOOK_ID_FK + ") REFERENCES " + TABLE_BOOKS + "(" + COL_BOOK_ID + "))";
        db.execSQL(createNotes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }

     public long addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, book.getTitle());
        values.put(COL_AUTHOR, book.getAuthor());
        values.put(COL_GENRE, book.getGenre());
        return db.insert(TABLE_BOOKS, null, values);
    }

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
                list.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public long addNote(int bookId, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BOOK_ID_FK, bookId);
        values.put(COL_NOTE_TEXT, note);
        return db.insert(TABLE_NOTES, null, values);
    }

     public ArrayList<String> getAllNotes(){
        ArrayList<String> notesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES, null);
        if(cursor.moveToFirst()){
            do{
                notesList.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE_TEXT)));
            } while(cursor.moveToNext());
        }
        cursor.close();
        return notesList;
    }


    public void updateNote(int noteId, String newText){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOTE_TEXT, newText);
        db.update(TABLE_NOTES, values, COL_NOTE_ID + "=?", new String[]{String.valueOf(noteId)});
    }

    public ArrayList<String> getNotesForBook(int bookId) {
        ArrayList<String> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES + " WHERE " + COL_BOOK_ID_FK + "=?",
                new String[]{String.valueOf(bookId)});
        if (cursor.moveToFirst()) {
            do {
                notes.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE_TEXT)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }
}
