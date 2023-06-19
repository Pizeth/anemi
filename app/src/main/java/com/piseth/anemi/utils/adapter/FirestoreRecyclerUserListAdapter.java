package com.piseth.anemi.utils.adapter;

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
import com.piseth.anemi.utils.model.User;

import org.jetbrains.annotations.NotNull;

public class FirestoreRecyclerUserListAdapter extends FirestoreRecyclerAdapter <User, FirestoreRecyclerUserListAdapter.UserListViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private FirestoreRecyclerUserListAdapter.OnUserListClickListener listener;
    public FirestoreRecyclerUserListAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FirestoreRecyclerUserListAdapter.UserListViewHolder holder, int position, @NonNull User model) {
        User user = getItem(position);
        Glide.with(holder.picture.getContext()).load(user.getPhoto()).into(holder.picture);
        holder.username.setText(user.getUsername());
        holder.phone.setText(user.getPhone());
    }

    @NonNull
    @Override
    public FirestoreRecyclerUserListAdapter.UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false);
        return new UserListViewHolder(view);
    }
    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    public String getDocumentId(int position) {
        return getSnapshots().getSnapshot(position).getId();
    }

//    Quick way to delete list item
    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public interface OnUserListClickListener {
        void onUpdate(int p);
        void onDelete(int p);
    }

    public void setOnUserListClickListener(OnUserListClickListener listener) {
        this.listener = listener;
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        TextView username, phone;
        MaterialButton btnUpdate, btnDelete;
        public UserListViewHolder(@NotNull View viewItem) {
            super(viewItem);
            picture = viewItem.findViewById(R.id.profilePicture);
            username = viewItem.findViewById(R.id.txtUsername);
            phone = viewItem.findViewById(R.id.txtPhone);
            btnUpdate = viewItem.findViewById(R.id.btnUpdateUser);
            btnDelete = viewItem.findViewById(R.id.btnDeleteUser);

            btnUpdate.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if(position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onUpdate(position);
                }
            });
            btnDelete.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDelete(position);
                }
            });
//            viewItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onUserListClickListener.onUpdate(getLayoutPosition());/////////DIALOG LISTENER????
//                    onUserListClickListener.onDelete(getLayoutPosition());
//                }
//            });
        }

//        @Override
//        public void onClick(View view) {
//            int id = view.getId();
//            if (id == R.id.btnUpdateUser) {
//                onUserListClickListener.onUpdate(this.getLayoutPosition());
//            } else if (id == R.id.btnDeleteUser) {
//                onUserListClickListener.onDelete(this.getLayoutPosition());
//            }
//        }
    }
}
