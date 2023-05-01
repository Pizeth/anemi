package com.piseth.anemi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;

public class login extends AppCompatActivity {

    private static int SPLASH_SCREEN = 10;
    private ImageView image;
    private TextInputLayout username,  password;
    private Button signIn, signAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        image = findViewById(R.id.image_logo);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.btnSignIn);
        signAdmin = findViewById(R.id.btnSignAdmin);
    }

    public void btnSignUpOnClick(View view) {
        Intent intent = new Intent(login.this, Register.class);
        Pair pairs[] = new Pair[3];
        pairs[0] = new Pair<View, String>(image, "logo_image");
        pairs[1] = new Pair<View, String>(username, "username_input");
        pairs[2] = new Pair<View, String>(password, "password_input");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(login.this, pairs);
        startActivity(intent, options.toBundle());
//        finish();
    }

    public void btnSignInOnClick(View view) {
        Intent intent = new Intent(login.this, BookDashBoardAcivity.class);
        startActivity(intent);
    }
    public void btnSignAsAdminOnClick(View view) {
        Intent intent = new Intent(login.this, Admin_Login.class);
        Pair pairs[] = new Pair[5];
        pairs[0] = new Pair<View, String>(image, "logo_image");
        pairs[1] = new Pair<View, String>(username, "username_input");
        pairs[2] = new Pair<View, String>(password, "password_input");
        pairs[3] = new Pair<View, String>(signIn, "sign_in");
        pairs[4] = new Pair<View, String>(signAdmin, "user_sign");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(login.this, pairs);
        startActivity(intent, options.toBundle());
//        finish();
    }
}