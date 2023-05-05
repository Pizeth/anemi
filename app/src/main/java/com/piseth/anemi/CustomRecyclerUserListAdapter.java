package com.piseth.anemi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomRecyclerUserListAdapter extends RecyclerView.Adapter<CustomRecyclerUserListAdapter.UserListViewHolder> {

    private Context context;
    private List<User> users;
    private DatabaseManageHandler db;
    private LayoutInflater inflater;

    public CustomRecyclerUserListAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
        db = new DatabaseManageHandler(context);
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_list, parent, false);
        UserListViewHolder holder = new UserListViewHolder(view, new OnUserListClickListener() {
            @Override
            public void onUpdate(int p) {

                Log.d("Update: ", "Update button pressed " + getItemId(p));
                Toast.makeText(context, "Update pressed " + getItemId(p), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDelete(int p) {
                // Implement your functionality for onDelete here
                db.deleteUser((int)(getItemId(p)));
                Log.d("Update: ", "Delete button pressed " + getItemId(p));
                Toast.makeText(context, "Delete pressed " + getItemId(p), Toast.LENGTH_SHORT).show();
                users.remove(p);
                notifyDataSetChanged();
//                notifyItemRemoved(p);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
        User user = users.get(position);
        holder.picture.setImageBitmap(user.getPhoto());
        holder.username.setText(user.getUsername());
        holder.phone.setText(user.getPhone());
//        final long itemId = user.getId();
//        holder.itemView.setOnClickListener();
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onProductClickListener.onProductClick(view, itemId);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public long getItemId(int position) {
        return users.get(position).getId();
    }

    public static interface OnUserListClickListener {
        public void onUpdate(int p);
        public void onDelete(int p);
    }

    public static class UserListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView picture;
        TextView username, phone;
        MaterialButton btnUpdate, btnDelete;
        OnUserListClickListener onUserListClickListener;
        public UserListViewHolder(@NotNull View viewItem, OnUserListClickListener listener) {
            super(viewItem);
            picture = viewItem.findViewById(R.id.profilePicture);
            username = viewItem.findViewById(R.id.txtUsername);
            phone = viewItem.findViewById(R.id.txtPhone);
            btnUpdate = viewItem.findViewById(R.id.btnUpdateUser);
            btnDelete = viewItem.findViewById(R.id.btnDeleteUser);
            this.onUserListClickListener = listener;
            btnUpdate.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnUpdateUser:
                    onUserListClickListener.onUpdate(this.getLayoutPosition());
                    break;
                case R.id.btnDeleteUser:
                    onUserListClickListener.onDelete(this.getLayoutPosition());
                    break;
                default:
                    break;
            }
        }
    }
}
