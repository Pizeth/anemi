package com.piseth.anemi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomRecyclerBookListAdapter extends RecyclerView.Adapter<CustomRecyclerBookListAdapter.BookListViewHolder> {

    Context context;
    private List<Book> books;
    private final DatabaseManageHandler db;
    private DialogUpdateBookFragment.DialogListener dialogListener;
    private CustomRecyclerBookListAdapter.OnBookListClickListener onBookListClickListener;

    public CustomRecyclerBookListAdapter(Context context, List<Book> books, DialogUpdateBookFragment.DialogListener dialogListener, CustomRecyclerBookListAdapter.OnBookListClickListener onBookListClickListener) {
        this.context = context;
        this.books = books;
        this.dialogListener = dialogListener;
        this.onBookListClickListener = onBookListClickListener;
        db = new DatabaseManageHandler(context);
    }

    @NonNull
    @Override
    public BookListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_list, parent, false);
        BookListViewHolder holder = new BookListViewHolder(view, onBookListClickListener);
        return holder;
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

    @Override
    public long getItemId(int position) {
        return books.get(position).getBookId();
    }

    public interface OnBookListClickListener {
        void onUpdate(int p);
        void onDelete(int p);
    }

    public static class BookListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView cover;
        TextView title;
        TextView author;
        MaterialButton btnUpdate, btnDelete;
        OnBookListClickListener onBookListClickListener;
        public BookListViewHolder(@NotNull View viewItem, OnBookListClickListener listener) {
            super(viewItem);
            cover = viewItem.findViewById(R.id.book_cover);
            title = viewItem.findViewById(R.id.txtTitle);
            author = viewItem.findViewById(R.id.txtAuthor);
            btnUpdate = viewItem.findViewById(R.id.btnUpdateBook);
            btnDelete = viewItem.findViewById(R.id.btnDeleteBook);
            this.onBookListClickListener = listener;
            btnUpdate.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnUpdateBook:
                    onBookListClickListener.onUpdate(this.getLayoutPosition());
                    break;
                case R.id.btnDeleteBook:
                    onBookListClickListener.onDelete(this.getLayoutPosition());
                    break;
                default:
                    break;
            }
        }
    }
}
