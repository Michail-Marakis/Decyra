package com.example.decyra.ui.Forum.personal_review;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ReviewDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Intent i = new Intent(this, ReviewDetailComposeActivity.class);

        // Review data
        i.putExtra("type", intent.getStringExtra("type"));
        i.putExtra("university", intent.getStringExtra("university"));
        i.putExtra("country", intent.getStringExtra("country"));
        i.putExtra("userName", intent.getStringExtra("userName"));
        i.putExtra("rating", intent.getFloatExtra("rating", 0f));
        i.putExtra("text", intent.getStringExtra("text"));
        i.putExtra("likes", intent.getIntExtra("likes", 0));
        i.putExtra("reviewId", intent.getStringExtra("reviewId"));
        i.putExtra("Views", intent.getIntExtra("Views", 0));
        i.putExtra("comments", intent.getIntExtra("comments", 0));

        // User data
        i.putExtra("userId", intent.getStringExtra("userId"));
        i.putExtra("userFullName", intent.getStringExtra("userFullName"));
        i.putExtra("userEmail", intent.getStringExtra("userEmail"));
        i.putExtra("userPhone", intent.getStringExtra("userPhone"));
        i.putExtra("modeType", intent.getStringExtra("modeType"));

        startActivity(i);
        finish();
    }
}