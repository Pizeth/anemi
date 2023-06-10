package com.piseth.anemi.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.piseth.anemi.R;
import com.piseth.anemi.firebase.viewmodel.FirebaseUserViewModel;
import com.piseth.anemi.room.viewmodel.UserRoomViewModel;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.AnemiUtils;
import com.piseth.anemi.utils.util.DatabaseManageHandler;

import java.net.URI;

public class Register extends AppCompatActivity {

    private DatabaseManageHandler db;
    private SharedPreferences loggedInUser;
//    private Bitmap imageToStore;
    private Uri imageToStore;
    private TextInputLayout txt_username, txt_password, txt_re_password, txt_phone;
    private ImageView profileImage;
    private TextView errorLabel;
    private UserRoomViewModel userRoomViewModel;
    private FirebaseUserViewModel firebaseUserViewModel;
    private FirebaseFirestore firestoreDb;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txt_username = findViewById(R.id.username);
        txt_password = findViewById(R.id.password);
        txt_re_password = findViewById(R.id.re_password);
        txt_phone = findViewById(R.id.phone);
        profileImage = findViewById(R.id.image_logo);
        errorLabel = findViewById(R.id.lbl_error);
        db = new DatabaseManageHandler(this);
        firestoreDb = FirebaseFirestore.getInstance();
        loggedInUser = getSharedPreferences(AnemiUtils.LOGGED_IN_USER, MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();
        userRoomViewModel = new ViewModelProvider(this).get(UserRoomViewModel.class);
    }

    public void btnAddPhotoOnClickListener(View view) {
//        chooseImage();
        pickImage.launch("image/*");
    }

    public void btnSignUpOnClickListener(View view) {
        String username, password, re_password, phone;

        if (txt_username.getEditText() != null && txt_password.getEditText() != null && txt_re_password.getEditText() != null && txt_phone.getEditText() != null && profileImage != null) {
            username = txt_username.getEditText().getText().toString().trim();
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
//            createUser(username, password, re_password, phone, );
        }
    }

//    uploadImageBtn.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            progressBar.setVisibility(View.VISIBLE);
//            firebaseViewModel.uploadImagesToFirebase(mImageURI , photoRoomViewModel);
//            firebaseViewModel.getTaskMutableLiveData().observe(MainActivity.this, new Observer<Task<DocumentReference>>() {
//                @Override
//                public void onChanged(Task<DocumentReference> documentReferenceTask) {
//                    if (documentReferenceTask.isSuccessful()){
//                        mImageView.setImageResource(R.drawable.upload_icon);
//                        Toast.makeText(MainActivity.this, "Image Uploaded Successfully !!", Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(MainActivity.this, documentReferenceTask.getException().toString() , Toast.LENGTH_SHORT).show();
//                    }
//                    progressBar.setVisibility(View.INVISIBLE);
//                }
//            });
//        }
//    });

    private void createUser(String username, String email, String password, String re_password, String phone) {
        if (!isValidUsername(username) | !isValidPassword(password) |
            !isValidRePassword(re_password, password) | !isValidPhoneNo(phone) |
            !isValidPhoto(imageToStore)) {
            return;
        }
        Log.d("Insert: ", username + " " + password + " " + phone);
        auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(username, email, password, AnemiUtils.ROLE_USER, phone);
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Toast.makeText(Register.this, "Successfully Register new User", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register.this, Login.class));
                } else {
                    Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            User user = new User(AnemiUtils.DUMMY_ID, username, password, AnemiUtils.ROLE_USER, phone, "imageToStore");
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

    private void chooseImage() {
        try {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

//            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            Intent pickIntent = new Intent(Intent.ACTION_PICK);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
            startActivityForResult(chooserIntent, AnemiUtils.PICK_IMAGE);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    ActivityResultLauncher pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(),
        new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null){
                    imageToStore = result;
                    profileImage.setImageURI(result);
                }
            }
        });

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        try {
//            super.onActivityResult(requestCode, requestCode, data);
//            if (resultCode == RESULT_OK && requestCode == AnemiUtils.PICK_IMAGE && data != null && data.getData() != null) {
//                Uri selectedImageUri = data.getData();
//                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
//                profileImage.setImageBitmap(imageToStore);
//                profileImage.setClipToOutline(true);
//            }
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

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