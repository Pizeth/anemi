package com.piseth.anemi.ui.fragments.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.piseth.anemi.R;
import com.piseth.anemi.firebase.viewmodel.FirebaseBookViewModel;
import com.piseth.anemi.firebase.viewmodel.FirebaseUserViewModel;
import com.piseth.anemi.utils.adapter.FirestoreRecyclerBookListAdapter;
import com.piseth.anemi.utils.adapter.FirestoreRecyclerUserListAdapter;
import com.piseth.anemi.utils.model.Book;
import com.piseth.anemi.utils.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilterSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterSearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomMenu;
    private FirebaseBookViewModel firebaseBookViewModel;
    private FirestoreRecyclerBookListAdapter fireAdapter;
    public FilterSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterSearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterSearchFragment newInstance(String param1, String param2) {
        FilterSearchFragment fragment = new FilterSearchFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomMenu = view.findViewById(R.id.bottom_search_menu);
        firebaseBookViewModel = new ViewModelProvider(getActivity()).get(FirebaseBookViewModel.class);
        recyclerView = view.findViewById(R.id.filter_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        bottomMenu.inflateMenu(R.menu.filter_menu);
        bottomMenu.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.search) {
                showSearchDialog();
                return true;
            } else if (itemId == R.id.filter) {
                showFilterDialog();
                return true;
            } 
            return false;
        });
    }

    private void showSearchDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
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
            fireAdapter = new FirestoreRecyclerBookListAdapter(options);
        });
        alertDialog.show();
    }

    private void showFilterDialog() {
        AlertDialog.Builder alerDialog = new AlertDialog.Builder(getContext());
        alerDialog.setTitle("Search");
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_option, null);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.txt_search);
        ChipGroup chipGroup = view.findViewById(R.id.chipGroup);
    }

    @Override
    public void onStart() {
        super.onStart();
//        fireAdapter.startListening();
    }
}