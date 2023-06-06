package com.piseth.anemi.utils.adapter;

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
import com.piseth.anemi.utils.model.User;

import org.jetbrains.annotations.NotNull;

public class FirestoreRecyclerUserListAdapter extends FirebaseRecyclerAdapter<User, FirestoreRecyclerUserListAdapter.UserListViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FirestoreRecyclerUserListAdapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FirestoreRecyclerUserListAdapter.UserListViewHolder holder, int position, @NonNull User model) {

    }

    @NonNull
    @Override
    public FirestoreRecyclerUserListAdapter.UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public interface OnUserListClickListener {
        void onUpdate(int p);
        void onDelete(int p);
        void onView(int p);
    }

    public static class UserListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView cover;
        TextView title;
        TextView author;
        MaterialButton btnUpdate, btnDelete, btnView;
        FirestoreRecyclerUserListAdapter.OnUserListClickListener onUserListClickListener;
        public UserListViewHolder(@NotNull View viewItem, FirestoreRecyclerUserListAdapter.OnUserListClickListener listener) {
            super(viewItem);
            cover = viewItem.findViewById(R.id.book_cover);
            title = viewItem.findViewById(R.id.txtTitle);
            author = viewItem.findViewById(R.id.txtAuthor);
            btnUpdate = viewItem.findViewById(R.id.btnUpdateBook);
            btnDelete = viewItem.findViewById(R.id.btnDeleteBook);
            btnView = viewItem.findViewById(R.id.btnView);

            this.onUserListClickListener = listener;
            btnUpdate.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
            btnView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.btnUpdateBook) {
                onUserListClickListener.onUpdate(this.getLayoutPosition());
            } else if (id == R.id.btnDeleteBook) {
                onUserListClickListener.onDelete(this.getLayoutPosition());
            } else if (id == R.id.btnView) {
                onUserListClickListener.onView(this.getLayoutPosition());
            }
        }
    }
}
