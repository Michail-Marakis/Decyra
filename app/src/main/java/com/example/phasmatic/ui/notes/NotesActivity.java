package com.example.phasmatic.ui.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.phasmatic.R;
import com.example.phasmatic.data.model.Note;
import com.example.phasmatic.extras.InternetConnection;
import com.example.phasmatic.extras.ProfileImageManager;
import com.example.phasmatic.ui.BackButtonHelper;
import com.example.phasmatic.ui.Profile_Menu.ProfileMenuHelper;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotesActivity extends AppCompatActivity {

    private MaterialButton addNoteBtn;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<Note> notesList = new ArrayList<>();
    private DatabaseReference notesRef, usersRef;

    private ImageButton btnBack;
    private ImageView imgProfile;

    private String userId, userFullName, userEmail, userPhone;
    private ProfileMenuHelper profileMenuHelper;

    private InternetConnection inter = new InternetConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);


        if(!inter.isConnected(this)){
            inter.showCustomDialog(this);
        }

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userFullName = intent.getStringExtra("userFullName");
        userEmail = intent.getStringExtra("userEmail");
        userPhone = intent.getStringExtra("userPhone");

        btnBack = findViewById(R.id.btnBack);
        imgProfile = findViewById(R.id.imgProfile);
        addNoteBtn = findViewById(R.id.addnewnotebtn);
        recyclerView = findViewById(R.id.recyclerview);

        FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance(
                "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
        );

        notesRef = firebaseDb.getReference("notes");
        usersRef = firebaseDb.getReference("users");

        profileMenuHelper = new ProfileMenuHelper(
                this,
                userId,
                userFullName,
                userEmail,
                userPhone
        );

        imgProfile.setOnClickListener(v -> profileMenuHelper.showProfileMenu(v));

        BackButtonHelper.attachToGoModeSelection(this, R.id.btnBack, userId, userFullName, userEmail, userPhone);

        loadProfilePhoto();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new MyAdapter(this, notesList, note -> {
            Intent i = new Intent(NotesActivity.this, AddNoteActivity.class);
            i.putExtra("userId", userId);
            i.putExtra("userFullName", userFullName);
            i.putExtra("userEmail", userEmail);
            i.putExtra("userPhone", userPhone);

            i.putExtra("noteId", note.getId());
            i.putExtra("title", note.getTitle());
            i.putExtra("description", note.getDescription());
            i.putExtra("createdTime", note.getCreatedTime());

            startActivity(i);
        });

        recyclerView.setAdapter(myAdapter);

        addNoteBtn.setOnClickListener(v -> {
            Intent i = new Intent(NotesActivity.this, AddNoteActivity.class);
            i.putExtra("userId", userId);
            i.putExtra("userFullName", userFullName);
            i.putExtra("userEmail", userEmail);
            i.putExtra("userPhone", userPhone);
            startActivity(i);
        });

        loadNotes();
    }

    private void loadNotes() {
        notesRef.orderByChild("user_id").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        notesList.clear();

                        for (DataSnapshot child : snapshot.getChildren()) {
                            Note note = child.getValue(Note.class);
                            if (note != null) {
                                notesList.add(note);
                            }
                        }

                        Collections.sort(notesList, (n1, n2) -> {
                            //prwta pinned
                            if (n1.isPinned() && !n2.isPinned()) return -1;
                            if (!n1.isPinned() && n2.isPinned()) return 1;
                            //prwta pinned kai meta me createdTime
                            return Long.compare(n2.getCreatedTime(), n1.getCreatedTime());
                        });

                        myAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
    }

    private void loadProfilePhoto() {
        if (userId == null || userId.isEmpty()) {
            imgProfile.setImageResource(R.drawable.baseline_face_24);
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
                        .into(imgProfile);
            } else {
                Bitmap bitmap = ProfileImageManager.loadBitmap(this, userId);
                if (bitmap != null) {
                    imgProfile.setImageBitmap(bitmap);
                } else {
                    imgProfile.setImageResource(R.drawable.baseline_face_24);
                }
            }
        });
    }
}