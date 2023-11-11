package com.piseth.anemi.utils.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.piseth.anemi.utils.model.Author;

import java.util.List;

public class AuthorDropdownListViewAdapter extends RecyclerView.Adapter<AuthorDropdownListViewAdapter.AuthorListViewHolder> {

    private Context context;
    private List<Author> authors;

    public AuthorDropdownListViewAdapter(Context context, List<Author> authors) {
        this.context = context;
        this.authors = authors;
    }
    @NonNull
    @Override
    public AuthorDropdownListViewAdapter.AuthorListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorDropdownListViewAdapter.AuthorListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return authors.size();
    }

    @Override
    public long getItemId(int position) {
        return authors.get(position).getId();
    }

    public class AuthorListViewHolder extends RecyclerView.ViewHolder {
        TextView penName;
        ImageView avatar;

        public AuthorListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
