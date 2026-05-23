package com.example.decyra.frontend.conference.conference_screen;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The type Conference activity.
 */
public class ConferenceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent source = getIntent();
        Intent i = new Intent(this, ConferenceComposeActivity.class);

        i.putExtra("userId", source.getStringExtra("userId"));
        i.putExtra("userFullName", source.getStringExtra("userFullName"));
        i.putExtra("userEmail", source.getStringExtra("userEmail"));
        i.putExtra("userPhone", source.getStringExtra("userPhone"));
        i.putExtra("code", source.getStringExtra("code"));
        i.putExtra("profileImageUrl", source.getStringExtra("profileImageUrl"));

        startActivity(i);
        finish();
    }
}