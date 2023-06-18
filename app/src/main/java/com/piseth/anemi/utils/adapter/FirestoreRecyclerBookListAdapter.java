package com.piseth.anemi.utils.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.piseth.anemi.R;
import com.piseth.anemi.utils.model.Book;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.AnemiUtils;

import org.jetbrains.annotations.NotNull;

public class FirestoreRecyclerBookListAdapter extends FirestoreRecyclerAdapter<Book, FirestoreRecyclerBookListAdapter.BookListViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    private FirestoreRecyclerBookListAdapter.OnBookListClickListener listener;
    private SharedPreferences loggedInUser;

    public FirestoreRecyclerBookListAdapter(@NonNull FirestoreRecyclerOptions<Book> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FirestoreRecyclerBookListAdapter.BookListViewHolder holder, int position, @NonNull Book model) {
        Glide.with(holder.cover.getContext()).load(model.getCover()).into(holder.cover);
        holder.title.setText(model.getBookName());
        holder.author.setText(model.getAuthor());
    }

    @NonNull
    @Override
    public FirestoreRecyclerBookListAdapter.BookListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list, parent, false);
        loggedInUser = parent.getContext().getSharedPreferences(AnemiUtils.LOGGED_IN_USER, MODE_PRIVATE);
        User user = AnemiUtils.getLoggedInUser(loggedInUser);
        if(user != null) {
            if(user.getUserRoleId() != AnemiUtils.ROLE_ADMIN) {
                view.findViewById(R.id.btnUpdateBook).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.btnDeleteBook).setVisibility(View.INVISIBLE);
            }
        }
        return new BookListViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getBookId();
    }

    public String getDocumentId(int position) {
        return getSnapshots().getSnapshot(position).getId();
    }

    public interface OnBookListClickListener {
        void onUpdate(int p);
        void onDelete(int p);
        void onView(int p);
    }

    public void setOnBookListClickListener(OnBookListClickListener listener) {
        this.listener = listener;
    }

    public class BookListViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title, author;
        MaterialButton btnUpdate, btnDelete, btnView;
        public BookListViewHolder(@NotNull View viewItem) {
            super(viewItem);
            cover = viewItem.findViewById(R.id.book_cover);
            title = viewItem.findViewById(R.id.txtTitle);
            author = viewItem.findViewById(R.id.txtAuthor);
            btnUpdate = viewItem.findViewById(R.id.btnUpdateBook);
            btnDelete = viewItem.findViewById(R.id.btnDeleteBook);
            btnView = viewItem.findViewById(R.id.btnView);

            btnUpdate.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if(position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onUpdate(position);
                }
            });
            btnDelete.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if(position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDelete(position);
                }
            });
            btnView.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if(position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onView(position);
                }
            });
        }
    }
}
