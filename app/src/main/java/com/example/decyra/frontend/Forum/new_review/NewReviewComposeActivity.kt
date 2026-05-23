package com.example.decyra.frontend.Forum.new_review

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.decyra.R
import com.example.decyra.backend.domain.ForumReview
import com.example.decyra.extras.InternetConnection
import com.example.decyra.extras.ProfileImageManager
import com.example.decyra.frontend.Chat.users_to_chat.UsersActivity
import com.example.decyra.frontend.Profile_Menu.account_settings.AccountActivity
import com.example.decyra.frontend.conference.general_conference.GeneralConferenceActivity
import com.example.decyra.frontend.login.LoginActivity
import com.example.decyra.frontend.notes.Notes.NotesActivity
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewReviewComposeActivity : AppCompatActivity() {

    private val inter = InternetConnection()

    private lateinit var forumRef: DatabaseReference
    private lateinit var countriesRef: DatabaseReference
    private lateinit var universitiesRef: DatabaseReference
    private lateinit var usersRef: DatabaseReference

    private var userId: String? = null
    private var userFullName: String? = null
    private var userEmail: String? = null
    private var userPhone: String? = null

    private val countryList = mutableStateListOf<String>()
    private val uniList = mutableStateListOf<String>()

    var selectedType by mutableStateOf("Erasmus")
    var selectedCountry by mutableStateOf<String?>(null)
    var selectedUniversity by mutableStateOf<String?>(null)
    var ratingText by mutableStateOf("")
    var reviewText by mutableStateOf("")

    var isSaving by mutableStateOf(false)

    var profileImageUrl by mutableStateOf<String?>(null)
    var profileBitmap by mutableStateOf<Bitmap?>(null)

    private val CHANNEL_ID = "forum_reviews_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!inter.isConnected(this)) {
            inter.showCustomDialog(this)
        }

        val incomingIntent = intent
        userId = incomingIntent.getStringExtra("userId")
        userFullName = incomingIntent.getStringExtra("userFullName")
        userEmail = incomingIntent.getStringExtra("userEmail")
        userPhone = incomingIntent.getStringExtra("userPhone")

        val db = FirebaseDatabase.getInstance(
            "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
        )

        forumRef = db.getReference("forum_reviews")
        countriesRef = db.getReference("countries")
        universitiesRef = db.getReference("universities")
        usersRef = db.getReference("users")

        createNotificationChannel()
        loadProfilePhoto()
        loadCountries()

        setContent {
            val speechLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val matches = result.data?.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS
                    )
                    reviewText = matches?.firstOrNull() ?: reviewText
                }
            }

            NewReviewScreen(
                selectedType = selectedType,
                countries = countryList,
                universities = uniList,
                selectedCountry = selectedCountry,
                selectedUniversity = selectedUniversity,
                ratingText = ratingText,
                reviewText = reviewText,
                isSaving = isSaving,
                profileImageUrl = profileImageUrl,
                profileBitmap = profileBitmap,
                onTypeSelected = { selectedType = it },
                onCountrySelected = { country ->
                    selectedCountry = country
                    selectedUniversity = null
                    uniList.clear()
                    if (!country.isNullOrBlank()) {
                        loadUniversitiesForCountry(country)
                    }
                },
                onUniversitySelected = { selectedUniversity = it },
                onRatingTextChange = { ratingText = it },
                onReviewTextChange = { reviewText = it },
                onBackClick = { finish() },
                onVoiceClick = {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(
                            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                        )
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "el-GR")
                    }
                    try {
                        speechLauncher.launch(intent)
                    } catch (e: Exception) {
                        Toast.makeText(
                            this,
                            "Η αναγνώριση φωνής δεν υποστηρίζεται",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onSaveClick = { saveReview() },

                onProfileClick = {
                    startActivity(
                        Intent(this, AccountActivity::class.java).apply {
                            putUserExtras(userId, userFullName, userEmail, userPhone)
                        }
                    )
                },

                onChatClick = {
                    startActivity(
                        Intent(this, UsersActivity::class.java).apply {
                            putUserExtras(userId, userFullName, userEmail, userPhone)
                        }
                    )
                    finish()
                },

                onConferenceClick = {
                    startActivity(
                        Intent(this, GeneralConferenceActivity::class.java).apply {
                            putUserExtras(userId, userFullName, userEmail, userPhone)
                        }
                    )
                    finish()
                },

                onNotesClick = {
                    startActivity(
                        Intent(this, NotesActivity::class.java).apply {
                            putUserExtras(userId, userFullName, userEmail, userPhone)
                        }
                    )
                    finish()
                },

                onLogoutClick = {
                    logout(userId)
                }
            )
        }
    }

    private fun Intent.putUserExtras(
        userId: String?,
        userFullName: String?,
        userEmail: String?,
        userPhone: String?
    ): Intent {
        putExtra("userId", userId)
        putExtra("userFullName", userFullName)
        putExtra("userEmail", userEmail)
        putExtra("userPhone", userPhone)
        return this
    }

    private fun logout(userId: String?) {
        if (userId.isNullOrEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        usersRef.child(userId).child("remember").setValue(0)
            .addOnCompleteListener {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
    }

    private fun loadProfilePhoto() {
        val uid = userId
        if (uid.isNullOrEmpty()) {
            profileImageUrl = null
            profileBitmap = null
            return
        }

        usersRef.child(uid).get().addOnSuccessListener { snapshot ->
            val remoteUrl = snapshot.child("profileImageUrl").getValue(String::class.java)

            if (!remoteUrl.isNullOrEmpty()) {
                profileImageUrl = "$remoteUrl?t=${System.currentTimeMillis()}"
                profileBitmap = null
            } else {
                val bitmap = ProfileImageManager.loadBitmap(this, uid)
                if (bitmap != null) {
                    profileBitmap = bitmap
                    profileImageUrl = null
                } else {
                    profileBitmap = null
                    profileImageUrl = null
                }
            }
        }
    }

    private fun loadCountries() {
        countriesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                countryList.clear()
                for (child in snapshot.children) {
                    val name = child.child("name").getValue(String::class.java)
                    if (!name.isNullOrBlank()) {
                        countryList.add(name)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun loadUniversitiesForCountry(countryName: String) {
        universitiesRef
            .orderByChild("country")
            .equalTo(countryName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    uniList.clear()

                    for (child in snapshot.children) {
                        val uniCountry = child.child("country").getValue(String::class.java)
                        val uniName = child.child("name").getValue(String::class.java)

                        if (!uniCountry.isNullOrBlank() &&
                            uniCountry.equals(countryName, ignoreCase = true) &&
                            !uniName.isNullOrBlank()
                        ) {
                            uniList.add(uniName)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun saveReview() {
        val type = if (selectedType == "Master") "master" else "erasmus"
        val country = selectedCountry
        val university = selectedUniversity
        val text = reviewText.trim()

        val ratingInt = try {
            ratingText.toInt()
        } catch (e: NumberFormatException) {
            0
        }

        if (country.isNullOrBlank() || university.isNullOrBlank() ||
            text.isBlank() || ratingInt !in 1..5
        ) {
            Toast.makeText(
                this,
                "Fill all fields and rating 1-5",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        isSaving = true

        val id = forumRef.push().key
        if (id == null) {
            isSaving = false
            Toast.makeText(this, "Error creating review id", Toast.LENGTH_SHORT).show()
            return
        }

        val createdAt = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        ).format(Date())

        val review = ForumReview(
            id,
            userId,
            userFullName,
            type,
            university,
            country,
            text,
            ratingInt.toFloat(),
            0L,
            createdAt,
            0
        )

        forumRef.child(id).setValue(review)
            .addOnSuccessListener {
                isSaving = false
                Toast.makeText(this, "Review posted", Toast.LENGTH_SHORT).show()
                showNotification(
                    "Νέο Review Δημοσιεύτηκε",
                    "Το review για το $university δημοσιεύτηκε με επιτυχία."
                )
                finish()
            }
            .addOnFailureListener { e ->
                isSaving = false
                Toast.makeText(
                    this,
                    "Failed: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Forum Reviews"
            val descriptionText = "Notifications for new forum reviews"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun showNotification(title: String, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        val intent = Intent(this, NewReviewComposeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.outline_circle_notifications_24)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(1, notification)
        }
    }
}
