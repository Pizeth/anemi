package com.piseth.anemi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomBookListAdapter extends BaseAdapter {
    private List<Book> books;
    private LayoutInflater inflater;

    public CustomBookListAdapter(Context context, List<Book> books)  {
        this.books = books;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int i) {
        return books.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null) {
            view = inflater.inflate(R.layout.book_list, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.title = view.findViewById(R.id.txtUsername);
            viewHolder.author = view.findViewById((R.id.txtPhone));
            viewHolder.cover = view.findViewById(R.id.profilePicture);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Book book = books.get(i);
        viewHolder.title.setText(book.getBookName());
        viewHolder.author.setText(book.getAuthor());
//        int iconId = context.getResources().getIdentifier(location.getName(), "mipmap", context.getPackageName());
//        viewHolder.icon.setImageResource(iconId);
        viewHolder.cover.setImageResource(book.getPage());
        return view;
    }

    static class ViewHolder {
        ImageView cover;
        TextView title;
        TextView author;
    }
}
