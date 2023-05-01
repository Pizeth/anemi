package com.piseth.anemi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void btnBackToSignInOnClick(View view) {
        Intent intent = new Intent(Register.this, login.class);
        startActivity(intent);
    }

}