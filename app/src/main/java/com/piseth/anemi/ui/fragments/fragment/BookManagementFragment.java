package com.piseth.anemi.ui.fragments.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.piseth.anemi.R;
import com.piseth.anemi.firebase.viewmodel.FirebaseBookViewModel;
import com.piseth.anemi.ui.fragments.dialog.DialogUpdateBookFragment;
import com.piseth.anemi.utils.adapter.FirestoreRecyclerBookListAdapter;
import com.piseth.anemi.utils.model.Book;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.AnemiUtils;
import com.piseth.anemi.utils.util.WrapContentLinearLayoutManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookManagementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookManagementFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private SharedPreferences loggedInUser;
    private BookDetailFragment bookDetailFragment;
    private FloatingActionButton fabAddBook;
    private FirebaseBookViewModel firebaseBookViewModel;
    private FirestoreRecyclerBookListAdapter fireAdapter;

    public BookManagementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookManagementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookManagementFragment newInstance(String param1, String param2) {
        BookManagementFragment fragment = new BookManagementFragment();
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

        if(getContext() != null) {
            loggedInUser = getContext().getSharedPreferences(AnemiUtils.LOGGED_IN_USER, MODE_PRIVATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseBookViewModel = new ViewModelProvider(getActivity()).get(FirebaseBookViewModel.class);
        FirestoreRecyclerOptions<Book> options = new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(firebaseBookViewModel.getAllBooksQuery(), Book.class)
                .build();
        fireAdapter = new FirestoreRecyclerBookListAdapter(options);
        recyclerView = view.findViewById(R.id.recyclerBookView);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(fireAdapter);

        fabAddBook = view.findViewById(R.id.floating_add_book);
        User user = AnemiUtils.getLoggedInUser(loggedInUser);
        if(user != null) {
            if(user.getRoleId() != AnemiUtils.ROLE_ADMIN) {
                fabAddBook.setVisibility(View.INVISIBLE);
            }
        }
        fabAddBook.setOnClickListener(view1 -> dialogAction(AnemiUtils.NEW_BOOK_ENTRY, AnemiUtils.STARTING_POSITION, AnemiUtils.ACTION_ADD));
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteBookDialog(viewHolder.getAbsoluteAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        fireAdapter.setOnBookListClickListener(new FirestoreRecyclerBookListAdapter.OnBookListClickListener() {
            @Override
            public void onUpdate(int p) {
                dialogAction(fireAdapter.getDocumentId(p), p, AnemiUtils.ACTION_UPDATE);
            }

            @Override
            public void onDelete(int p) {
                deleteBookDialog(p);
            }

            @Override
            public void onView(int p) {
                Bundle book_id = new Bundle();
                book_id.putString("book_id", fireAdapter.getDocumentId(p));
                bookDetailFragment = new BookDetailFragment(book_id);
                getParentFragmentManager().beginTransaction().replace(R.id.container, bookDetailFragment).commit();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView.getRecycledViewPool().clear();
        fireAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        fireAdapter.stopListening();
    }

    public void deleteBookDialog(int p) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Remove Book");
        alertDialog.setMessage("Delete this book??");
        alertDialog.setPositiveButton("CANCEL", (dialog, which) -> dialog.cancel());
        alertDialog.setNegativeButton("YES", (dialog, which) -> {
            // DO SOMETHING HERE
            firebaseBookViewModel.deleteBook(fireAdapter.getDocumentId(p));
            Log.d("BookManagement", "Successfully Delete book" + fireAdapter.getItem(p).getBookName());
            Toast.makeText(getContext(), "Successfully Delete Book name " + fireAdapter.getItem(p).getBookName(), Toast.LENGTH_LONG).show();
        });

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    public void dialogAction(String book_id, int list_position, int action) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("notAlertDialog", true);
        bundle.putString("book_id", book_id);
        bundle.putInt("position", list_position);

        DialogUpdateBookFragment dialogFragment = new DialogUpdateBookFragment(bundle, action);
        dialogFragment.setArguments(bundle);
        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager(); // instantiate your view context
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prev = ((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        dialogFragment.show(ft, "dialog");
    }
}