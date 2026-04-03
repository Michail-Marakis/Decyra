package com.example.phasmatic.ui.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.phasmatic.R;
import com.example.phasmatic.data.model.Note;
import com.example.phasmatic.extras.ProfileImageManager;
import com.example.phasmatic.ui.BackButtonHelper;
import com.example.phasmatic.ui.Profile_Menu.ProfileMenuHelper;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNoteActivity extends AppCompatActivity {

    private EditText titleInput, descriptionInput;
    private MaterialButton saveBtn;
    private ImageButton btnBack;
    private ImageView imgProfile;

    private DatabaseReference notesRef, usersRef;

    private String userId, userFullName, userEmail, userPhone;
    private ProfileMenuHelper profileMenuHelper;
    private String editingNoteId = null;
    private long editingCreatedTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        titleInput = findViewById(R.id.titleinput);
        descriptionInput = findViewById(R.id.descriptioninput);
        saveBtn = findViewById(R.id.savebtn);
        btnBack = findViewById(R.id.btnBack);
        imgProfile = findViewById(R.id.imgProfile);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userFullName = intent.getStringExtra("userFullName");
        userEmail = intent.getStringExtra("userEmail");
        userPhone = intent.getStringExtra("userPhone");

        editingNoteId = intent.getStringExtra("noteId");
        String noteTitle = intent.getStringExtra("title");
        String noteDescription = intent.getStringExtra("description");
        editingCreatedTime = intent.getLongExtra("createdTime", 0L);

        if (editingNoteId != null) {
            titleInput.setText(noteTitle);
            descriptionInput.setText(noteDescription);
        }


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

        BackButtonHelper.attachToGoNotes(this, R.id.btnBack, userId, userFullName, userEmail, userPhone);

        loadProfilePhoto();

        saveBtn.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                titleInput.setError("Title is required");
                return;
            }

            if (editingNoteId == null) {
                //neo note
                String noteId = notesRef.push().getKey();
                if (noteId == null) {
                    Toast.makeText(this, "Failed to create note id", Toast.LENGTH_SHORT).show();
                    return;
                }

                long createdTime = System.currentTimeMillis();
                Note note = new Note(noteId, title, description, createdTime, userId);

                notesRef.child(noteId).setValue(note)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
            } else {
                //edit uparxonots note
                Note note = new Note(
                        editingNoteId,
                        title,
                        description,
                        editingCreatedTime == 0L ? System.currentTimeMillis() : editingCreatedTime,
                        userId
                );

                notesRef.child(editingNoteId).setValue(note)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
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