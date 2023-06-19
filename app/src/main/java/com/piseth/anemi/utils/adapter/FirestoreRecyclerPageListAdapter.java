package com.piseth.anemi.utils.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.piseth.anemi.R;
import com.piseth.anemi.utils.model.Page;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.AnemiUtils;

import org.jetbrains.annotations.NotNull;

public class FirestoreRecyclerPageListAdapter extends FirestoreRecyclerAdapter<Page, FirestoreRecyclerPageListAdapter.PageListViewHolder>  {
    private OnPageListClickListener listener;

    public FirestoreRecyclerPageListAdapter(@NonNull FirestoreRecyclerOptions<Page> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FirestoreRecyclerPageListAdapter.PageListViewHolder holder, int position, @NonNull Page model) {
        Glide.with(holder.page.getContext()).load(model.getImageURL()).into(holder.page);
        holder.pageNumber.setText(String.format(Integer.toString(model.getPageNumber())));
    }

    @NonNull
    @Override
    public FirestoreRecyclerPageListAdapter.PageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_list, parent, false);
        SharedPreferences loggedInUser = parent.getContext().getSharedPreferences(AnemiUtils.LOGGED_IN_USER, MODE_PRIVATE);
        User user = AnemiUtils.getLoggedInUser(loggedInUser);
        if(user != null) {
            if(user.getUserRoleId() != AnemiUtils.ROLE_ADMIN) {
                view.findViewById(R.id.btnUpdatePage).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.btnDeletePage).setVisibility(View.INVISIBLE);
            }
        }
        return new PageListViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    public String getDocumentId(int position) {
        return getSnapshots().getSnapshot(position).getId();
    }
    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public interface OnPageListClickListener {
        void onUpdate(int p);
        void onDelete(int p);
//        void onView(int p);
    }

    public void setOnPageListClickListener(FirestoreRecyclerPageListAdapter.OnPageListClickListener listener) {
        this.listener = listener;
    }

    public class PageListViewHolder extends RecyclerView.ViewHolder {
        ImageView page;
        TextView pageNumber;
        MaterialButton btnUpdate, btnDelete, btnView;
        public PageListViewHolder(@NotNull View viewItem) {
            super(viewItem);
            page = viewItem.findViewById(R.id.page);
            pageNumber = viewItem.findViewById(R.id.pageNumber);
            btnUpdate = viewItem.findViewById(R.id.btnUpdatePage);
            btnDelete = viewItem.findViewById(R.id.btnDeletePage);
            btnView = viewItem.findViewById(R.id.btnViewPage);

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
//            btnView.setOnClickListener(view -> {
//                int position = getAbsoluteAdapterPosition();
//                if(position != RecyclerView.NO_POSITION && listener != null) {
//                    listener.onView(position);
//                }
//            });
        }
    }
}
