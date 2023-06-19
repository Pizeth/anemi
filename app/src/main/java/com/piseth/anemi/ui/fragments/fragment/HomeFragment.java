package com.piseth.anemi.ui.fragments.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.material.search.SearchBar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private FirebaseBookViewModel firebaseBookViewModel;
    private FirestoreRecyclerHomeViewAdapter fireAdapter;
    private ViewBookContentFragment viewBookContentFragment;
    private SearchView searchView;
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
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation()));

        searchView = view.findViewById(R.id.search_view);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        fireAdapter.setOnBookItemClickListener((view1, p) -> {
            Bundle book_id = new Bundle();
            book_id.putString("book_id", fireAdapter.getDocumentId(p));
            book_id.putString("book_title", fireAdapter.getItem(p).getBookName());
            viewBookContentFragment = new ViewBookContentFragment(book_id);
            getParentFragmentManager().beginTransaction().replace(R.id.container, viewBookContentFragment).commit();
        });
    }

    private void filterList(String newText) {
        if(!newText.isEmpty()) {
            FirestoreRecyclerOptions<Book> options = new FirestoreRecyclerOptions.Builder<Book>()
                    .setQuery(firebaseBookViewModel.getSearchBookQuery(newText), Book.class)
                    .build();
            FirestoreRecyclerHomeViewAdapter filterAdapter = new FirestoreRecyclerHomeViewAdapter(options);
            recyclerView.setAdapter(filterAdapter);
            filterAdapter.startListening();
        } else {
            recyclerView.setAdapter(fireAdapter);
        }
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

    //Not yet Implement
    private void showFilterDialog() {
        android.app.AlertDialog.Builder alerDialog = new android.app.AlertDialog.Builder(getContext());
        alerDialog.setTitle("Search");
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_option, null);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.txt_search);
        ChipGroup chipGroup = view.findViewById(R.id.chipGroup);
    }
}