package com.piseth.anemi;

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
import com.google.gson.Gson;

public class login extends AppCompatActivity {

//    private static int SPLASH_SCREEN = 10;
    public static String LOGGED_IN_USER = "logged_user";
    public static String USER_PHOTO = "user_photo";
    private ImageView image;
    private TextInputLayout username,  password;
//    private Button signIn, signAdmin;
    private DatabaseManageHandler db;
    private User user;
    private SharedPreferences loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        loggedInUser = getSharedPreferences(LOGGED_IN_USER, MODE_PRIVATE);
        image = findViewById(R.id.image_logo);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
//        signIn = findViewById(R.id.btnSignIn);
//        signAdmin = findViewById(R.id.btnSignAdmin);
        db = new DatabaseManageHandler(this);

        if (loggedInUser.contains(LOGGED_IN_USER)) {
            Intent intent = new Intent(login.this, BookDashBoardActivity.class);
            startActivity(intent);
        }
    }

    public void btnSignUpOnClick(View view) {
        Intent intent = new Intent(login.this, Register.class);
        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(image, "logo_image");
        pairs[1] = new Pair<View, String>(username, "username_input");
        pairs[2] = new Pair<View, String>(password, "password_input");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(login.this, pairs);
        startActivity(intent, options.toBundle());
//        finish();
    }

    public void btnSignInOnClick(View view) {
        if(this.username.getEditText() != null && this.password.getEditText() != null) {
            String username = this.username.getEditText().getText().toString();
            String password = this.password.getEditText().getText().toString();
            if(!isValidUsername() || !isValidPassword()) return;
            if (checkExistedUser(username, password)) {
                Toast.makeText(this, "Login Successfully! Welcome back " + username + "!!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(login.this, BookDashBoardActivity.class);
                startActivity(intent);
            } else Toast.makeText(this, "Login failed! Incorrect Credential!!!", Toast.LENGTH_SHORT).show();
        }
    }
//    public void btnSignAsAdminOnClick(View view) {
//        Intent intent = new Intent(login.this, Admin_Login.class);
//        Pair[] pairs = new Pair[5];
//        pairs[0] = new Pair<View, String>(image, "logo_image");
//        pairs[1] = new Pair<View, String>(username, "username_input");
//        pairs[2] = new Pair<View, String>(password, "password_input");
//        pairs[3] = new Pair<View, String>(signIn, "sign_in");
//        pairs[4] = new Pair<View, String>(signAdmin, "user_sign");
//        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(login.this, pairs);
//        startActivity(intent, options.toBundle());
//        finish();
//    }

    public boolean checkExistedUser(String username, String password) {
        boolean isExisted = false;
        if(!username.isEmpty() && !password.isEmpty()) user = db.getUser(username);
        if(user != null) {
            if(password.equals(user.getPassword())) {
                SharedPreferences.Editor prefsEditor = loggedInUser.edit();
                byte[] userPhoto = AnemiUtils.getBitmapAsByteArray(user.getPhoto());
                Gson gson = new Gson();
                String json = gson.toJson(user);
                prefsEditor.putString(LOGGED_IN_USER, json);
                prefsEditor.putString(USER_PHOTO, AnemiUtils.BASE64Encode(userPhoto));
                prefsEditor.apply();
//                Toast.makeText(getApplicationContext(), "Successfully Logged in Username " + username, Toast.LENGTH_SHORT).show();
                Log.d("USERNAME: ", username + " was found");
                isExisted = true;
            }
        }
        return isExisted;
    }

    private boolean isValidUsername() {
        String val = (username.getEditText() != null) ? username.getEditText().getText().toString().trim() : "";
//        String noWhiteSpace = "(?\\s+$)";
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if(val.isEmpty()) {
            username.setError("Username cannot be empty!");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            username.setError("White space is not allow!");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }
    private boolean isValidPassword() {
        String val = (password.getEditText() != null) ? password.getEditText().getText().toString().trim() : "";
        if(val.isEmpty()) {
            password.setError("Username cannot be empty!");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}