package com.piseth.anemi.utils.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.piseth.anemi.R;
import com.piseth.anemi.utils.model.Book;

import org.jetbrains.annotations.NotNull;

public class FirestoreRecyclerBookListAdapter extends FirebaseRecyclerAdapter<Book, FirestoreRecyclerBookListAdapter.BookListViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    private FirestoreRecyclerBookListAdapter.OnBookListClickListener onBookListClickListener;

    public FirestoreRecyclerBookListAdapter(@NonNull FirebaseRecyclerOptions<Book> options, FirestoreRecyclerBookListAdapter.OnBookListClickListener onBookListClickListener) {
        super(options);
        this.onBookListClickListener = onBookListClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull FirestoreRecyclerBookListAdapter.BookListViewHolder holder, int position, @NonNull Book model) {

    }

    @NonNull
    @Override
    public FirestoreRecyclerBookListAdapter.BookListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list, parent, false);
        FirestoreRecyclerBookListAdapter.BookListViewHolder holder = new FirestoreRecyclerBookListAdapter.BookListViewHolder(view, onBookListClickListener);
        return holder;
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
        FirestoreRecyclerBookListAdapter.OnBookListClickListener onBookListClickListener;
        public BookListViewHolder(@NotNull View viewItem, FirestoreRecyclerBookListAdapter.OnBookListClickListener listener) {
            super(viewItem);
            cover = viewItem.findViewById(R.id.book_cover);
            title = viewItem.findViewById(R.id.txtTitle);
            author = viewItem.findViewById(R.id.txtAuthor);
            btnUpdate = viewItem.findViewById(R.id.btnUpdateBook);
            btnDelete = viewItem.findViewById(R.id.btnDeleteBook);
            btnView = viewItem.findViewById(R.id.btnView);

            this.onBookListClickListener = listener;
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
