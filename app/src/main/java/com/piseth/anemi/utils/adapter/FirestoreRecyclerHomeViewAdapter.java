package com.piseth.anemi.utils.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.piseth.anemi.R;
import com.piseth.anemi.utils.model.Book;
import com.squareup.picasso.Picasso;

public class FirestoreRecyclerHomeViewAdapter extends FirestoreRecyclerAdapter<Book, FirestoreRecyclerHomeViewAdapter.HomeViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private OnBookItemClickListener listener;

    public FirestoreRecyclerHomeViewAdapter(@NonNull FirestoreRecyclerOptions<Book> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HomeViewHolder holder, int position, @NonNull Book model) {
        Picasso.get().load(model.getCover()).into(holder.cover);
        holder.title.setText(model.getBookName());
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getBookId();
    }

    public String getDocumentId(int position) {
        return getSnapshots().getSnapshot(position).getId();
    }

//    @Override
//    public Filter getFilter() {
//        return bookFilter;
//    }
//
//    private Filter bookFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//            List<Book> filterList = new ArrayList<>();
//            if (charSequence == null || charSequence.length() == 0) {
//                filterList.addAll(filterBook);
//            } else {
//                String filterPattern = charSequence.toString().toLowerCase().trim();
//                for (Book book : filterBook) {
//                    if (book.getBookName().toLowerCase().contains(filterPattern)) {
//                        filterList.add(book);
//                    }
//                }
//            }
//            FilterResults result = new FilterResults();
//            result.values = filterList;
//            return  result;
//        }
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//
//        }
//    };

    public interface OnBookItemClickListener {
        void onClickView(View view, int p);
    }

    public void setOnBookItemClickListener(FirestoreRecyclerHomeViewAdapter.OnBookItemClickListener listener) {
        this.listener = listener;
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title;


        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.book_item);
            title = itemView.findViewById(R.id.book_item_title);
            itemView.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if(position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onClickView(view, getAbsoluteAdapterPosition());
                }
            });
        }
    }
}
