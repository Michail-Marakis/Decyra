package com.example.phasmatic.ui.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phasmatic.R;


public class GeneralConferenceActivity extends AppCompatActivity {

    private Button btntoCall;

    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_conference);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        btntoCall = findViewById(R.id.btntoConference);
        btntoCall.setOnClickListener(v->{
            Intent i = new Intent(this, ConferenceActivity.class);
            i.putExtra("userId", userId);
            startActivity(i);
        });
    }

}
