package com.piseth.anemi.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.piseth.anemi.ui.fragments.dialog.AddBookFragment;
import com.piseth.anemi.ui.fragments.fragment.BookManagementFragment;
import com.piseth.anemi.ui.fragments.fragment.HomeFragment;
import com.piseth.anemi.ui.fragments.fragment.UserManageFragment;
import com.piseth.anemi.ui.fragments.fragment.UserProfileFragment;
import com.piseth.anemi.utils.model.User;
import com.piseth.anemi.utils.util.AnemiUtils;

public class BookDashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
    private BookManagementFragment bookManagementFragment;
    private SharedPreferences loggedInUser;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth auth;
//    private FirebaseUser loggedInUser;
//    private FirebaseFirestore firebaseFirestore;
//    private CollectionReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        binding.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_book_dash_board);
        bottomMenu = findViewById(R.id.bottom_navigation_menu);
//Hook
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        topMenu = findViewById(R.id.top_tool_bar);
        //Toolbar
        setSupportActionBar(topMenu);
        //Navigation Drawer Menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, topMenu, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        loggedInUser = getSharedPreferences(AnemiUtils.LOGGED_IN_USER, MODE_PRIVATE);

//        firebaseFirestore = FirebaseFirestore.getInstance();
//        userRef = firebaseFirestore.collection("Users");
        homeFragment = new HomeFragment();
        userManageFragment =  new UserManageFragment();
        userProfileFragment = new UserProfileFragment();
        bookManagementFragment = new BookManagementFragment();
        addBookFragment = new AddBookFragment();
//        auth = FirebaseAuth.getInstance();
//        loggedInUser = auth.getCurrentUser();
        User user = AnemiUtils.getLoggedInUser(loggedInUser);

        //        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        if (savedInstanceState == null) {
            replaceFragment(homeFragment);
        }

        if(user != null) {
            ImageView profile = navigationView.getHeaderView(0).findViewById(R.id.imageProfile);
            TextView username = navigationView.getHeaderView(0).findViewById(R.id.text_username);
//            Log.d("success", loggedInUser.getString(USER_PHOTO, ""));
//            byte[] photo = AnemiUtils.BASE64Decode(loggedInUser.getString(USER_PHOTO, ""));
//            profile.setImageBitmap(AnemiUtils.getBitmapFromBytesArray(photo));
//            profile.setImageURI(user.getPhoto());
            Glide.with(profile.getContext()).load(user.getPhoto()).into(profile);
            profile.setCropToPadding(true);
            profile.setClipToOutline(true);
            username.setText(user.getUsername());
            Log.i("ddfd", "Current role is " + user.getUserRoleId());
            if(user.getUserRoleId() != AnemiUtils.ROLE_ADMIN) {
                bottomMenu.getMenu().findItem(R.id.book_manage).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_book_manage).setVisible(false);
                bottomMenu.getMenu().findItem(R.id.user_manage).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_user_manage).setVisible(false);
            }
        }
//        bottomMenu.getMenu().findItem(R.id.user_manage).setEnabled(false);
//        bottomMenu.getMenu().findItem(R.id.user_manage).setCheckable(false);

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
    protected void onStart() {
        super.onStart();
//        loggedInUser = auth.getCurrentUser();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        MenuItem item = menu.findItem(R.id.user_manage);
//        item.setVisible(false);
//        menu.removeItem(2);
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
//            SharedPreferences.Editor prefsEditor = loggedInUser.edit();
//            prefsEditor.remove(LOGGED_IN_USER);
//            prefsEditor.apply();
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