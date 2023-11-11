package com.piseth.anemi.utils.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.piseth.anemi.R;
import com.piseth.anemi.ui.fragments.dialog.DialogUpdateBookFragment;
import com.piseth.anemi.utils.model.Book;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.AnemiUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

// Switch to using FireStoreRecyclerView
public class CustomRecyclerBookListAdapter extends RecyclerView.Adapter<CustomRecyclerBookListAdapter.BookListViewHolder> {

    Context context;
    private List<Book> books;
    private DialogUpdateBookFragment.OnCompletedDialogListener dialogListener;
    private OnBookListClickListener listener;

    public CustomRecyclerBookListAdapter() {
    }

    public void setList(List<Book> book) {
        this.books = books;
    }

    public CustomRecyclerBookListAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public BookListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list, parent, false);
        SharedPreferences loggedInUser = context.getSharedPreferences(AnemiUtils.LOGGED_IN_USER, MODE_PRIVATE);
        User user = AnemiUtils.getLoggedInUser(loggedInUser);
        if(user != null) {
            if(user.getRoleId() != AnemiUtils.ROLE_ADMIN) {
                view.findViewById(R.id.btnUpdateBook).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.btnDeleteBook).setVisibility(View.INVISIBLE);
            }
        }
        return new BookListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookListViewHolder holder, int position) {
        Book book = books.get(position);
        if(book != null) {
            Log.d("Holder", "onBindViewHolder publish date: " + book.getPublishedDate());
            Log.d("Holder", "onBindViewHolder author name: " + book.getAuthor().getPenName());
            Log.d("Holder", "onBindViewHolder firstname name: " + book.getAuthor().getFirstName());
            Log.d("Holder", "onBindViewHolder lastname name: " + book.getAuthor().getLastName());
            Log.d("Holder", "onBindViewHolder lastname avatar: " + book.getAuthor().getAvatar());
            Glide.with(holder.cover.getContext()).load(book.getCover()).into(holder.cover);
            holder.title.setText(book.getBookTitle());
            holder.author.setText(book.getAuthor().getPenName());
        }
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
        void onView(int p);
    }

    public void setOnUserListClickListener(CustomRecyclerBookListAdapter.OnBookListClickListener listener) {
        this.listener = listener;
    }

    public class BookListViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title;
        TextView author;
        MaterialButton btnUpdate, btnDelete, btnView;
        public BookListViewHolder(@NotNull View viewItem) {
            super(viewItem);
            cover = viewItem.findViewById(R.id.book_cover);
            title = viewItem.findViewById(R.id.txtTitle);
            author = viewItem.findViewById(R.id.txtAuthor);
            btnUpdate = viewItem.findViewById(R.id.btnUpdateBook);
            btnDelete = viewItem.findViewById(R.id.btnDeleteBook);
            btnView = viewItem.findViewById(R.id.btnView);

            btnUpdate.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if(position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onUpdate(position);
                }
            });
            btnDelete.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if(position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDelete(position);
                }
            });
            btnView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if(position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onView(position);
                }
            });
        }
    }
}
