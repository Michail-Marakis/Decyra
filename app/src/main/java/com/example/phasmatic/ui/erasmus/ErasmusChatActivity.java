package com.example.phasmatic.ui.erasmus;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ErasmusChatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent source = getIntent();
        Intent i = new Intent(this, ErasmusChatComposeActivity.class);

        i.putExtra("userId", source.getStringExtra("userId"));
        i.putExtra("userFullName", source.getStringExtra("userFullName"));
        i.putExtra("userEmail", source.getStringExtra("userEmail"));
        i.putExtra("userPhone", source.getStringExtra("userPhone"));
        i.putExtra("userExpectations", source.getStringExtra("userExpectations"));

        startActivity(i);
        finish();
    }
}