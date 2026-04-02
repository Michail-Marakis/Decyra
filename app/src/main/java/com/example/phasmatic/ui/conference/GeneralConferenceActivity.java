package com.example.phasmatic.ui.conference;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.phasmatic.R;
import com.example.phasmatic.data.model.User;
import com.example.phasmatic.extras.ProfileImageManager;
import com.example.phasmatic.ui.ModeSelectionActivity;
import com.example.phasmatic.ui.Profile_Menu.ProfileMenuHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class GeneralConferenceActivity extends AppCompatActivity {

    private Button btnJoin, btnConfirm;

    private String userId;
    private ImageButton  btnBack;

    private ImageView btnProfile;
    private DatabaseReference usersRef;
    private String userFullName, userEmail, userPhone;

    private ProfileMenuHelper profileMenuHelper;
    DatabaseReference conferenceRef;
    ConferenceAdapter adapter;
    public List<User> userList = new ArrayList<>();


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_conference);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        btnProfile = findViewById(R.id.imgProfile);
        btnConfirm = findViewById(R.id.btnConfirmConference);
        btnJoin = findViewById(R.id.btnJoinRoom);
        btnBack = findViewById(R.id.btnBackConference);

        RecyclerView recyclerView = findViewById(R.id.recyclerUsers);

        FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance(
                "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
        );
        usersRef = firebaseDb.getReference("users");

        usersRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {

                userList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);

                    if (user != null && user.getId() != null) {
                        //if (!user.getId().equals(userId)) {  //an theloume admin sto UI
                            userList.add(user);
                        //}
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull com.google.firebase.database.DatabaseError error) {
                android.widget.Toast.makeText(GeneralConferenceActivity.this, "DB Error", android.widget.Toast.LENGTH_SHORT).show();
            }
        });


        adapter = new ConferenceAdapter(userList, user -> {});
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        userFullName = intent.getStringExtra("userFullName");
        userEmail = intent.getStringExtra("userEmail");
        userPhone = intent.getStringExtra("userPhone");


        profileMenuHelper = new ProfileMenuHelper(
                this,
                userId,
                userFullName,
                userEmail,
                userPhone
        );

        conferenceRef = FirebaseDatabase.getInstance(
                "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
        ).getReference("conferences");


        btnConfirm.setOnClickListener(v -> {

            List<String> selected = adapter.getSelectedUserIds();

            if (selected.isEmpty()) {
                android.widget.Toast.makeText(this, "Choose at least one other user", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            String code = java.util.UUID.randomUUID().toString().substring(0, 6);

            DatabaseReference roomRef = conferenceRef.child(code);

            List<String> participants = new ArrayList<>(selected);

            if (!participants.contains(userId)) {
                participants.add(userId);
            }

            for (String uid : participants) {
                roomRef.child("participants").child(uid).setValue(true);
            }

            long now = System.currentTimeMillis();
            long end = now + (10 * 60 * 60 * 1000);

            //roomRef.child("createdBy").setValue(userId); an theloume admin sto UI
            roomRef.child("time_start").setValue(now);
            roomRef.child("time_end").setValue(end);
            roomRef.child("code").setValue(code);

            android.widget.Toast.makeText(
                    this,
                    "Room created. Code: " + code,
                    android.widget.Toast.LENGTH_LONG
            ).show();
        });



        btnJoin.setOnClickListener(v -> {

            AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("Enter Room Code");

            final android.widget.EditText input = new android.widget.EditText(this);
            input.setHint("Code...");
            builder.setView(input);

            builder.setPositiveButton("Join", (dialog, which) -> {
                String code = input.getText().toString().trim();

                conferenceRef.child(code).get().addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {

                        Intent i = new Intent(this, ConferenceActivity.class);
                        i.putExtra("code", code);
                        i.putExtra("userId", userId);
                        i.putExtra("userName", userFullName);
                        startActivity(i);

                    } else {
                        android.widget.Toast.makeText(this, "Invalid code", android.widget.Toast.LENGTH_SHORT).show();
                    }
                });

            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });




        btnBack.setOnClickListener(v->{
            Intent i = new Intent(this, ModeSelectionActivity.class);
            i.putExtra("userId",userId);
            startActivity(i);
        });

        btnProfile.setOnClickListener(v -> profileMenuHelper.showProfileMenu(v));
        loadProfilePhoto();



    }

    private void loadProfilePhoto() {
        if (userId == null || userId.isEmpty()) {
            btnProfile.setImageResource(R.drawable.baseline_face_24);
            return;
        }

        usersRef.child(userId).get().addOnSuccessListener(snapshot -> {
            String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);

            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                String displayUrl = profileImageUrl + "?t=" + System.currentTimeMillis();

                Glide.with(this)
                        .load(displayUrl)
                        .placeholder(R.drawable.baseline_face_24)
                        .error(R.drawable.baseline_face_24)
                        .into(btnProfile);
            } else {
                // fallback se local cache an uparxei
                Bitmap bitmap = ProfileImageManager.loadBitmap(this, userId);
                if (bitmap != null) {
                    btnProfile.setImageBitmap(bitmap);
                } else {
                    btnProfile.setImageResource(R.drawable.baseline_face_24);
                }
            }
        });
    }


}
