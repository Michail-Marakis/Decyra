package com.example.phasmatic.ui.career;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.phasmatic.R;
import com.example.phasmatic.data.ai.OpenAIChatClient;
import com.example.phasmatic.extras.HTMLFileExporter;
import com.example.phasmatic.extras.InternetConnection;
import com.example.phasmatic.extras.ProfileImageManager;
import com.example.phasmatic.ui.BackButtonHelper;
import com.example.phasmatic.ui.Profile_Menu.ProfileMenuHelper;
import com.example.phasmatic.ui.erasmus.ErasmusChatComposeActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class CareerChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent source = getIntent();
        Intent i = new Intent(this, CareerChatComposeActivity.class);

        i.putExtra("userId", source.getStringExtra("userId"));
        i.putExtra("userFullName", source.getStringExtra("userFullName"));
        i.putExtra("userEmail", source.getStringExtra("userEmail"));
        i.putExtra("userPhone", source.getStringExtra("userPhone"));
        i.putExtra("userExpectations", source.getStringExtra("userExpectations"));

        startActivity(i);
        finish();
    }
}