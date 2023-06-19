package com.piseth.anemi.ui.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.piseth.anemi.R;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.AnemiUtils;
import com.piseth.anemi.utils.util.DatabaseManageHandler;

public class Login extends AppCompatActivity {

    private ImageView image;
    private TextInputLayout username, password;
    //    private Button signIn, signAdmin;
    private DatabaseManageHandler db;
    private User user;
    private SharedPreferences loggedInUser;
    private FirebaseAuth auth;
    private CollectionReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        image = findViewById(R.id.image_logo);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        userRef = firebaseFirestore.collection("Users");
        loggedInUser = getSharedPreferences(AnemiUtils.LOGGED_IN_USER, MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void btnSignUpOnClick(View view) {
        Intent intent = new Intent(Login.this, Register.class);
        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(image, "logo_image");
        pairs[1] = new Pair<View, String>(username, "username_input");
        pairs[2] = new Pair<View, String>(password, "password_input");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
        startActivity(intent, options.toBundle());
//        finish();
    }

    public void btnSignInOnClick(View view) {
        if (this.username.getEditText() != null && this.password.getEditText() != null) {
            String username = this.username.getEditText().getText().toString();
            String password = this.password.getEditText().getText().toString();
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                login(username,password);
            } else {
                //get the email associated with the username
                userRef.whereEqualTo("username", username).limit(1).addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.d("Error", error.getMessage());
                        return;
                    }
                    if (value != null && !value.isEmpty()) {
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            User user = documentSnapshot.toObject(User.class);
                            Log.d("LOGIN", "User " + user.getUsername() + " is found");
                            if(user.getUsername().equals(username)) {
                                login(user.getEmail(), password);
                            }
                        }
                    } else {
                        Toast.makeText(Login.this, "Login failed! Incorrect Credential!!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void login(String email, String password) {
//        if (!isValidUsername(email) | !isValidPassword(password)) return;
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(Login.this, "Login Successfully! Welcome back " + email + "!!!", Toast.LENGTH_SHORT).show();
                FirebaseUser currentUser = auth.getCurrentUser();
                if(currentUser != null) {
                    saveCurrentUserToPreference(auth.getCurrentUser());
                }
            } else {
                Toast.makeText(Login.this, "Login failed! Incorrect Credential!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveCurrentUserToPreference(FirebaseUser currentUser) {
        userRef.document(currentUser.getUid()).get().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                DocumentSnapshot document = task1.getResult();
                if (document.exists()) {
                    User user = document.toObject(User.class);
                    AnemiUtils.setUserPreference(loggedInUser, user);
                    startActivity(new Intent(Login.this, BookDashBoardActivity.class));
                }
            }
        });
    }


//   No longer use, switch to FirebaseAuth
    public boolean checkExistedUser(String username, String password) {
        boolean isExisted = false;
        if (!username.isEmpty() && !password.isEmpty()) user = db.getUser(username);
        if (user != null) {
            if (password.equals(user.getPassword())) {
                SharedPreferences.Editor prefsEditor = loggedInUser.edit();
//                byte[] userPhoto = AnemiUtils.getBitmapAsByteArray(user.getPhoto());
                Gson gson = new Gson();
                String json = gson.toJson(user);
                prefsEditor.putString(AnemiUtils.LOGGED_IN_USER, json);
//                prefsEditor.putString(AnemiUtils.USER_PHOTO, AnemiUtils.BASE64Encode(userPhoto));
                prefsEditor.apply();
//                Toast.makeText(getApplicationContext(), "Successfully Logged in Username " + username, Toast.LENGTH_SHORT).show();
                Log.d("USERNAME: ", username + " was found");
                isExisted = true;
            }
        }
        return isExisted;
    }
}