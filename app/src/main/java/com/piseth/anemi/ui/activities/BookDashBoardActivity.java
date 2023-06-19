package com.piseth.anemi.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.piseth.anemi.R;
import com.piseth.anemi.ui.fragments.fragment.BookManagementFragment;
import com.piseth.anemi.ui.fragments.fragment.HomeFragment;
import com.piseth.anemi.ui.fragments.fragment.UserManageFragment;
import com.piseth.anemi.ui.fragments.fragment.UserProfileFragment;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.AnemiUtils;

public class BookDashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private HomeFragment homeFragment;
    private UserManageFragment userManageFragment;
    private UserProfileFragment userProfileFragment;
    private BookManagementFragment bookManagementFragment;
    private SharedPreferences loggedInUser;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_book_dash_board);
        //    ActivityMainBinding binding;
        BottomNavigationView bottomMenu = findViewById(R.id.bottom_navigation_menu);
        //Hook
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        MaterialToolbar topMenu = findViewById(R.id.top_tool_bar);
        //Toolbar
        setSupportActionBar(topMenu);
        //Navigation Drawer Menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, topMenu, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        loggedInUser = getSharedPreferences(AnemiUtils.LOGGED_IN_USER, MODE_PRIVATE);

        homeFragment = new HomeFragment();
        userManageFragment =  new UserManageFragment();
        userProfileFragment = new UserProfileFragment();
        bookManagementFragment = new BookManagementFragment();
        User user = AnemiUtils.getLoggedInUser(loggedInUser);

        if (savedInstanceState == null) {
            replaceFragment(homeFragment);
        }

        if(user != null) {
            ImageView profile = navigationView.getHeaderView(0).findViewById(R.id.imageProfile);
            TextView username = navigationView.getHeaderView(0).findViewById(R.id.text_username);
            Glide.with(profile.getContext()).load(user.getPhoto()).into(profile);
            profile.setCropToPadding(true);
            profile.setClipToOutline(true);
            username.setText(user.getUsername());
            if(user.getUserRoleId() != AnemiUtils.ROLE_ADMIN) {
                bottomMenu.getMenu().findItem(R.id.book_manage).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_book_manage).setVisible(false);
                bottomMenu.getMenu().findItem(R.id.user_manage).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_user_manage).setVisible(false);
            }
        }

//        switch to using sharePreference

//        if(loggedInUser != null) {
//            userRef.document(loggedInUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            User user = document.toObject(User.class);
//                            ImageView profile = navigationView.getHeaderView(0).findViewById(R.id.imageProfile);
//                            TextView username = navigationView.getHeaderView(0).findViewById(R.id.text_username);
//                            Glide.with(profile.getContext()).load(user.getPhoto()).into(profile);
//                            profile.setCropToPadding(true);
//                            profile.setClipToOutline(true);
//                            username.setText(user.getUsername());
//                            if(user.getUserRoleId() != AnemiUtils.ROLE_ADMIN) {
//                                bottomMenu.getMenu().findItem(R.id.user_manage).setVisible(false);
//                                navigationView.getMenu().findItem(R.id.nav_user_manage).setVisible(false);
//                            }
//                        } else {
//                            Log.d(TAG, "No such document");
//                        }
//                    } else {
//                        Log.d(TAG, "get failed with ", task.getException());
//                    }
//                }
//            });
//        }

        bottomMenu.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(homeFragment);
                return true;
            } else if (itemId == R.id.book_manage) {
                replaceFragment(bookManagementFragment);
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

//        Not working due to using Navigation drawer

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

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_home) {
            replaceFragment(homeFragment);
        } else if (id == R.id.nav_book_manage) {
            replaceFragment(bookManagementFragment);
        } else if (id == R.id.nav_user_manage) {
            replaceFragment(userManageFragment);
        } else if (id == R.id.nav_user_profile) {
            replaceFragment(userProfileFragment);
        } else if (id == R.id.logout) {
            SharedPreferences.Editor prefsEditor = loggedInUser.edit();
            prefsEditor.remove(AnemiUtils.LOGGED_IN_USER);
            prefsEditor.apply();
            drawerLayout.close();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(BookDashBoardActivity.this, Login.class);
            startActivity(intent);
            return true;
        }
//        drawerLayout.closeDrawer(GravityCompat.START);
        item.setChecked(true);
        drawerLayout.close();
        return true;
    }
}