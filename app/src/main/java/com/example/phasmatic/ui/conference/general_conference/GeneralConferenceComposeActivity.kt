package com.example.phasmatic.ui.conference.general_conference

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.phasmatic.data.model.Note
import com.example.phasmatic.data.model.User
import com.example.phasmatic.extras.InternetConnection
import com.example.phasmatic.extras.NotificationSender
import com.example.phasmatic.extras.ProfileImageManager
import com.example.phasmatic.ui.Chat.users_to_chat.UsersActivity
import com.example.phasmatic.ui.Profile_Menu.ProfileMenuHelper
import com.example.phasmatic.ui.Profile_Menu.account_settings.AccountActivity
import com.example.phasmatic.ui.conference.conference_1_N.ConferenceActivity
import com.example.phasmatic.ui.login.LoginActivity
import com.example.phasmatic.ui.modeSelection.ModeSelectionActivity
import com.example.phasmatic.ui.notes.Notes.NotesActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class GeneralConferenceComposeActivity : AppCompatActivity() {

    private lateinit var usersRef: DatabaseReference
    private lateinit var conferenceRef: DatabaseReference
    private lateinit var rootRef: DatabaseReference

    private var profileBitmap by mutableStateOf<Bitmap?>(null)
    private var profileUrl by mutableStateOf<String?>(null)
    private var searchQuery by mutableStateOf("")
    private var joinCode by mutableStateOf("")
    private var showJoinDialog by mutableStateOf(false)

    private val selectedUserIds = mutableStateListOf<String>()
    private val fullUserList = mutableStateListOf<User>()
    private val visibleUserList = mutableStateListOf<User>()

    private lateinit var userId: String
    private var userFullName: String? = null
    private var userEmail: String? = null
    private var userPhone: String? = null

    private lateinit var profileMenuHelper: ProfileMenuHelper

    private val inter = InternetConnection()
    private val firebaseDb: FirebaseDatabase = FirebaseDatabase.getInstance(
        "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!inter.isConnected(this)) {
            inter.showCustomDialog(this)
        }

        userId = intent.getStringExtra("userId").orEmpty()
        userFullName = intent.getStringExtra("userFullName")
        userEmail = intent.getStringExtra("userEmail")
        userPhone = intent.getStringExtra("userPhone")

        usersRef = firebaseDb.getReference("users")
        conferenceRef = firebaseDb.getReference("conferences")
        rootRef = firebaseDb.reference

        profileMenuHelper = ProfileMenuHelper(
            this,
            userId,
            userFullName,
            userEmail,
            userPhone
        )

        loadUsers()
        loadProfilePhoto()

        setContent {
            GeneralConferenceScreen(
                userFullName = userFullName,
                searchQuery = searchQuery,
                users = visibleUserList,
                selectedUserIds = selectedUserIds,
                profileImageUrl = profileUrl,
                profileBitmap = profileBitmap,
                showJoinDialog = showJoinDialog,
                joinCode = joinCode,
                onSearchChange = {
                    searchQuery = it
                    filterUsers(it)
                },
                onBackClick = {
                    startActivity(Intent(this, ModeSelectionActivity::class.java).apply {
                        putExtra("userId", userId)
                        putExtra("userFullName", userFullName)
                        putExtra("userEmail", userEmail)
                        putExtra("userPhone", userPhone)
                    })
                    finish()
                },
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
                },

                onToggleUser = { user ->
                    toggleUserSelection(user)
                },
                onConfirmClick = {
                    if (selectedUserIds.isEmpty()) {
                        Toast.makeText(this, "Choose at least one other user", Toast.LENGTH_SHORT).show()
                    } else {
                        showScheduleDialogs()
                    }
                },
                onJoinClick = {
                    showJoinDialog = true
                },
                onJoinCodeChange = {
                    joinCode = it
                },
                onDismissJoinDialog = {
                    showJoinDialog = false
                },
                onConfirmJoin = {
                    attemptJoinRoom(joinCode.trim())
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

    private fun loadUsers() {
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                fullUserList.clear()
                visibleUserList.clear()

                for (ds in snapshot.children) {
                    val user = ds.getValue(User::class.java)
                    if (user != null && !user.id.isNullOrBlank() && user.id != userId) {
                        fullUserList.add(user)
                    }
                }

                filterUsers(searchQuery)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@GeneralConferenceComposeActivity, "DB Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterUsers(query: String) {
        visibleUserList.clear()

        if (query.isBlank()) {
            visibleUserList.addAll(fullUserList)
            return
        }

        val lower = query.lowercase(Locale.getDefault()).trim()
        visibleUserList.addAll(
            fullUserList.filter { user ->
                val name = user.fullName?.lowercase(Locale.getDefault()).orEmpty()
                name.contains(lower)
            }
        )
    }

    private fun toggleUserSelection(user: User) {
        val uid = user.id ?: return
        if (selectedUserIds.contains(uid)) {
            selectedUserIds.remove(uid)
        } else {
            selectedUserIds.add(uid)
        }
    }

    private fun showScheduleDialogs() {
        val now = Calendar.getInstance()

        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        scheduleConference(year, month, dayOfMonth, hourOfDay, minute)
                    },
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    true
                ).show()
            },
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun scheduleConference(year: Int, month: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int) {
        val selectedDateTime = Calendar.getInstance().apply {
            set(year, month, dayOfMonth, hourOfDay, minute, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val startMillis = selectedDateTime.timeInMillis
        val eventDate = String.Companion.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth)
        val timeText = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(startMillis))
        val code = UUID.randomUUID().toString().replace("-", "").take(6).uppercase(Locale.getDefault())
        val notesRef = rootRef.child("notes")

        val participants = selectedUserIds.toMutableList().apply {
            if (!contains(userId)) add(userId)
        }

        val updates = hashMapOf<String, Any>()

        participants.forEach { uid ->
            updates["/conferences/$code/participants/$uid"] = true
        }

        updates["/conferences/$code/code"] = code
        updates["/conferences/$code/event_date"] = eventDate
        updates["/conferences/$code/time_start"] = startMillis
        updates["/conferences/$code/time_text"] = timeText
        updates["/conferences/$code/note"] = "Conference meeting"
        updates["/conferences/$code/created_by"] = userId
        updates["/conferences/$code/created_by_name"] = userFullName.orEmpty()

        participants.forEach { participantId ->
            val noteId = notesRef.push().key ?: return@forEach
            val noteTitle = "You have a new Video Conference"
            val noteDescription = "Code: $code\nCreated by: ${userFullName.orEmpty()}\nDate: $eventDate\nTime: $timeText"

            val note = Note(
                noteId,
                noteTitle,
                noteDescription,
                System.currentTimeMillis(),
                participantId,
                false
            )

            updates["/notes/$noteId"] = note

            val senderName = userFullName ?: "User"
            firebaseDb.getReference("users").child(participantId).child("fcmToken")
                .get()
                .addOnSuccessListener { snapshot ->
                    val token = snapshot.getValue(String::class.java)
                    if (!token.isNullOrEmpty()) {
                        NotificationSender.send(
                            token,
                            "new_meeting",
                            senderName,
                            "Invited to a new Conference"
                        )
                    }
                }
        }

        rootRef.updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Scheduled at $eventDate", Toast.LENGTH_LONG).show()
                selectedUserIds.clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun attemptJoinRoom(code: String) {
        if (code.isBlank()) {
            Toast.makeText(this, "Enter a code", Toast.LENGTH_SHORT).show()
            return
        }

        conferenceRef.child(code).get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.exists()) {
                    Toast.makeText(this, "Invalid code", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val startMillis = snapshot.child("time_start").getValue(Long::class.java)
                if (startMillis == null) {
                    Toast.makeText(this, "Invalid event time", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val now = System.currentTimeMillis()
                val fifteenMinutes = 15 * 60 * 1000
                val allowedJoinTime = startMillis - fifteenMinutes

                if (now >= allowedJoinTime) {
                    showJoinDialog = false
                    startActivity(Intent(this, ConferenceActivity::class.java).apply {
                        putExtra("code", code)
                        putExtra("userId", userId)
                        putExtra("userName", userFullName)
                    })
                } else {
                    val diffMillis = allowedJoinTime - now
                    val totalMinutes = diffMillis / 60000
                    val days = totalMinutes / 1440
                    val hours = (totalMinutes % 1440) / 60
                    val minutes = totalMinutes % 60

                    val message = when {
                        days >= 1 -> "Join opens in $days days $hours hours"
                        hours >= 1 -> "Join opens in $hours hours $minutes minutes"
                        else -> "Join opens in $minutes minutes"
                    }

                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error connecting to server", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadProfilePhoto() {
        if (userId.isBlank()) {
            profileUrl = null
            profileBitmap = null
            return
        }

        usersRef.child(userId).get().addOnSuccessListener { snapshot ->
            val remoteUrl = snapshot.child("profileImageUrl").getValue(String::class.java)
            if (!remoteUrl.isNullOrEmpty()) {
                profileUrl = remoteUrl + "?t=" + System.currentTimeMillis()
                profileBitmap = null
            } else {
                profileBitmap = ProfileImageManager.loadBitmap(this, userId)
                profileUrl = null
            }
        }
    }
}