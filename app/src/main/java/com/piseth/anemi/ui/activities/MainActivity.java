package com.piseth.anemi.ui.activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.imagekit.android.ImageKit;
import com.imagekit.android.entity.TransformationPosition;
import com.imagekit.android.entity.UploadPolicy;
import com.piseth.anemi.R;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.AnemiUtils;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN = 3000;
    //Variables
    private Animation topAnim, bottomAnim;
    private ImageView image;
    private FirebaseAuth auth;
    private SharedPreferences loggedInUser;
    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

//        ImageKit.Companion.init(
//                getApplicationContext(),
//                AnemiUtils.PUBLIC_KEY,
//                AnemiUtils.URL_ENDPOINT,
//                TransformationPosition.PATH,
//                new UploadPolicy.Builder()
//                        .requireNetworkType(UploadPolicy.NetworkType.ANY)
//                        .maxRetries(5)
//                        .build()
//        );
        contextOfApplication = getApplicationContext();
        //Animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Hooks
        image = findViewById(R.id.imageView);

        image.setAnimation(topAnim);
        auth = FirebaseAuth.getInstance();
        loggedInUser = getSharedPreferences(AnemiUtils.LOGGED_IN_USER, MODE_PRIVATE);

        new Handler().postDelayed(() -> {
            Intent intent;
            User loggedUser = AnemiUtils.getLoggedInUser(loggedInUser);
            FirebaseUser currentUser = auth.getCurrentUser();
            if(loggedUser !=null || currentUser != null) {
                intent = new Intent(MainActivity.this, BookDashBoardActivity.class);
            } else {
                intent = new Intent(MainActivity.this, Login.class);
            }
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(image, "logo_image");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
            startActivity(intent, options.toBundle());
            finish();
        }, SPLASH_SCREEN);

    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }
}
