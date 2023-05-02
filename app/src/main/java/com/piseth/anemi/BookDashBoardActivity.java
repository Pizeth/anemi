package com.piseth.anemi;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.piseth.anemi.databinding.ActivityMainBinding;

public class BookDashBoardActivity extends AppCompatActivity {

//    ActivityMainBinding binding;
    BottomNavigationView bottomMenu;
    HomeFragment homeFragment = new HomeFragment();
    UserManageFragment userManageFragment =  new UserManageFragment();
    UserProfileFragment userProfileFragment = new UserProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        binding.
        setContentView(R.layout.activity_book_dash_board);
        bottomMenu = findViewById(R.id.bottom_navigation_menu);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

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