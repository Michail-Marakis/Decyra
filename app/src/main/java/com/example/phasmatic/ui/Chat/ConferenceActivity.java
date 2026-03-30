package com.example.phasmatic.ui.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phasmatic.R;
import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceConfig;
import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceFragment;

public class ConferenceActivity extends AppCompatActivity {

    private String userId;
    private String conferenceID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        conferenceID = intent.getStringExtra("conferenceID");

        addFragment();


    }

    public void addFragment() {
        long appID = 581663696;
        String appSign = "b8262be56354041edcd4128c02f73d0858f0d143f55036a2b79b45b16f61011c";

        String userName = "user_" + userId; // απλό display name

        ZegoUIKitPrebuiltVideoConferenceConfig config = new ZegoUIKitPrebuiltVideoConferenceConfig();

        ZegoUIKitPrebuiltVideoConferenceFragment fragment =
                ZegoUIKitPrebuiltVideoConferenceFragment.newInstance(
                        appID,
                        appSign,
                        userId,        // UNIQUE user
                        userName,
                        conferenceID,  // SAME ROOM
                        config
                );

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();
    }


}
