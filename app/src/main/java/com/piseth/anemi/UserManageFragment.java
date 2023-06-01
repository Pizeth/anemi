package com.piseth.anemi;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.piseth.anemi.room.viewmodel.UserRoomViewModel;
import com.piseth.anemi.utils.model.User;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserManageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserManageFragment extends Fragment implements DialogUpdateUserFragment.DialogListener, CustomRecyclerUserListAdapter.OnUserListClickListener {

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

    public static String LOGGED_IN_USER = "logged_user";
    private SharedPreferences loggedInUser;
//    private MaterialToolbar topMenu;
    private List<User> loadData;
    private UserRoomViewModel userRoomViewModel;
//    DialogUserUpdateFragment.DialogListener listener;

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
        if(getContext() != null) {
            loggedInUser = getContext().getSharedPreferences(AnemiUtils.LOGGED_IN_USER, MODE_PRIVATE);
        }
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
        recyclerView = view.findViewById(R.id.recyclerUserView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
//        adapter = new CustomRecyclerUserListAdapter(getContext(), this, this);
        recyclerView.setAdapter(adapter);
        userRoomViewModel = new ViewModelProvider(getActivity()).get(UserRoomViewModel.class);
//        userRoomViewModel.getAllUsersLiveData().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
//            @Override
//            public void onChanged(List<User> users) {
//                adapter.submitList(users);
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
                userRoomViewModel.deleteUser(adapter.getItemId(viewHolder.getAdapterPosition()));
                deleteUserDialog(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
//        adapter.notifyAll();
//        adapter.notifyDataSetChanged();

//        topMenu = view.findViewById(R.id.top_tool_bar);
//        topMenu.setOnMenuItemClickListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.add) {
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
    public void onFinishUpdateDialog(int position, User user) {
        loadData.set(position, user);
        adapter.notifyItemChanged(position);
    }

//    public void setListener(DialogUserUpdateFragment.DialogListener listener) {
//        this.listener = listener;
//    }

    @Override
    public void onUpdate(int p) {
        Log.d("Update: ", "Update button pressed " + adapter.getItemId(p));
        Toast.makeText(getContext(), "Update pressed " + adapter.getItemId(p), Toast.LENGTH_SHORT).show();


        Bundle bundle = new Bundle();
        bundle.putBoolean("notAlertDialog", true);
        bundle.putLong("user_id", adapter.getItemId(p));
        bundle.putInt("position", p);

        DialogUpdateUserFragment dialogFragment = new DialogUpdateUserFragment(bundle, this);
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
        // Implement your functionality for onDelete here
        deleteUserDialog(p);
    }

    public void deleteUserDialog(int p) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Remove User");
        alertDialog.setMessage("Delete this user??");
        alertDialog.setPositiveButton("CANCEL", (dialog, which) -> dialog.cancel());
        alertDialog.setNegativeButton("YES", (dialog, which) -> {
            // DO SOMETHING HERE
            db.deleteUser((int) (adapter.getItemId(p)));
            Log.d("Update: ", "Successfully Delete User" + adapter.getItemId(p));
            Toast.makeText(getContext(), "Successfully Delete User " + adapter.getItemId(p), Toast.LENGTH_SHORT).show();
            loadData.remove(p);
            adapter.notifyItemRemoved(p);
        });

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }
}