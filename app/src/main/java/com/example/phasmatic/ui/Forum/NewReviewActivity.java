package com.example.phasmatic.ui.Forum;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.phasmatic.R;
import com.example.phasmatic.data.model.ForumReview;
import com.example.phasmatic.extras.InternetConnection;
import com.example.phasmatic.ui.BackButtonHelper;
import com.example.phasmatic.ui.Profile_Menu.ProfileMenuHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.graphics.Bitmap;
import com.example.phasmatic.extras.ProfileImageManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewReviewActivity extends AppCompatActivity {

    private Spinner spnType, spnUniversity;
    private AutoCompleteTextView dropCountry;
    private EditText edtText, edtRating;
    private Button btnSave, btnVoice;
    private ImageButton btnBack;
    private ImageView imgProfile;

    private String userId, userFullName, userEmail, userPhone;
    private DatabaseReference forumRef, countriesRef, universitiesRef, usersRef;
    private ProfileMenuHelper profileMenuHelper;

    private ArrayAdapter<String> countryAdapter, uniAdapter;
    private final List<String> countryList = new ArrayList<>();
    private final List<String> uniList = new ArrayList<>();

    private String selectedCountry = null;
    private String selectedUniversity = null;
    private static final String CHANNEL_ID = "forum_reviews_channel";
    private NotificationManager notificationManager;

    private InternetConnection inter = new InternetConnection();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_review);


        if(!inter.isConnected(this)){
            inter.showCustomDialog(this);
        }

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userFullName = intent.getStringExtra("userFullName");
        userEmail = intent.getStringExtra("userEmail");
        userPhone = intent.getStringExtra("userPhone");

        spnType = findViewById(R.id.spnType);
        dropCountry = findViewById(R.id.dropCountry);
        spnUniversity = findViewById(R.id.spnUniversity);
        edtRating = findViewById(R.id.edtRating);
        edtText = findViewById(R.id.edtText);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        imgProfile = findViewById(R.id.imgProfile);
        btnVoice = findViewById(R.id.btnVoice);

        profileMenuHelper = new ProfileMenuHelper(this, userId, userFullName, userEmail, userPhone);
        imgProfile.setOnClickListener(v -> profileMenuHelper.showProfileMenu(v));

        BackButtonHelper.attach(this, R.id.btnBack);

        List<String> types = Arrays.asList("Erasmus", "Master");
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                this, R.layout.item_dropdown_dark, types);
        typeAdapter.setDropDownViewResource(R.layout.item_dropdown_dark);
        spnType.setAdapter(typeAdapter);

        FirebaseDatabase db = FirebaseDatabase.getInstance(
                "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app");
        forumRef = db.getReference("forum_reviews");
        countriesRef = db.getReference("countries");
        universitiesRef = db.getReference("universities");
        usersRef = db.getReference("users");
        loadProfilePhoto();
        setupCountryUniversityDropdowns();

        btnSave.setOnClickListener(v -> saveReview());

        btnVoice.setOnClickListener(v -> startSpeechRecognizer());
        createNotificationChannel();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    private void showNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Review Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Intent intent = new Intent(this, NewReviewActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, CHANNEL_ID);
        } else {
            builder = new Notification.Builder(this);
        }

        builder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.outline_circle_notifications_24)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Forum Reviews";
            String description = "Notifications for new forum reviews";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
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
                // fallback se local cache an uparxei
                Bitmap bitmap = ProfileImageManager.loadBitmap(this, userId);
                if (bitmap != null) {
                    imgProfile.setImageBitmap(bitmap);
                } else {
                    imgProfile.setImageResource(R.drawable.baseline_face_24);
                }
            }
        });
    }

    private void startSpeechRecognizer() {
        int REQUEST_SPEECH_RECOGNIZER = 3000;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "el-GR");

        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "el-GR");


        try {
            startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Η αναγνώριση φωνής δεν υποστηρίζεται στη συσκευή σας", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int REQUEST_SPEECH_RECOGNIZER = 3000;
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("DEMO-REQUESTCODE", Integer.toString(requestCode));
        Log.i("DEMO-RESULTCODE", Integer.toString(resultCode));

        if (requestCode == REQUEST_SPEECH_RECOGNIZER && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            edtText.setText(text.get(0));

            Log.i("DEMO-ANSWER", text.get(0));

        } else {
            System.out.println("Recognizer API error");
        }
    }


    private void setupCountryUniversityDropdowns() {
        countryAdapter = new ArrayAdapter<>(
                this, R.layout.item_dropdown_dark, countryList);
        countryAdapter.setDropDownViewResource(R.layout.item_dropdown_dark);
        dropCountry.setAdapter(countryAdapter);
        dropCountry.setThreshold(0);
        dropCountry.setOnClickListener(v -> dropCountry.showDropDown());

        uniAdapter = new ArrayAdapter<>(
                this, R.layout.item_dropdown_dark, uniList);
        uniAdapter.setDropDownViewResource(R.layout.item_dropdown_dark);
        spnUniversity.setAdapter(uniAdapter);

        countriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                countryList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String name = child.child("name").getValue(String.class);
                    if (name != null) countryList.add(name);
                }
                countryAdapter.notifyDataSetChanged();
            }

            @Override public void onCancelled(DatabaseError error) {}
        });

        dropCountry.setOnItemClickListener((parent, view, position, id) -> {
            selectedCountry = parent.getItemAtPosition(position).toString();

            selectedUniversity = null;
            uniList.clear();
            uniAdapter.notifyDataSetChanged();

            loadUniversitiesForCountry(selectedCountry);
        });

        spnUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < uniList.size()) {
                    selectedUniversity = uniList.get(position);
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {
                selectedUniversity = null;
            }
        });
    }

    private void loadUniversitiesForCountry(String countryName) {
        universitiesRef.orderByChild("country")
                .equalTo(countryName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot snapshot) {
                        uniList.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            String uniCountry = child.child("country").getValue(String.class);
                            String uniName = child.child("name").getValue(String.class);
                            if (uniCountry != null
                                    && uniCountry.equalsIgnoreCase(countryName)
                                    && uniName != null) {
                                uniList.add(uniName);
                            }
                        }
                        uniAdapter.notifyDataSetChanged();
                    }
                    @Override public void onCancelled(DatabaseError error) {}
                });
    }

    private void saveReview() {
        String typeUi = (String) spnType.getSelectedItem();
        String type = "erasmus";
        if ("Master".equalsIgnoreCase(typeUi)) type = "master";

        String country = selectedCountry != null ? selectedCountry : "";
        String university = selectedUniversity != null ? selectedUniversity : "";
        String text = edtText.getText().toString().trim();

        int ratingInt;
        try {
            ratingInt = Integer.parseInt(edtRating.getText().toString().trim());
        } catch (NumberFormatException e) {
            ratingInt = 0;
        }

        if (country.isEmpty() || university.isEmpty() || text.isEmpty()
                || ratingInt < 1 || ratingInt > 5) {
            Toast.makeText(this, "Fill all fields and rating 1-5", Toast.LENGTH_SHORT).show();
            return;
        }

        float rating = ratingInt;

        String id = forumRef.push().getKey();
        if (id == null) {
            Toast.makeText(this, "Error creating review id", Toast.LENGTH_SHORT).show();
            return;
        }

        long likes = 0L;
        String createdAt = new java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                java.util.Locale.getDefault()
        ).format(new java.util.Date());

        ForumReview review = new ForumReview(
                id,
                userId,
                userFullName,
                type,
                university,
                country,
                text,
                rating,
                likes,
                createdAt,
                0
        );


        forumRef.child(id)
                .setValue(review)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Review posted", Toast.LENGTH_SHORT).show();
                    showNotification(
                            "Νέο Review Δημοσιεύτηκε",
                            "Το review για το " + university + " δημοσιεύτηκε με επιτυχία."
                    );
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

}