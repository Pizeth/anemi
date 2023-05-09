package com.piseth.anemi;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements DialogUpdateBookFragment.DialogListener, CustomRecyclerBookListAdapter.OnBookListClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private RecyclerView recyclerView;
    private DatabaseManageHandler db;
    private CustomRecyclerBookListAdapter adapter;
    private List<Book> loadData;
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
        context = getActivity().getApplicationContext();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = new DatabaseManageHandler(getActivity());
        loadData = db.getAllBooks();
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
        recyclerView = view.findViewById(R.id.recyclerBookView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new CustomRecyclerBookListAdapter(getContext(), loadData, this, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onUpdate(int p) {
        Log.d("Update: ", "Update button pressed " + adapter.getItemId(p));
        Toast.makeText(getContext(), "Update pressed " + adapter.getItemId(p), Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putBoolean("notAlertDialog", true);
        bundle.putLong("book_id", adapter.getItemId(p));
        bundle.putInt("position", p);

        DialogUpdateBookFragment dialogFragment = new DialogUpdateBookFragment(bundle, this);
        dialogFragment.setTargetFragment(this, 0);
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

    @Override
    public void onDelete(int p) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Delete Book");
        alertDialog.setMessage("Delete this book?");
        alertDialog.setPositiveButton("CANCEL", (dialog, which) -> dialog.cancel());
        alertDialog.setNegativeButton("YES", (dialog, which) -> {
            Log.d("Update: ", "Delete button pressed " + adapter.getItemId(p));
            Toast.makeText(getContext(), "Delete pressed " + adapter.getItemId(p), Toast.LENGTH_SHORT).show();
            // DO SOMETHING HERE
            db.deleteBook((int)(adapter.getItemId(p)));
            Log.d("Update: ", "Delete button pressed " + adapter.getItemId(p));
            Toast.makeText(getContext(), "Delete pressed " + adapter.getItemId(p), Toast.LENGTH_SHORT).show();
            loadData.remove(p);
            adapter.notifyItemRemoved(p);
        });

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    @Override
    public void onFinishUpdateDialog(int position, Book book) {
        loadData.set(position, book);
        adapter.notifyItemChanged(position);
    }
}