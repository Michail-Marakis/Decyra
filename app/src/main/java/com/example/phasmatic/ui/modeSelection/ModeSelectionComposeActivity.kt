package com.example.phasmatic.ui.modeSelection

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.phasmatic.ui.Chat.ChatActivity
import com.example.phasmatic.ui.Forum.ForumActivity
import com.example.phasmatic.ui.QuestionnaireActivity
import com.example.phasmatic.ui.conference.ConferenceActivity
import com.example.phasmatic.ui.notes.NotesActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ModeSelectionComposeActivity : ComponentActivity() {

    private val firebaseDb: FirebaseDatabase = FirebaseDatabase.getInstance(
        "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
    )

    private val usersRef: DatabaseReference = firebaseDb.getReference("users")

    private var profileUrl by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = intent.getStringExtra("userId")
        val userFullName = intent.getStringExtra("userFullName")
        val userEmail = intent.getStringExtra("userEmail")
        val userPhone = intent.getStringExtra("userPhone")

        loadProfileImage(userId) { url ->
            profileUrl = url
        }

        setContent {
            ModeSelectionScreen(
                userId = userId,
                userFullName = userFullName,
                userEmail = userEmail,
                userPhone = userPhone,
                profileImageUrl = profileUrl, // 🔥 NEW

                onModeSelected = { mode ->
                    startActivity(
                        Intent(this, QuestionnaireActivity::class.java).apply {
                            putExtra("userId", userId)
                            putExtra("userFullName", userFullName)
                            putExtra("userEmail", userEmail)
                            putExtra("userPhone", userPhone)
                            putExtra("modeType", mode)
                        }
                    )
                },

                onForumClick = {
                    startActivity(Intent(this, ForumActivity::class.java))
                },

                onProfileClick = {},

                onChatClick = {
                    startActivity(Intent(this, ChatActivity::class.java))
                },

                onConferenceClick = {
                    startActivity(Intent(this, ConferenceActivity::class.java))
                },

                onNotesClick = {
                    startActivity(Intent(this, NotesActivity::class.java))
                },

                onLogoutClick = {
                    finish()
                }
            )
        }
    }

    private fun loadProfileImage(
        userId: String?,
        onResult: (String?) -> Unit
    ) {
        if (userId.isNullOrEmpty()) {
            onResult(null)
            return
        }

        usersRef.child(userId).get()
            .addOnSuccessListener { snapshot ->
                val url = snapshot.child("profileImageUrl").value as? String
                onResult(url)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}