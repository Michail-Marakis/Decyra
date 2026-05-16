package com.example.decyra.ui.Chat.users_to_chat

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.decyra.data.model.Conversation
import com.example.decyra.extras.InternetConnection
import com.example.decyra.extras.ProfileImageManager
import com.example.decyra.ui.Chat.chat.ChatActivity
import com.example.decyra.ui.Profile_Menu.account_settings.AccountActivity
import com.example.decyra.ui.conference.general_conference.GeneralConferenceActivity
import com.example.decyra.ui.login.LoginActivity
import com.example.decyra.ui.modeSelection.ModeSelectionActivity
import com.example.decyra.ui.notes.Notes.NotesActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UsersComposeActivity : AppCompatActivity() {

    private val inter = InternetConnection()
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance(
        "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
    )

    private lateinit var conversationsRef: DatabaseReference
    private lateinit var usersRef: DatabaseReference

    private var userId: String? = null
    private var userFullName: String? = null
    private var userEmail: String? = null
    private var userPhone: String? = null

    private val conversations = mutableStateListOf<Conversation>()
    private val userNames = mutableStateMapOf<String, String>()
    private val userImageUrls = mutableStateMapOf<String, String?>()

    var profileImageUrl by mutableStateOf<String?>(null)
    var profileBitmap by mutableStateOf<Bitmap?>(null)

    private var conversationsListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!inter.isConnected(this)) {
            inter.showCustomDialog(this)
        }

        val intent = getIntent()
        userId = intent.getStringExtra("userId")
        userFullName = intent.getStringExtra("userFullName")
        userEmail = intent.getStringExtra("userEmail")
        userPhone = intent.getStringExtra("userPhone")

        conversationsRef = db.getReference("conversations")
        usersRef = db.getReference("users")

        loadProfilePhoto()
        loadConversations()

        setContent {
            UsersScreen(
                conversations = conversations,
                currentUid = userId,
                profileImageUrl = profileImageUrl,
                profileBitmap = profileBitmap,
                resolveOtherUserName = { otherUid -> userNames[otherUid] ?: "Loading..." },
                resolveOtherUserImage = { otherUid -> userImageUrls[otherUid] },
                onBackClick = {
                    startActivity(
                        Intent(this, ModeSelectionActivity::class.java).apply {
                            putExtra("userId", userId)
                            putExtra("userFullName", userFullName)
                            putExtra("userEmail", userEmail)
                            putExtra("userPhone", userPhone)
                        }
                    )
                    finish()
                },
                onConversationClick = { conversation ->
                    val otherUid = if (userId == conversation.leftUser_id) {
                        conversation.rightUser_id
                    } else {
                        conversation.leftUser_id
                    }

                    if (otherUid.isNullOrBlank()) {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                    } else {
                        loadUserNameAndOpenChat(otherUid)
                    }
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

    private fun loadConversations() {
        conversationsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                conversations.clear()

                for (ds in snapshot.children) {
                    val c = ds.getValue(Conversation::class.java) ?: continue

                    if (!TextUtils.isEmpty(userId) &&
                        (userId == c.leftUser_id || userId == c.rightUser_id)
                    ) {
                        conversations.add(c)

                        val otherUid = if (userId == c.leftUser_id) c.rightUser_id else c.leftUser_id
                        if (!otherUid.isNullOrBlank()) {
                            preloadOtherUser(otherUid, c.timeLastMessage)
                        }
                    }
                }

                conversations.sortWith { a, b ->
                    when {
                        a.timeLastMessage == null || b.timeLastMessage == null -> 0
                        else -> b.timeLastMessage.compareTo(a.timeLastMessage)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        conversationsRef.addValueEventListener(conversationsListener as ValueEventListener)
    }

    private fun preloadOtherUser(otherUid: String, cacheSeed: String?) {
        usersRef.child(otherUid).get().addOnSuccessListener { snapshot ->
            val fullName = snapshot.child("fullName").getValue(String::class.java)
            userNames[otherUid] = if (!fullName.isNullOrBlank()) fullName else "User"

            val url = snapshot.child("profileImageUrl").getValue(String::class.java)
            userImageUrls[otherUid] = if (!url.isNullOrBlank()) {
                url + "?t=" + (cacheSeed ?: System.currentTimeMillis().toString())
            } else {
                null
            }
        }.addOnFailureListener {
            userNames[otherUid] = "User"
            userImageUrls[otherUid] = null
        }
    }

    private fun loadUserNameAndOpenChat(otherUid: String) {
        usersRef.child(otherUid).get().addOnSuccessListener { snapshot ->
            var otherName = "User"
            if (snapshot.exists()) {
                val name = snapshot.child("fullName").getValue(String::class.java)
                if (!TextUtils.isEmpty(name)) otherName = name!!
            }

            Intent(this, ChatActivity::class.java).also { i ->
                i.putExtra("userId", userId)
                i.putExtra("otherUid", otherUid)
                i.putExtra("otherName", otherName)
                i.putExtra("userEmail", userEmail)
                i.putExtra("userPhone", userPhone)
                i.putExtra("userFullName", userFullName)
                startActivity(i)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        conversationsListener?.let { conversationsRef.removeEventListener(it) }
    }
}

