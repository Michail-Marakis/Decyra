package com.example.decyra.frontend.Profile_Menu.edit_academic_account_settings;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * The type Edit user info activity.
 */
public class EditUserInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Intent i = new Intent(this, EditUserInfoComposeActivity.class);
        i.putExtra("userId", intent.getStringExtra("userId"));
        startActivity(i);
        finish();
    }
}