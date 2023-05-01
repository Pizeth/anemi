package com.piseth.anemi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class BookDashBoardAcivity extends AppCompatActivity {

    BottomNavigationView bottomMenu;
    HomeFragment homeFragment = new HomeFragment();
    UserManageFragment userManageFragment =  new UserManageFragment();
    UserProfileFragment userProfileFragment = new UserProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_dash_board);

        bottomMenu = findViewById(R.id.bottom_navigation_menu);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        BadgeDrawable badge = bottomMenu.getOrCreateBadge(R.id.user_manage);
        badge.setVisible(true);
        badge.setNumber(7);

        bottomMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;
                    case R.id.user_manage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, userManageFragment).commit();
                        return true;
                    case R.id.user_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, userProfileFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}