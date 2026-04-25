package com.example.phasmatic.ui.Chat.chat;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Intent i = new Intent(this, ChatComposeActivity.class);

        i.putExtra("userId", intent.getStringExtra("userId"));
        i.putExtra("otherUid", intent.getStringExtra("otherUid"));
        i.putExtra("otherName", intent.getStringExtra("otherName"));
        i.putExtra("userFullName", intent.getStringExtra("userFullName"));
        i.putExtra("userEmail", intent.getStringExtra("userEmail"));
        i.putExtra("userPhone", intent.getStringExtra("userPhone"));

        startActivity(i);
        finish();
    }
}


