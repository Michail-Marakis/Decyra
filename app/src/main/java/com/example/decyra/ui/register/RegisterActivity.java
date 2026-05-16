package com.example.decyra.ui.register;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.decyra.ui.register.RegisterComposeActivity;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(this, RegisterComposeActivity.class);
        startActivity(i);
        finish();
    }
}