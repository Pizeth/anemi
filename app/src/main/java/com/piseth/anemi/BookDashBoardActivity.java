package com.piseth.anemi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BookDashBoardActivity extends AppCompatActivity {

//    ActivityMainBinding binding;
    public static String LOGGED_IN_USER = "logged_user";
    public static String USER_PHOTO = "user_photo";
    private BottomNavigationView bottomMenu;
    private MaterialToolbar topMenu;
    private HomeFragment homeFragment = new HomeFragment();
    private UserManageFragment userManageFragment =  new UserManageFragment();
    private UserProfileFragment userProfileFragment = new UserProfileFragment();
    private SharedPreferences loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        binding.
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_book_dash_board);
        bottomMenu = findViewById(R.id.bottom_navigation_menu);
        topMenu = findViewById(R.id.top_tool_bar);
        loggedInUser = getSharedPreferences(LOGGED_IN_USER, MODE_PRIVATE);

//        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        replaceFragment(homeFragment);

//        BadgeDrawable badge = bottomMenu.getOrCreateBadge(R.id.user_manage);
//        badge.setVisible(true);
//        badge.setNumber(7);

        bottomMenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(homeFragment);
                    return true;
                case R.id.user_manage:
                    replaceFragment(userManageFragment);
                    return true;
                case R.id.user_profile:
                    replaceFragment(userProfileFragment);
                    return true;
            }
            return false;
        });
//        topMenu.setNavigationOnClickListener(); {
//            // Handle navigation icon press
//        }

        topMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.search:
//                    replaceFragment(homeFragment);
                    return true;
                case R.id.add:
//                    replaceFragment(userManageFragment);
                    return true;
                case R.id.logout:
                    SharedPreferences.Editor prefsEditor = loggedInUser.edit();
                    prefsEditor.remove(LOGGED_IN_USER);
                    prefsEditor.commit();
                    Intent intent = new Intent(BookDashBoardActivity.this, login.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        });
    }


//    @Override
//    public boolean onPrepareOptionsMenu (Menu menu) {
////        if (isFinalized) {
////            menu.getItem(1).setEnabled(false);
////            // You can also use something like:
////            // menu.findItem(R.id.example_foobar).setEnabled(false);
////        }
////        menu.getItem(1).setEnabled(false);
//        MenuItem item = menu.findItem(R.id.bottom_navigation_menu);
//        menu.getItem(1).setVisible(false);
//        return true;
//    }

    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        Menu myMenu = menu;
        MenuItem item = menu.findItem(R.id.user_manage);
//        if (item != null) {
//            item.setVisible(true);
//        }
        item.setVisible(false);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}