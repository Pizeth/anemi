package com.piseth.anemi;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
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

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.piseth.anemi.utils.model.User;

import java.net.URI;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment implements DialogUpdateUserFragment.DialogListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static String LOGGED_IN_USER = "logged_user";
    public static String USER_PHOTO = "user_photo";
    private SharedPreferences loggedInUser;
    private MaterialToolbar topMenu;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseManageHandler db;
    private User user;
    private Bitmap imageToStore;
    private TextView txt_username, txt_role, txt_phone;
    private ImageView profileImage;
    private FloatingActionButton fabUpdateUser;
    private String selectedImagePath;
    private URI imagePath;

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
        db = new DatabaseManageHandler(getActivity());
        loggedInUser = getActivity().getSharedPreferences(LOGGED_IN_USER, MODE_PRIVATE);
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
        txt_role = view.findViewById(R.id.role);
        txt_phone = view.findViewById(R.id.phone);
        profileImage = view.findViewById(R.id.image_logo);
        fabUpdateUser = view.findViewById(R.id.floating_edit_user);
//        topMenu = view.findViewById(R.id.top_tool_bar);
        if (loggedInUser.contains(LOGGED_IN_USER) && loggedInUser.contains(USER_PHOTO)) {
            Gson gson = new Gson();
            String json = loggedInUser.getString(LOGGED_IN_USER, "");
            user = gson.fromJson(json, User.class);
            byte[] photo = AnemiUtils.BASE64Decode(loggedInUser.getString(USER_PHOTO, ""));
            imageToStore = AnemiUtils.getBitmapFromBytesArray(photo);
            user.setPhoto(imageToStore);
            reloadInfo(user);
        }
        fabUpdateUser.setOnClickListener(view1 -> {
            Log.d("Update: ", "Update button pressed");
            int user_id = user.getId();
            Bundle bundle = new Bundle();
            bundle.putBoolean("notAlertDialog", true);
            bundle.putLong("user_id", user_id);
            bundle.putInt("position", user_id);

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
        });

//        topMenu.setOnMenuItemClickListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.edit) {
//                Log.d("Update: ", "Update button pressed");
//                Toast.makeText(getContext(), "Update pressed", Toast.LENGTH_SHORT).show();
//
//                int user_id = user.getId();
//                Bundle bundle = new Bundle();
//                bundle.putBoolean("notAlertDialog", true);
//                bundle.putLong("user_id", user_id);
//                bundle.putInt("position", user_id);
//
//                DialogUserUpdateFragment dialogFragment = new DialogUserUpdateFragment(bundle, this);
//                dialogFragment.setTargetFragment(this, 0);
//                dialogFragment.setArguments(bundle);
//                FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager(); // instantiate your view context
//                FragmentTransaction ft = fragmentManager.beginTransaction();
//                Fragment prev = ((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag("dialog");
//                if (prev != null) {
//                    ft.remove(prev);
//                }
//                ft.addToBackStack(null);
//
//                dialogFragment.show(ft, "dialog");
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

//    @Override
//    public void onClick(View view) {
//        int id = view.getId();
//        if (id == R.id.buttonAddPhoto) {
//            chooseImage();
//            return;
//        } else if (id == R.id.btnSave) {
//            String username, password, re_password, phone;
//
//            username = (txt_username.getEditText() != null) ? txt_username.getEditText().getText().toString().trim() : "";
//            password = (txt_password.getEditText() != null) ? txt_password.getEditText().getText().toString().trim() : "";
//            re_password = (txt_re_password.getEditText() != null) ? txt_re_password.getEditText().getText().toString().trim() : "";
//            phone = (txt_phone.getEditText() != null) ? txt_phone.getEditText().getText().toString().trim() : "";
//            boolean result = updateUser(username, password, re_password, phone, imageToStore);
//            if (result) {
//                Toast.makeText(getContext(), "Successfully update user", Toast.LENGTH_SHORT).show();
//                Log.d("Successful: ", "Back to manage user Screen");
//                        Intent intent = new Intent(Register.this, login.class);
//                        startActivity(intent);
//            }
//        }
//    }

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

    @Override
    public void onFinishUpdateDialog(int position, User user) {
        //TO DO after update user profile
        SharedPreferences.Editor prefsEditor = loggedInUser.edit();
        byte[] userPhoto = AnemiUtils.getBitmapAsByteArray(user.getPhoto());
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString(LOGGED_IN_USER, json);
        prefsEditor.putString(USER_PHOTO, AnemiUtils.BASE64Encode(userPhoto));
        prefsEditor.apply();
        reloadInfo(user);
    }

    public void reloadInfo(User user) {
        if(user != null) {
            txt_username.setText(user.getUsername());
            txt_role.setText(db.getRole(user.getUserRoleId()));
            txt_phone.setText(user.getPhone());
            profileImage.setImageBitmap(user.getPhoto());
            profileImage.setCropToPadding(true);
            profileImage.setClipToOutline(true);
            Log.d("USERNAME: ", user.getUsername() + " 's data acquired'");
        }
    }
}