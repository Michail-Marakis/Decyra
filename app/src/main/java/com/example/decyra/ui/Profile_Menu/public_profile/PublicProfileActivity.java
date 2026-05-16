package com.example.decyra.ui.Profile_Menu.public_profile;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class PublicProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Intent i = new Intent(this, PublicProfileComposeActivity.class);

        i.putExtra("userId", intent.getStringExtra("userId"));
        i.putExtra("currentUserId", intent.getStringExtra("currentUserId"));
        i.putExtra("userFullName", intent.getStringExtra("userFullName"));
        i.putExtra("userEmail", intent.getStringExtra("userEmail"));
        i.putExtra("userPhone", intent.getStringExtra("userPhone"));

        startActivity(i);
        finish();
    }
}