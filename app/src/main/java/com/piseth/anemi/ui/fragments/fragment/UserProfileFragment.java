package com.piseth.anemi.ui.fragments.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.piseth.anemi.R;
import com.piseth.anemi.ui.fragments.dialog.DialogUpdateUserFragment;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.AnemiUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static String LOGGED_IN_USER = "logged_user";
    private SharedPreferences loggedInUser;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
//    private DatabaseManageHandler db;
//    private Bitmap imageToStore;
    private TextView txt_username, txt_email, txt_role, txt_phone;
    private ImageView profileImage;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
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
//        db = new DatabaseManageHandler(getActivity());
        if(getActivity() != null) {
            loggedInUser = getActivity().getSharedPreferences(AnemiUtils.LOGGED_IN_USER, MODE_PRIVATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txt_username = view.findViewById(R.id.username);
        txt_email = view.findViewById(R.id.email);
        txt_role = view.findViewById(R.id.role);
        txt_phone = view.findViewById(R.id.phone);
        profileImage = view.findViewById(R.id.image_logo);
        FloatingActionButton fabUpdateUser = view.findViewById(R.id.floating_edit_user);
//        reloadInfo(AnemiUtils.getLoggedInUser(loggedInUser));

        fabUpdateUser.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("notAlertDialog", true);
            bundle.putString("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());

            DialogUpdateUserFragment dialogFragment = new DialogUpdateUserFragment(bundle);
            dialogFragment.setArguments(bundle);
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager(); // instantiate your view context
            FragmentTransaction ft = fragmentManager.beginTransaction();
            Fragment prev = ((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            dialogFragment.show(ft, "dialog");
            dialogFragment.setOnUpdateCompletedDialogListener(this::reloadInfo);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        reloadInfo(AnemiUtils.getLoggedInUser(loggedInUser));
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }

    public void reloadInfo(User user) {
        if(user != null) {
            txt_username.setText(user.getUsername());
            txt_email.setText(user.getEmail());
            txt_role.setText(user.getUserRoleId() == AnemiUtils.ROLE_ADMIN ? AnemiUtils.ADMIN : AnemiUtils.USER);
            txt_phone.setText(user.getPhone());
            Glide.with(profileImage.getContext()).load(user.getPhoto()).into(profileImage);
            profileImage.setCropToPadding(true);
            profileImage.setClipToOutline(true);
            Log.d("USERNAME: ", user.getUsername() + " 's data acquired'");
        }
    }
}