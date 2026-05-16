package com.example.decyra.ui.conference.general_conference;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GeneralConferenceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent source = getIntent();
        Intent i = new Intent(this, GeneralConferenceComposeActivity.class);

        if (source.getExtras() != null) {
            i.putExtras(source.getExtras());
        }

        startActivity(i);
        finish();
    }
}