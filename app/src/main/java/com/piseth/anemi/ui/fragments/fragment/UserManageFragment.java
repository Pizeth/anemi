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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.piseth.anemi.R;
import com.piseth.anemi.firebase.viewmodel.FirebaseUserViewModel;
import com.piseth.anemi.retrofit.viewmodel.UserViewModel;
import com.piseth.anemi.room.viewmodel.UserRoomViewModel;
import com.piseth.anemi.ui.fragments.dialog.DialogUpdateUserFragment;
import com.piseth.anemi.utils.adapter.CustomRecyclerUserListAdapter;
import com.piseth.anemi.utils.adapter.FirestoreRecyclerUserListAdapter;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.AnemiUtils;
import com.piseth.anemi.utils.util.WrapContentLinearLayoutManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserManageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserManageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String TAG = UserManageFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private FirebaseFirestore fireDb = FirebaseFirestore.getInstance();
    private CollectionReference userRef = fireDb.collection("Users");
    private FirestoreRecyclerUserListAdapter fireAdapter;
    private CustomRecyclerUserListAdapter adapter;
    private SharedPreferences loggedInUser;
    private List<User> loadData;
    private UserRoomViewModel userRoomViewModel;
    private UserViewModel userViewModel;
    private FirebaseUserViewModel firebaseUserViewModel;

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
//        userRoomViewModel = new ViewModelProvider(getActivity()).get(UserRoomViewModel.class);
//        firebaseUserViewModel = new ViewModelProvider(getActivity()).get(FirebaseUserViewModel.class);
//        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
//                .setQuery(firebaseUserViewModel.getAllUsersQuery(), User.class)
//                .build();
//        fireAdapter = new FirestoreRecyclerUserListAdapter(options);
        adapter = new CustomRecyclerUserListAdapter();

        recyclerView = view.findViewById(R.id.recyclerUserView);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(fireAdapter);
        recyclerView.setAdapter(adapter);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
//        userViewModel.getAllUsers().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
//            @Override
//            public void onChanged(List<User> users) {
//                List<User> allUsers = users;
//                for (User user: allUsers) {
//                    Log.d(TAG, "Username is: " + user.getUsername());
//                }
//                adapter.submitList(users);
//            }
//        });
        liveUserDataListening();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteUserDialog(viewHolder.getAbsoluteAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);


        adapter.setOnUserListClickListener(new CustomRecyclerUserListAdapter.OnUserListClickListener() {
            @Override
            public void onUpdate(int p) {
//                String docId = fireAdapter.getDocumentId(p);
                long id = adapter.getItemId(p);
                Bundle bundle = new Bundle();
                bundle.putBoolean("notAlertDialog", true);
//                bundle.putString("user_id", docId);
                bundle.putLong("user_id", id);
                bundle.putInt("position", p);

                DialogUpdateUserFragment dialogFragment = new DialogUpdateUserFragment(bundle);
                dialogFragment.setArguments(bundle);
                FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                Fragment prev = ((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                dialogFragment.show(ft, "dialog");
//                dialogFragment.setOnUpdateCompletedDialogListener(user -> {
//                    Log.d("Manage User", "Finish update ID" + fireAdapter.getDocumentId(p));
//                    FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();
//                    if(fireUser != null && fireUser.getUid().equals(docId)) {
//                        AnemiUtils.setUserPreference(loggedInUser, user);
//                    }
//                });
                dialogFragment.setOnUpdateCompletedDialogListener(new DialogUpdateUserFragment.OnUpdateCompletedDialogListener() {
                    @Override
                    public void onFinishUpdateDialog(long id, User user) {
                        Log.d("User Avatar Updated", "User avatar is : " + user.getAvatar());
                        userViewModel.updateUser(id, user);
                        liveUserDataListening();
                        adapter.notify();
                        Toast.makeText(getContext(), user.getUsername() + "'s has been successfully updated", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onDelete(int p) {
                deleteUserDialog(p);
            }
        });

//        fireAdapter.setOnUserListClickListener(new FirestoreRecyclerUserListAdapter.OnUserListClickListener() {
//            @Override
//            public void onUpdate(int p) {
//                String docId = fireAdapter.getDocumentId(p);
//                Bundle bundle = new Bundle();
//                bundle.putBoolean("notAlertDialog", true);
//                bundle.putString("user_id", docId);
//                bundle.putInt("position", p);
//
//                DialogUpdateUserFragment dialogFragment = new DialogUpdateUserFragment(bundle);
//                dialogFragment.setArguments(bundle);
//                FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
//                FragmentTransaction ft = fragmentManager.beginTransaction();
//                Fragment prev = ((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag("dialog");
//                if (prev != null) {
//                    ft.remove(prev);
//                }
//                ft.addToBackStack(null);
//                dialogFragment.show(ft, "dialog");
//                dialogFragment.setOnUpdateCompletedDialogListener(user -> {
//                    Log.d("Manage User", "Finish update ID" + fireAdapter.getDocumentId(p));
//                    FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();
//                    if(fireUser != null && fireUser.getUid().equals(docId)) {
//                        AnemiUtils.setUserPreference(loggedInUser, user);
//                    }
//                });
//            }
//            @Override
//            public void onDelete(int p) {
//                deleteUserDialog(p);
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
//        recyclerView.getRecycledViewPool().clear();
//        fireAdapter.startListening();
//        liveUserDataListening();
    }


    @Override
    public void onStop() {
        super.onStop();
//        fireAdapter.stopListening();
    }

    //No longer use, switch to firebase
    public void onUpdate(int p) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("notAlertDialog", true);
        bundle.putLong("user_id", fireAdapter.getItemId(p));
        bundle.putInt("position", p);

        DialogUpdateUserFragment dialogFragment = new DialogUpdateUserFragment(bundle);
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

    public void liveUserDataListening() {
        userViewModel.getAllUsers().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                List<User> allUsers = users;
                for (User user: allUsers) {
                    Log.d(TAG, "Username is: " + user.getUsername());
                }
                adapter.submitList(users);
            }
        });
    }

    public void deleteUserDialog(int p) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Remove User");
        alertDialog.setMessage("Delete this user??");
        alertDialog.setPositiveButton("CANCEL", (dialog, which) -> dialog.cancel());
        alertDialog.setNegativeButton("YES", (dialog, which) -> {
            // Perform delete action using modelView
//            firebaseUserViewModel.deleteUser(fireAdapter.getDocumentId(p));
            userViewModel.deleteUser(adapter.getItemId(p));
//            userRoomViewModel.deleteUser(fireAdapter.getItemId(p));
        });

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }
}