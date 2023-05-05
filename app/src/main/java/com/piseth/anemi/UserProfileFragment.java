package com.piseth.anemi;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.net.URI;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static String LOGGED_IN_USER = "logged_user";
    public static String USER_PHOTO = "user_photo";
    private SharedPreferences loggedInUser;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final int PICK_IMAGE = 1;
    public static final int ROLE_USER = 1;
    public static final int DUMMY_ID = 99;
    private DatabaseManageHandler db;
    private User user;
    private Bitmap imageToStore;
    private TextInputLayout txt_username, txt_password, txt_re_password, txt_phone;
    private ImageView profileImage;
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
        db = new DatabaseManageHandler(getContext());
        loggedInUser = getContext().getSharedPreferences(LOGGED_IN_USER, MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        loggedInUser = getContext().getSharedPreferences(LOGGED_IN_USER, MODE_PRIVATE);
        txt_username = view.findViewById(R.id.username);
        txt_password = view.findViewById(R.id.password);
        txt_re_password = view.findViewById(R.id.re_password);
        txt_phone = view.findViewById(R.id.phone);
        profileImage = view.findViewById(R.id.image_logo);
        if (loggedInUser.contains(LOGGED_IN_USER) && loggedInUser.contains(USER_PHOTO)) {
            Gson gson = new Gson();
            String json = loggedInUser.getString(LOGGED_IN_USER, "");
            user = gson.fromJson(json, User.class);
            byte[] photo = AnemiUtils.BASE64Decode(loggedInUser.getString(USER_PHOTO, ""));
            imageToStore = AnemiUtils.getBitmapFromBytesArray(photo);
            user.setPhoto(imageToStore);
            if(user != null) {
                txt_username.getEditText().setText(user.getUsername());
//                txt_password.getEditText().setText(user.getPassword());
//                txt_re_password.getEditText().setText(user.getPassword());
                txt_phone.getEditText().setText(user.getPhone());
                profileImage.setImageBitmap(user.getPhoto());
//                profileImage.setImageDrawable(new BitmapDrawable(getResources(), AnemiUtils.getBitmapFromBytesArray(photo)));
                profileImage.setCropToPadding(true);
                profileImage.setClipToOutline(true);

                Log.d("USERNAME: ", user.getUsername() + " 's data acquired'");
            }
        }
        Button addPhoto = view.findViewById(R.id.buttonAddPhoto);
        Button saveButton = view.findViewById(R.id.btnSave);
        addPhoto.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddPhoto:
                chooseImage();
                return;
            case R.id.btnSave:
                String username, password, re_password, phone;

                username = (txt_username.getEditText() != null) ? txt_username.getEditText().getText().toString().trim() : "";
                password = (txt_password.getEditText() != null) ? txt_password.getEditText().getText().toString().trim() : "";
                re_password = (txt_re_password.getEditText() != null) ? txt_re_password.getEditText().getText().toString().trim() : "";
                phone = (txt_phone.getEditText() != null) ? txt_phone.getEditText().getText().toString().trim() : "";
                boolean result = updateUser(username, password, re_password, phone, imageToStore);
                if (result) {
                    Toast.makeText(getContext(), "Successfully update user", Toast.LENGTH_SHORT).show();
                    Log.d("Successful: ", "Back to manage user Screen");
//                        Intent intent = new Intent(Register.this, login.class);
//                        startActivity(intent);
                }
                return;
        }
    }

    public boolean updateUser(String username, String password, String re_password, String phone, Bitmap imageToStore) {
        boolean checkOperation = false;
        if(!username.isEmpty() && !username.equals(user.getUsername())) user.setUsername(username);
        if(!password.isEmpty() && !re_password.isEmpty() && password.equals(re_password)) user.setPassword(password);
        if(!phone.isEmpty() && !phone.equals(user.getPhone())) user.setPhone(phone);
        if(imageToStore != null && !imageToStore.sameAs(user.getPhoto())) user.setPhoto(imageToStore);
        if(db.updateUser(user) > 0) {
            SharedPreferences.Editor prefsEditor = loggedInUser.edit();
            byte[] userPhoto = AnemiUtils.getBitmapAsByteArray(user.getPhoto());
            Gson gson = new Gson();
            String json = gson.toJson(user);
            prefsEditor.putString(LOGGED_IN_USER, json);
            prefsEditor.putString(USER_PHOTO, AnemiUtils.BASE64Encode(userPhoto));
            prefsEditor.apply();
            Log.d("Update: ", user.getUsername() + " " + user.getPassword());
            Toast.makeText(getContext(), user.getUsername() + " " + user.getPassword(), Toast.LENGTH_SHORT).show();
            checkOperation = true;
        }
        return checkOperation;
    }

    private void chooseImage() {
        try {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");
//            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            Intent pickIntent = new Intent(Intent.ACTION_PICK);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

            startActivityForResult(chooserIntent, PICK_IMAGE);
        } catch(Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, requestCode, data);
            if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);
                profileImage.setImageBitmap(imageToStore);
            }
        } catch(Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
}