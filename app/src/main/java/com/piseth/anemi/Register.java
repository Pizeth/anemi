package com.piseth.anemi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

public class Register extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    public static final int ROLE_USER = 1;
    public static final int DUMMY_ID = 99;
    public static String LOGGED_IN_USER = "logged_user";
    public static String USER_PHOTO = "user_photo";
    private DatabaseManageHandler db;
    private SharedPreferences loggedInUser;
    private Bitmap imageToStore;
    private TextInputLayout txt_username, txt_password, txt_re_password, txt_phone;
    private ImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txt_username = findViewById(R.id.username);
        txt_password = findViewById(R.id.password);
        txt_re_password = findViewById(R.id.re_password);
        txt_phone = findViewById(R.id.phone);
        profileImage = findViewById(R.id.image_logo);
        db = new DatabaseManageHandler(this);
        loggedInUser = getSharedPreferences(LOGGED_IN_USER, MODE_PRIVATE);
    }

    public void btnAddPhotoOnClickListener(View view) {
        chooseImage();
    }
    public void btnSignUpOnClickListener(View view) {
        String username, password, re_password, phone;

        if(txt_username.getEditText() != null && txt_password.getEditText() != null && txt_re_password.getEditText() != null && txt_phone.getEditText() != null && profileImage != null) {
            username = txt_username.getEditText().getText().toString().trim();
            password = txt_password.getEditText().getText().toString().trim();
            re_password = txt_re_password.getEditText().getText().toString().trim();
            phone = txt_phone.getEditText().getText().toString().trim();
            Log.d("Insert: ", username + " " + password + " " + phone);
            boolean result = addUser(username, password, re_password, phone, imageToStore);
            if (result) {
                Toast.makeText(getApplicationContext(), "Successfully added new User", Toast.LENGTH_SHORT).show();
                Log.d("Successful: ", "Back to Login Screen");
                Intent intent = new Intent(Register.this, login.class);
                startActivity(intent);
            }
        }
//        if(checkQueryRun != -1) {
//            Toast.makeText(getApplicationContext(), "Successfully added new User", Toast.LENGTH_SHORT).show();
//            db.close();
//        } else {
//            Toast.makeText(getApplicationContext(), "Failed to add new User", Toast.LENGTH_SHORT).show();
//        }
    }
    public void btnBackToSignInOnClick(View view) {
        Intent intent = new Intent(Register.this, login.class);
        startActivity(intent);
    }

    public boolean addUser(String username, String password, String re_password, String phone, Bitmap imageToStore) {
        boolean checkOperation = false;
        if(!username.isEmpty() && !password.isEmpty() && !re_password.isEmpty() && !phone.isEmpty() && imageToStore != null) {
            if(password.equals(re_password)) {
                User user = new User(DUMMY_ID, username, password, ROLE_USER, phone, imageToStore);
                SharedPreferences.Editor prefsEditor = loggedInUser.edit();
                byte[] userPhoto = AnemiUtils.getBitmapAsByteArray(user.getPhoto());
                Gson gson = new Gson();
                int user_id = (int) db.addUser(user);
                checkOperation = (user_id == -1) ? false : true;
                user.setId(user_id);
                String json = gson.toJson(user);
                prefsEditor.putString(LOGGED_IN_USER, json);
                prefsEditor.putString(USER_PHOTO, AnemiUtils.BASE64Encode(userPhoto));
                prefsEditor.apply();
                Log.d("Insert: ", "UserID: " + user.getId() + " Username: " + user.getUsername() + " Password" + user.getPassword());
                Toast.makeText(getApplicationContext(), user.getId() + " " + user.getUsername() + " " + user.getPassword(), Toast.LENGTH_SHORT).show();
            }
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
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, requestCode, data);
            if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                profileImage.setImageBitmap(imageToStore);
            }
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}