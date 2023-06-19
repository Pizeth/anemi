package com.piseth.anemi.utils.adapter;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.piseth.anemi.R;
import com.piseth.anemi.ui.fragments.dialog.DialogUpdateUserFragment;
import com.piseth.anemi.utils.model.User;

import org.jetbrains.annotations.NotNull;

import de.danielbechler.diff.ObjectDifferBuilder;

// Switch to using FireStoreRecyclerView
//public class CustomRecyclerUserListAdapter extends ListAdapter<User, CustomRecyclerUserListAdapter.UserListViewHolder> {
public class CustomRecyclerUserListAdapter extends FirebaseRecyclerAdapter<User, CustomRecyclerUserListAdapter.UserListViewHolder> {

//    private final Context context;
//    private List<User> users;
//    private final DatabaseManageHandler db;
    private DialogUpdateUserFragment.OnUpdateCompletedDialogListener dialogListener;
    private OnUserListClickListener onUserListClickListener;
    private SharedPreferences loggedInUser;
//    private LayoutInflater inflater;

//    public CustomRecyclerUserListAdapter(Context context, DialogUpdateUserFragment.DialogListener dialogListener, OnUserListClickListener onUserListClickListener) {
//        super(DIFF_CALLBACK);
//        this.context = context;
////        this.users = users;
//        this.dialogListener = dialogListener;
//        this.onUserListClickListener = onUserListClickListener;
//        db = new DatabaseManageHandler(context);
//    }
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CustomRecyclerUserListAdapter(@NonNull FirebaseRecyclerOptions<User> options, DialogUpdateUserFragment.OnUpdateCompletedDialogListener dialogListener, OnUserListClickListener onUserListClickListener) {
        super(options);
        this.dialogListener = dialogListener;
        this.onUserListClickListener = onUserListClickListener;
//        db = new DatabaseManageHandler(context);
    }


    private static final DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return ObjectDifferBuilder.buildDefault().compare(oldItem, newItem).hasChanges();
        }
    };

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false);
        UserListViewHolder holder = new UserListViewHolder(view, onUserListClickListener);
//        UserListViewHolder holder = new UserListViewHolder(view, new OnUserListClickListener() {
//            @Override
//            public void onUpdate(int p) {
//
//                Log.d("Update: ", "Update button pressed " + getItemId(p));
//                Toast.makeText(context, "Update pressed " + getItemId(p), Toast.LENGTH_SHORT).show();
//
//
//                Bundle bundle = new Bundle();
//                bundle.putBoolean("notAlertDialog", true);
//                bundle.putLong("user_id", getItemId(p));
//                bundle.putInt("position", p);
//
//                DialogUserUpdateFragment dialogFragment = new DialogUserUpdateFragment(bundle, dialogListener);
////                dialogFragment.setTargetFragment(this, 0);
////                Log.d("Current target: " ,dialogFragment.getTargetFragment().toString());
//                dialogFragment.setArguments(bundle);
//                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager(); // instantiate your view context
//                FragmentTransaction ft = fragmentManager.beginTransaction();
//                Fragment prev = ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag("dialog");
//                if (prev != null) {
//                    ft.remove(prev);
//                }
//                ft.addToBackStack(null);
//
//                dialogFragment.show(ft, "dialog");
//            }
//
//            @Override
//            public void onDelete(int p) {
//                // Implement your functionality for onDelete here
//
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//                alertDialog.setTitle("Remove User");
//                alertDialog.setMessage("Delete this user??");
//                alertDialog.setPositiveButton("CANCEL", (dialog, which) -> dialog.cancel());
//                alertDialog.setNegativeButton("YES", (dialog, which) -> {
//                    Log.d("Update: ", "Delete button pressed " + getItemId(p));
//                    Toast.makeText(context, "Delete pressed " + getItemId(p), Toast.LENGTH_SHORT).show();
//                    // DO SOMETHING HERE
//                    db.deleteUser((int)(getItemId(p)));
//                    Log.d("Update: ", "Delete button pressed " + getItemId(p));
//                    Toast.makeText(context, "Delete pressed " + getItemId(p), Toast.LENGTH_SHORT).show();
//                    users.remove(p);
//                    notifyItemRemoved(p);
////                    notifyDataSetChanged();
////                    notifyItemRemoved(p);
////                    notifyDataSetChanged();
//
//                });
//
//                AlertDialog dialog = alertDialog.create();
//                dialog.show();
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
        User user = getItem(position);
//        holder.picture.setImageBitmap(user.getPhoto());
        Glide.with(holder.picture.getContext()).load(user.getPhoto()).into(holder.picture);
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
    protected void onBindViewHolder(@NonNull UserListViewHolder holder, int position, @NonNull User model) {

    }

//    @Override
//    public int getItemCount() {
//        return users.size();
//    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

//    public void setUsers(List<User> users) {
//        this.users = users;
//        notifyDataSetChanged();
//    }

    public User getUserAt(int position) {
        return getItem(position);
    }

//    @Override
//    public void onFinishUpdateDialog(int position, User user) {
//        users.set(position, user);
//        notifyItemChanged(position);
//    }

    public interface OnUserListClickListener {
        void onUpdate(int p);
        void onDelete(int p);
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
//            viewItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onUserListClickListener.onUpdate(getLayoutPosition());/////////DIALOG LISTENER????
//                    onUserListClickListener.onDelete(getLayoutPosition());
//                }
//            });
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.btnUpdateUser) {
                onUserListClickListener.onUpdate(this.getLayoutPosition());
            } else if (id == R.id.btnDeleteUser) {
                onUserListClickListener.onDelete(this.getLayoutPosition());
            }
        }
    }
}
