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

public class CustomRecyclerUserListAdapter extends RecyclerView.Adapter<CustomRecyclerUserListAdapter.UserListViewHolder> {

    Context context;
    private List<User> users;
    private LayoutInflater inflater;

    public CustomRecyclerUserListAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_list, parent, false);
        return new UserListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
        User user = users.get(position);
        holder.picture.setImageResource(user.getPhoto());
        holder.username.setText(user.getUsername());
        holder.phone.setText(user.getPhone());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserListViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        TextView username;
        TextView phone;
        public UserListViewHolder(@NotNull View viewItem) {
            super(viewItem);
            picture = viewItem.findViewById(R.id.profilePicture);
            username = viewItem.findViewById(R.id.txtUsername);
            phone = viewItem.findViewById(R.id.txtPhone);
        }
    }
}
