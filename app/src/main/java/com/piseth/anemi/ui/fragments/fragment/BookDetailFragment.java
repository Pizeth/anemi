package com.piseth.anemi.ui.fragments.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.piseth.anemi.utils.util.DatabaseManageHandler;
import com.piseth.anemi.R;
import com.piseth.anemi.utils.model.Book;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseManageHandler db;
    public static String LOGGED_IN_USER = "logged_user";
    private SharedPreferences loggedInUser;
    private MaterialToolbar topMenu;
    private TextView txt_title, txt_author, txt_description;
    private ImageView bookCover;
    private Book book;


    public BookDetailFragment() {
        // Required empty public constructor
    }

    public BookDetailFragment(Bundle savedInstanceState) {
        // Required empty public constructor
        super.setArguments(savedInstanceState);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmentt_book_detail.
     */
    // TODO: Rename and change types and number of parameters
    public static BookDetailFragment newInstance(String param1, String param2) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        loggedInUser = getActivity().getSharedPreferences(LOGGED_IN_USER, MODE_PRIVATE);
        db = new DatabaseManageHandler(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txt_title = view.findViewById(R.id.book_title);
        txt_author = view.findViewById(R.id.author);
        txt_description = view.findViewById(R.id.description);
        bookCover = view.findViewById(R.id.book_cover);
        if(getArguments() != null) {
            book = db.getBook(getArguments().getInt("book_id"));
            if(book != null) {
                txt_title.setText(book.getBookName());
                txt_author.setText(book.getAuthor());
                txt_description.setText(book.getDescription());
                bookCover.setImageBitmap(book.getCover());
                Log.d("success!", book.getBookName() + " 's data acquired'");
            }
        }
    }
}