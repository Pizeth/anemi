package com.piseth.anemi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomRecyclerBookListAdapter extends RecyclerView.Adapter<CustomRecyclerBookListAdapter.BookListViewHolder> {

    Context context;
    private List<Book> books;
    private LayoutInflater inflater;

    public CustomRecyclerBookListAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public BookListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.book_list, parent, false);
        return new BookListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookListViewHolder holder, int position) {
        Book book = books.get(position);
        holder.cover.setImageBitmap(book.getCover());
        holder.title.setText(book.getBookName());
        holder.author.setText(book.getAuthor());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class BookListViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title;
        TextView author;
        public BookListViewHolder(@NotNull View viewItem) {
            super(viewItem);
            cover = viewItem.findViewById(R.id.profilePicture);
            title = viewItem.findViewById(R.id.txtUsername);
            author = viewItem.findViewById(R.id.txtPhone);
        }
    }
}
