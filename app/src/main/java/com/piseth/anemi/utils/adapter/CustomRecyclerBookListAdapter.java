package com.piseth.anemi.utils.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.piseth.anemi.utils.util.AnemiUtils;
import com.piseth.anemi.utils.util.DatabaseManageHandler;
import com.piseth.anemi.R;
import com.piseth.anemi.ui.fragments.dialog.DialogUpdateBookFragment;
import com.piseth.anemi.utils.model.Book;
import com.piseth.anemi.utils.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomRecyclerBookListAdapter extends RecyclerView.Adapter<CustomRecyclerBookListAdapter.BookListViewHolder> {

    Context context;
    private List<Book> books;
    private final DatabaseManageHandler db;
    private DialogUpdateBookFragment.OnCompletedDialogListener dialogListener;
    private CustomRecyclerBookListAdapter.OnBookListClickListener onBookListClickListener;
    private SharedPreferences loggedInUser;

    public CustomRecyclerBookListAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
        db = new DatabaseManageHandler(context);
    }
    public CustomRecyclerBookListAdapter(Context context, List<Book> books, DialogUpdateBookFragment.OnCompletedDialogListener dialogListener, CustomRecyclerBookListAdapter.OnBookListClickListener onBookListClickListener) {
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
        BookListViewHolder holder = new BookListViewHolder(view, onBookListClickListener, context);
        loggedInUser = context.getSharedPreferences(AnemiUtils.LOGGED_IN_USER, MODE_PRIVATE);
        User user = AnemiUtils.getLoggedInUser(loggedInUser);
        if(user != null) {
            if(user.getUserRoleId() != AnemiUtils.ROLE_ADMIN) {
                view.findViewById(R.id.btnUpdateBook).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.btnDeleteBook).setVisibility(View.INVISIBLE);
            }
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookListViewHolder holder, int position) {
        Book book = books.get(position);
        if(book != null) {
//            holder.cover.setImageBitmap(book.getCover());
            holder.title.setText(book.getBookName());
            holder.author.setText(book.getAuthor());
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

    public static class BookListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView cover;
        TextView title;
        TextView author;
        MaterialButton btnUpdate, btnDelete, btnView;
        OnBookListClickListener onBookListClickListener;
        Context context;
        public BookListViewHolder(@NotNull View viewItem, OnBookListClickListener listener, Context context) {
            super(viewItem);
            cover = viewItem.findViewById(R.id.book_cover);
            title = viewItem.findViewById(R.id.txtTitle);
            author = viewItem.findViewById(R.id.txtAuthor);
            btnUpdate = viewItem.findViewById(R.id.btnUpdateBook);
            btnDelete = viewItem.findViewById(R.id.btnDeleteBook);
            btnView = viewItem.findViewById(R.id.btnView);

            this.onBookListClickListener = listener;
            this.context = context;
            btnUpdate.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
            btnView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.btnUpdateBook) {
                onBookListClickListener.onUpdate(this.getLayoutPosition());
            } else if (id == R.id.btnDeleteBook) {
                onBookListClickListener.onDelete(this.getLayoutPosition());
            } else if (id == R.id.btnView) {
                onBookListClickListener.onView(this.getLayoutPosition());
            }
        }
    }
}
