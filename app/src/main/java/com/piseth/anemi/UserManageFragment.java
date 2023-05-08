package com.piseth.anemi;

import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserManageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserManageFragment extends Fragment implements DialogUserUpdateFragment.DialogListener, CustomRecyclerUserListAdapter.OnUserListClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private DatabaseManageHandler db;
    private CustomRecyclerUserListAdapter adapter;

//    private List<User> loadData() {
//        List<User> users = db.getAllUsers();
////        users.add(new User(1, "Razeth", "1234567", 1, "0123456789", R.mipmap.cover));
//        return users;
//    }

    private List<User> loadData;
    DialogUserUpdateFragment.DialogListener listener;

    public UserManageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserManageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserManageFragment newInstance(String param1, String param2) {
        UserManageFragment fragment = new UserManageFragment();
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
        db = new DatabaseManageHandler(getActivity());
        loadData = db.getAllUsers();
//        adapter = new CustomRecyclerUserListAdapter(getContext(), loadData, this, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_manage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        dataInitialize();
        recyclerView = view.findViewById(R.id.recyclerUserView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new CustomRecyclerUserListAdapter(getContext(), loadData, this, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFinishUpdateDialog(int position, User user) {
        loadData.set(position, user);
        adapter.notifyItemChanged(position);
    }

    public void setListener(DialogUserUpdateFragment.DialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onUpdate(int p) {
        Log.d("Update: ", "Update button pressed " + adapter.getItemId(p));
        Toast.makeText(getContext(), "Update pressed " + adapter.getItemId(p), Toast.LENGTH_SHORT).show();


        Bundle bundle = new Bundle();
        bundle.putBoolean("notAlertDialog", true);
        bundle.putLong("user_id", adapter.getItemId(p));
        bundle.putInt("position", p);

        DialogUserUpdateFragment dialogFragment = new DialogUserUpdateFragment(bundle, this);
        dialogFragment.setTargetFragment(this, 0);
//                Log.d("Current target: " ,dialogFragment.getTargetFragment().toString());
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
// Implement your functionality for onDelete here

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Remove User");
        alertDialog.setMessage("Delete this user??");
        alertDialog.setPositiveButton("CANCEL", (dialog, which) -> dialog.cancel());
        alertDialog.setNegativeButton("YES", (dialog, which) -> {
            Log.d("Update: ", "Delete button pressed " + adapter.getItemId(p));
            Toast.makeText(getContext(), "Delete pressed " + adapter.getItemId(p), Toast.LENGTH_SHORT).show();
            // DO SOMETHING HERE
            db.deleteUser((int)(adapter.getItemId(p)));
            Log.d("Update: ", "Delete button pressed " + adapter.getItemId(p));
            Toast.makeText(getContext(), "Delete pressed " + adapter.getItemId(p), Toast.LENGTH_SHORT).show();
            loadData.remove(p);
            adapter.notifyItemRemoved(p);
//                    notifyDataSetChanged();
//                    notifyItemRemoved(p);
//                    notifyDataSetChanged();

        });

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }
}