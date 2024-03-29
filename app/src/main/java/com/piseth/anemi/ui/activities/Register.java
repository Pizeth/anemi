package com.piseth.anemi.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.piseth.anemi.R;
import com.piseth.anemi.firebase.viewmodel.FirebaseUserViewModel;
import com.piseth.anemi.retrofit.viewmodel.UserViewModel;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.AnemiUtils;
import com.piseth.anemi.utils.util.DatabaseManageHandler;
import com.uploadcare.android.library.api.UploadcareClient;
import com.uploadcare.android.library.api.UploadcareFile;
import com.uploadcare.android.library.callbacks.UploadFileCallback;
import com.uploadcare.android.library.exceptions.UploadcareApiException;
import com.uploadcare.android.library.upload.FileUploader;
import com.uploadcare.android.library.upload.Uploader;

public class Register extends AppCompatActivity {

    private DatabaseManageHandler db;
    private SharedPreferences loggedInUser;
//    private Bitmap imageToStore;
    private Uri imageToStore;
    private TextInputLayout txt_username, txt_email, txt_password, txt_re_password, txt_phone;
    private ImageView profileImage;
    private TextView errorLabel;
    private FirebaseUserViewModel firebaseUserViewModel;
    private UserViewModel userViewModel;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txt_username = findViewById(R.id.username);
        txt_email = findViewById(R.id.email);
        txt_password = findViewById(R.id.password);
        txt_re_password = findViewById(R.id.re_password);
        txt_phone = findViewById(R.id.phone);
        profileImage = findViewById(R.id.image_logo);
        errorLabel = findViewById(R.id.lbl_error);
        db = new DatabaseManageHandler(this);
        loggedInUser = getSharedPreferences(AnemiUtils.LOGGED_IN_USER, MODE_PRIVATE);
//        auth = FirebaseAuth.getInstance();
//        firebaseUserViewModel = new ViewModelProvider(this).get(FirebaseUserViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    public void btnAddPhotoOnClickListener(View view) {
        pickImage.launch("image/*");
    }

    public void btnSignUpOnClickListener(View view) {
        String username, email, password, re_password, phone;

        if (txt_username.getEditText() != null && txt_email.getEditText() != null
            && txt_password.getEditText() != null && txt_re_password.getEditText() != null
            && txt_phone.getEditText() != null && profileImage != null) {
            username = txt_username.getEditText().getText().toString().trim();
            email = txt_email.getEditText().getText().toString().trim();
            password = txt_password.getEditText().getText().toString().trim();
            re_password = txt_re_password.getEditText().getText().toString().trim();
            phone = txt_phone.getEditText().getText().toString().trim();
            Log.d("Insert: ", username + " " + password + " " + phone);
//            boolean result = addUser(username, password, re_password, phone, imageToStore);
//            if (result) {
//                Toast.makeText(getApplicationContext(), "Successfully added new User", Toast.LENGTH_SHORT).show();
//                Log.d("Successful: ", "Back to Login Screen");
//                Intent intent = new Intent(Register.this, Login.class);
//                startActivity(intent);
//            }
            createUser(username, email, password, re_password, phone);
        }
    }

    private void createUser(String username, String email, String password, String re_password, String phone) {
        if (!isValidUsername(username) | !isValidPassword(password) |
            !isValidRePassword(re_password, password) | !isValidPhoneNo(phone) |
            !isValidPhoto(imageToStore)) {
            return;
        }
        UploadcareClient uploadCare = new UploadcareClient(AnemiUtils.PUBLIC_KEY, AnemiUtils.PRIVATE_KEY);
        Uploader uploader = new FileUploader(uploadCare, imageToStore, getApplicationContext()).store(true);
        uploader.uploadAsync(new UploadFileCallback() {
            @Override
            public void onProgressUpdate(long l, long l1, double v) {}

            @Override
            public void onFailure(UploadcareApiException e) {
                // Handle errors.
                Log.d("Error", "Upload error " + e.getMessage());
                Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(UploadcareFile file) {
                Log.d("Success", "File OG URL is " + file.getOriginalFileUrl());
                Log.d("Success", "File OG name is " + file.getOriginalFilename());
                String fileName = file.getOriginalFileUrl().toString();
                Log.d("Uploaded", "File cdn location " + fileName);
                String avatarUrl = fileName.replace(file.getOriginalFilename(), username);
                Log.d("Success Rename", "File after rename " + avatarUrl);
                User user = new User(username, email, password, AnemiUtils.ROLE_USER, phone, avatarUrl);
                Log.d("User Updated", user.getUsername() + "'s info updated");
//                listener.onFinishUpdateDialog(id, user);
//                AnemiUtils.setUserPreference(loggedInUser, user);
                userViewModel.registerUser(user, loggedInUser);
                Toast.makeText(Register.this, "Successfully Register new User", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Register.this, Login.class));
            }
        });


        Log.d("Insert: ", username + " " + password + " " + phone);
//        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                User user = new User(username, email, password, AnemiUtils.ROLE_USER, phone);
//                User user = new User();
//                String id = auth.getUid();
//                firebaseUserViewModel.addNewUser(imageToStore, user, id);
//                AnemiUtils.setUserPreference(loggedInUser, user);
//                Toast.makeText(Register.this, "Successfully Register new User", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(Register.this, Login.class));
//            } else {
//                Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public void btnBackToSignInOnClick(View view) {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
    }

    public boolean addUser(String username, String password, String re_password, String phone, Uri imageToStore) {
        boolean checkOperation = false;
//        if(!username.isEmpty() && !password.isEmpty() && !re_password.isEmpty() && !phone.isEmpty() && imageToStore != null) {
        if (!isValidUsername(username) | !isValidPassword(password) | !isValidRePassword(re_password, password) | !isValidPhoneNo(phone) | !isValidPhoto(imageToStore)) {
            return checkOperation;
        }
        if (password.equals(re_password)) {
//            User user = new User(AnemiUtils.DUMMY_ID, username, password, AnemiUtils.ROLE_USER, phone, imageToStore);
//            User user = new User(AnemiUtils.DUMMY_ID, username, password, AnemiUtils.ROLE_USER, phone, "imageToStore");
            User user = new User();
            SharedPreferences.Editor prefsEditor = loggedInUser.edit();
//            byte[] userPhoto = AnemiUtils.getBitmapAsByteArray(user.getPhoto());
            Gson gson = new Gson();
            int user_id = (int) db.addUser(user);
            checkOperation = user_id != -1;
            user.setId(user_id);
            String json = gson.toJson(user);
            prefsEditor.putString(AnemiUtils.LOGGED_IN_USER, json);
//            prefsEditor.putString(AnemiUtils.USER_PHOTO, AnemiUtils.BASE64Encode(userPhoto));
            prefsEditor.apply();
            Log.d("Insert: ", "UserID: " + user.getId() + " Username: " + user.getUsername() + " Password" + user.getPassword());
        }
        return checkOperation;
    }

    ActivityResultLauncher pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(),
        new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null){
                    imageToStore = result;
                    profileImage.setImageURI(result);
                    profileImage.setClipToOutline(true);
                }
            }
        });

    private boolean isValidUsername(String text_username) {
        if (text_username.isEmpty()) {
            txt_username.setError("Username cannot be empty!");
            return false;
        } else if (!text_username.matches(AnemiUtils.NO_WHITE_SPACE)) {
            txt_username.setError("White space is not allow!");
            return false;
        } else {
            txt_username.setError(null);
            txt_username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean isValidPassword(String text_password) {
        if (text_password.isEmpty()) {
            txt_password.setError("Password cannot be empty!");
            return false;
        } else if (!text_password.matches(AnemiUtils.NO_WHITE_SPACE)) {
            txt_password.setError("White space is not allow!");
            return false;
        } else {
            txt_password.setError(null);
            txt_password.setErrorEnabled(false);
            return true;
        }

    }

    private boolean isValidRePassword(String text_re_password, String text_password) {
        if (text_re_password.isEmpty()) {
            txt_re_password.setError("Re-Password cannot be empty!");
            return false;
        } else if (!text_re_password.matches(AnemiUtils.NO_WHITE_SPACE)) {
            txt_re_password.setError("White space is not allow!");
            return false;
        } else if (!text_re_password.equals(text_password)) {
            txt_re_password.setError("Password and Re-Password does not match!");
            return false;
        } else {
            txt_re_password.setError(null);
            txt_re_password.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean isValidPhoneNo(String text_phone) {
        if (text_phone.isEmpty()) {
            txt_phone.setError("Phone cannot be empty");
            return false;
        } else if (!text_phone.matches(AnemiUtils.NO_WHITE_SPACE)) {
            txt_phone.setError("White space is not allow!");
            return false;
        } else {
            txt_phone.setError(null);
            txt_phone.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean isValidPhoto(Uri photo) {
        if (photo == null) {
            errorLabel.setText(R.string.error_photo);
            errorLabel.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorLabel.setText("");
            errorLabel.setVisibility(View.GONE);
            return true;
        }
    }
}