package com.example.decyra.frontend.notes.add_note;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * The type Add note activity.
 */
public class AddNoteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Intent i = new Intent(this, AddNoteComposeActivity.class);

        i.putExtra("userId", intent.getStringExtra("userId"));
        i.putExtra("userFullName", intent.getStringExtra("userFullName"));
        i.putExtra("userEmail", intent.getStringExtra("userEmail"));
        i.putExtra("userPhone", intent.getStringExtra("userPhone"));

        i.putExtra("noteId", intent.getStringExtra("noteId"));
        i.putExtra("title", intent.getStringExtra("title"));
        i.putExtra("description", intent.getStringExtra("description"));
        i.putExtra("createdTime", intent.getLongExtra("createdTime", 0L));

        startActivity(i);
        finish();
    }
}