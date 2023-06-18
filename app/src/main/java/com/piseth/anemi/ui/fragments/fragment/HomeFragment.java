package com.piseth.anemi.ui.fragments.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.piseth.anemi.R;
import com.piseth.anemi.common.Common;
import com.piseth.anemi.firebase.viewmodel.FirebaseBookViewModel;
import com.piseth.anemi.service.IBookLoadDone;
import com.piseth.anemi.utils.adapter.FirestoreRecyclerBookListAdapter;
import com.piseth.anemi.utils.adapter.FirestoreRecyclerHomeViewAdapter;
import com.piseth.anemi.utils.model.Book;
import com.piseth.anemi.utils.util.AnemiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements IBookLoadDone {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private SharedPreferences loggedInUser;
    private FirebaseBookViewModel firebaseBookViewModel;
    private FirestoreRecyclerHomeViewAdapter fireAdapter;
    private IBookLoadDone listener;
    private CollectionReference bookRef;
    private ViewBookContentFragment viewBookContentFragment;
    private FilterSearchFragment filterSearchFragment;
    private BottomNavigationView bottomMenu;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getActivity().getApplicationContext();
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if(getContext() != null) {
            loggedInUser = getContext().getSharedPreferences(AnemiUtils.LOGGED_IN_USER, MODE_PRIVATE);
        }
        listener = this;
        bookRef = FirebaseFirestore.getInstance().collection("Books");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseBookViewModel = new ViewModelProvider(getActivity()).get(FirebaseBookViewModel.class);
         FirestoreRecyclerOptions<Book> options = new FirestoreRecyclerOptions.Builder<Book>()
                .setQuery(firebaseBookViewModel.getAllBooksQuery(), Book.class)
                .build();
        fireAdapter = new FirestoreRecyclerHomeViewAdapter(options);
        recyclerView = view.findViewById(R.id.recyclerBookView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(fireAdapter);

//        bottomMenu = view.findViewById(R.id.bottom_search_menu);
//        bottomMenu.setOnItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.search) {
//                showSearchDialog();
//                return true;
//            } else if (itemId == R.id.filter) {
//                showFilterDialog();
//                return true;
//            }
//            return false;
//        });

//        buttonFilterSearch = view.findViewById(R.id.btnFilterSearch);
//        buttonFilterSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filterSearchFragment = new FilterSearchFragment();
//                getParentFragmentManager().beginTransaction().replace(R.id.container, filterSearchFragment).commit();
//            }
//        });
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

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation()));
        fireAdapter.setOnBookItemClickListener((view1, p) -> {
            Bundle book_id = new Bundle();
            book_id.putString("book_id", fireAdapter.getDocumentId(p));
            book_id.putString("book_title", fireAdapter.getItem(p).getBookName());
            viewBookContentFragment = new ViewBookContentFragment(book_id);
            getParentFragmentManager().beginTransaction().replace(R.id.container, viewBookContentFragment).commit();
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
//            bookRoomViewModel.deleteUser(fireAdapter.getItemId(p));
            Log.d("BookManagement", "Successfully Delete book" + fireAdapter.getItem(p).getBookName());
            Toast.makeText(getContext(), "Successfully Delete Book name " + fireAdapter.getItem(p).getBookName(), Toast.LENGTH_SHORT).show();
        });

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    @Override
    public void onBookLoadDoneListener(List<Book> books) {
        Common.books = books;
//        recyclerView
    }

    private void showSearchDialog() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getContext());
        alertDialog.setTitle("Search");
        LayoutInflater inflater = this.getLayoutInflater();
        View search_layout = inflater.inflate(R.layout.dialog_search, null);
        TextInputLayout search_box = search_layout.findViewById(R.id.edit_search);
        ChipGroup chipGroup = search_layout.findViewById(R.id.chipGroup);
        alertDialog.setView(search_layout);
        alertDialog.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
        alertDialog.setPositiveButton("SEARCH", (dialog, which) -> {
            FirestoreRecyclerOptions<Book> options = new FirestoreRecyclerOptions.Builder<Book>()
                    .setQuery(firebaseBookViewModel.getSearchBookQuery(search_box.getEditText().getText().toString()), Book.class)
                    .build();
            fireAdapter = new FirestoreRecyclerHomeViewAdapter(options);
        });
        alertDialog.show();
    }

    private void showFilterDialog() {
        android.app.AlertDialog.Builder alerDialog = new android.app.AlertDialog.Builder(getContext());
        alerDialog.setTitle("Search");
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_option, null);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.txt_search);
        ChipGroup chipGroup = view.findViewById(R.id.chipGroup);
    }

    public void loadBooks() {
        bookRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            List<Book> books = new ArrayList<>();
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null) {
                    for(QueryDocumentSnapshot snapshot: value) {
                        Book book = snapshot.toObject(Book.class);
                        books.add(book);
                    }
                }
                listener.onBookLoadDoneListener(books);
            }
        });
    }
}