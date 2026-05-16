package com.example.decyra.ui.conference.conference_1_N;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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

        startActivity(i);
        finish();
    }
}