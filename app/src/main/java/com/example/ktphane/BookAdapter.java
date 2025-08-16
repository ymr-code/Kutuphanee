package com.example.ktphane;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends BaseAdapter {
    private static final String TAG = "BookAdapter";
    private Context context;
    private ArrayList<Book> bookList;
    private LayoutInflater inflater;
    private DatabaseHelper dbHelper;

    public BookAdapter(Context context, ArrayList<Book> bookList, DatabaseHelper dbHelper) {
        this.context = context;
        this.bookList = bookList;
        this.dbHelper = dbHelper;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() { return bookList.size(); }

    @Override
    public Object getItem(int position) { return bookList.get(position); }

    @Override
    public long getItemId(int position) { return bookList.get(position).getId(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.book_item, parent, false);
            holder = new ViewHolder();
            // *** BURADA XML'deki ID'lerle eşleşmeli ***
            holder.titleText = convertView.findViewById(R.id.bookTitle);
            holder.authorText = convertView.findViewById(R.id.bookAuthor);
            holder.starImage = convertView.findViewById(R.id.starImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // güvenlik: position sınır kontrolü
        if (position < 0 || position >= bookList.size()) {
            Log.e(TAG, "Invalid position: " + position);
            return convertView;
        }

        Book book = bookList.get(position);
        String t = (book == null ? "NULL_BOOK" : book.getTitle());
        String a = (book == null ? "NULL_BOOK" : book.getAuthor());
        Log.d(TAG, "pos=" + position + " -> title='" + t + "' author='" + a + "' favorite=" + (book!=null && book.isFavorite()));

        // Eğer holder.* null ise logla (hata ayıklama)
        if (holder.titleText != null && book != null) holder.titleText.setText(book.getTitle());
        else Log.w(TAG, "titleText is null or book is null at pos " + position);

        if (holder.authorText != null && book != null) holder.authorText.setText(book.getAuthor());
        else Log.w(TAG, "authorText is null or book is null at pos " + position);

        if (holder.starImage != null && book != null) {
            holder.starImage.setImageResource(book.isFavorite() ? R.drawable.ic_true_star : R.drawable.ic_false_star);
            holder.starImage.setOnClickListener(v -> {
                boolean newFav = !book.isFavorite();
                book.setFavorite(newFav);
                dbHelper.updateFavorite(book.getId(), newFav);
                holder.starImage.setImageResource(newFav ? R.drawable.ic_true_star : R.drawable.ic_false_star);
            });
        } else {
            Log.w(TAG, "starImage null at pos " + position);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView titleText;
        TextView authorText;
        ImageView starImage;
    }
}
