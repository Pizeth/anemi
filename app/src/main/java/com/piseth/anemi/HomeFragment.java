package com.piseth.anemi;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements DialogUpdateBookFragment.DialogListener, CustomRecyclerBookListAdapter.OnBookListClickListener, NavigationView.OnNavigationItemSelectedListener {

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
    public static String LOGGED_IN_USER = "logged_user";
    private SharedPreferences loggedInUser;
    private MaterialToolbar topMenu;
    private List<Book> loadData;
    private BookDetailFragment bookDetailFragment;
    private FloatingActionButton fabAddBook;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

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
        loggedInUser = getContext().getSharedPreferences(LOGGED_IN_USER, MODE_PRIVATE);
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
        fabAddBook = view.findViewById(R.id.floating_add_book);
        fabAddBook.setOnClickListener(view1 -> dialogAction(AnemiUtils.NEW_ENTRY, AnemiUtils.STARTING_POSITION, AnemiUtils.ACTION_ADD));
        User user = AnemiUtils.getLoggedInUser(loggedInUser);
        if(user != null) {
            if(user.getUserRoleId() != AnemiUtils.ROLE_ADMIN) {
                fabAddBook.setVisibility(View.INVISIBLE);
            }
        }
        //Hook
//        drawerLayout = view.findViewById(R.id.home_drawer_layout);
//        navigationView = view.findViewById(R.id.home_nav_view);
//        topMenu = view.findViewById(R.id.top_tool_bar);
//        //Toolbar
//        ((AppCompatActivity)getActivity()).setSupportActionBar(topMenu);
//        //Navigation Drawer Menu
//        navigationView.bringToFront();
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, topMenu, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//        navigationView.setNavigationItemSelectedListener(this);
//        topMenu.setOnMenuItemClickListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.add) {
////                Bundle bundle = new Bundle();
////                bundle.putBoolean("notAlertDialog", true);
////                bundle.putLong("book_id", AnemiUtils.NEW_ENTRY);
////                bundle.putInt("position", AnemiUtils.NEW_ENTRY);
////
////                DialogUpdateBookFragment dialogFragment = new DialogUpdateBookFragment(bundle, AnemiUtils.ACTION_ADD, this);
////                dialogFragment.setTargetFragment(this, 0);
////                dialogFragment.setArguments(bundle);
////                FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager(); // instantiate your view context
////                FragmentTransaction ft = fragmentManager.beginTransaction();
////                Fragment prev = ((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag("dialog");
////                if (prev != null) {
////                    ft.remove(prev);
////                }
////                ft.addToBackStack(null);
////                dialogFragment.show(ft, "dialog");
//                dialogAction(AnemiUtils.NEW_ENTRY, AnemiUtils.STARTING_POSITION, AnemiUtils.ACTION_ADD);
//            } else if (itemId == R.id.logout) {
//                SharedPreferences.Editor prefsEditor = loggedInUser.edit();
//                prefsEditor.remove(LOGGED_IN_USER);
//                prefsEditor.apply();
//                Intent intent = new Intent(getActivity(), login.class);
//                startActivity(intent);
//                return true;
//            }
//            return false;
//        });
    }

    @Override
    public void onUpdate(int p) {
//        Bundle bundle = new Bundle();
//        bundle.putBoolean("notAlertDialog", true);
//        bundle.putLong("book_id", adapter.getItemId(p));
//        bundle.putInt("position", p);
//
//        DialogUpdateBookFragment dialogFragment = new DialogUpdateBookFragment(bundle, AnemiUtils.ACTION_UPDATE, this);
//        dialogFragment.setTargetFragment(this, 0);
//        dialogFragment.setArguments(bundle);
//        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager(); // instantiate your view context
//        FragmentTransaction ft = fragmentManager.beginTransaction();
//        Fragment prev = ((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag("dialog");
//        if (prev != null) {
//            ft.remove(prev);
//        }
//        ft.addToBackStack(null);
//        dialogFragment.show(ft, "dialog");
        dialogAction(adapter.getItemId(p), p, AnemiUtils.ACTION_UPDATE);
    }

    @Override
    public void onDelete(int p) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Delete Book");
        alertDialog.setMessage("Delete this book?");
        alertDialog.setPositiveButton("CANCEL", (dialog, which) -> dialog.cancel());
        alertDialog.setNegativeButton("YES", (dialog, which) -> {
            // DO SOMETHING HERE
            db.deleteBook((int)(adapter.getItemId(p)));
            Log.d("success", "Successfully delete book");
            Toast.makeText(getContext(), "Successfully delete book", Toast.LENGTH_SHORT).show();
            loadData.remove(p);
            adapter.notifyItemRemoved(p);
        });

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    @Override
    public void onView(int p) {
        Bundle book_id = new Bundle();
        book_id.putInt("book_id", (int) adapter.getItemId(p));
        bookDetailFragment = new BookDetailFragment(book_id);
        getParentFragmentManager().beginTransaction().replace(R.id.container, bookDetailFragment).commit();
    }

    @Override
    public void onFinishUpdateDialog(int position, Book book) {
        Log.d("success!", "Total Available Book:"  + loadData.size());
        if(loadData.size() > 0) {
            if(position != -1) {
                loadData.set(position, book);
                adapter.notifyItemChanged(position);
                Log.d("success!", "Adding new book"  + book.getBookName());
            } else {
                Log.d("success!", "Adding new book"  + book.getBookName());
                reloadData(book);
            }
        } else {
            Log.d("success!", "Adding new book"  + book.getBookName() + " empty list");
            reloadData(book);
        }
    }

    public void dialogAction(long book_id, int list_position, int action) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("notAlertDialog", true);
        bundle.putLong("book_id", book_id);
        bundle.putInt("position", list_position);

        DialogUpdateBookFragment dialogFragment = new DialogUpdateBookFragment(bundle, action, this);
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

    public void reloadData(Book book) {
        loadData.add(book);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        drawerLayout.closeDrawer(GravityCompat.START);
        item.setChecked(true);
        drawerLayout.close();
        return true;
    }
}