package com.piseth.anemi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

public class BookDashBoardActivity extends AppCompatActivity {

//    ActivityMainBinding binding;
    public static String LOGGED_IN_USER = "logged_user";
    public static String USER_PHOTO = "user_photo";
    public static final int ROLE_ADMIN = 3;
    private BottomNavigationView bottomMenu;
    private MaterialToolbar topMenu;
    private HomeFragment homeFragment;
    private UserManageFragment userManageFragment;
    private UserProfileFragment userProfileFragment;
    private AddBookFragment addBookFragment;
    private SharedPreferences loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        binding.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_book_dash_board);
        bottomMenu = findViewById(R.id.bottom_navigation_menu);
//        topMenu = findViewById(R.id.top_tool_bar);
        loggedInUser = getSharedPreferences(LOGGED_IN_USER, MODE_PRIVATE);
        homeFragment = new HomeFragment();
        userManageFragment =  new UserManageFragment();
        userProfileFragment = new UserProfileFragment();
        addBookFragment = new AddBookFragment();
        User user = AnemiUtils.getLoggedInUser(loggedInUser);

//        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        if (savedInstanceState == null) {
            replaceFragment(homeFragment);
        }

//        bottomMenu.getMenu().findItem(R.id.user_manage).setEnabled(false);
//        bottomMenu.getMenu().findItem(R.id.user_manage).setCheckable(false);

        if(user != null) {
            if(user.getUserRoleId() != ROLE_ADMIN) bottomMenu.getMenu().findItem(R.id.user_manage).setVisible(false);
        }

//        invalidateOptionsMenu();
//        bottomMenu.getMenu().findItem(R.id.user_manage).setIcon(new ColorDrawable(getResources().getColor(R.color.anemi)));
//        bottomMenu.findViewById(R.id.user_manage).setVisibility(View.GONE);
//        userManageFragment.setListener(this);

//        BadgeDrawable badge = bottomMenu.getOrCreateBadge(R.id.user_manage);
//        badge.setVisible(true);
//        badge.setNumber(7);

        bottomMenu.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(homeFragment);
                return true;
            } else if (itemId == R.id.user_manage) {
                replaceFragment(userManageFragment);
                return true;
            } else if (itemId == R.id.user_profile) {
                replaceFragment(userProfileFragment);
                return true;
            }
            return false;
        });
//        topMenu.setNavigationOnClickListener(); {
//            // Handle navigation icon press
//        }

//        topMenu.setOnMenuItemClickListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.search:
//                    replaceFragment(addBookFragment);
//                    return true;
//                case R.id.add:
//                    replaceFragment(addBookFragment);
//                    return true;
//                case R.id.logout:
//                    SharedPreferences.Editor prefsEditor = loggedInUser.edit();
//                    prefsEditor.remove(LOGGED_IN_USER);
//                    prefsEditor.apply();
//                    Intent intent = new Intent(BookDashBoardActivity.this, login.class);
//                    startActivity(intent);
//                    return true;
//            }
//            return false;
//        });
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

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.user_manage);
        item.setVisible(false);
        menu.removeItem(2);
        return super.onCreateOptionsMenu(menu);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}