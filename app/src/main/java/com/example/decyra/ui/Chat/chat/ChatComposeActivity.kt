package com.example.decyra.ui.Chat.chat

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.decyra.data.model.Message
import com.example.decyra.extras.InternetConnection
import com.example.decyra.extras.NotificationSender
import com.example.decyra.extras.ProfileImageManager
import com.example.decyra.ui.Chat.users_to_chat.UsersActivity
import com.example.decyra.ui.Profile_Menu.account_settings.AccountActivity
import com.example.decyra.ui.conference.general_conference.GeneralConferenceActivity
import com.example.decyra.ui.login.LoginActivity
import com.example.decyra.ui.notes.Notes.NotesActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.Locale

class ChatComposeActivity : AppCompatActivity() {

    private val inter = InternetConnection()
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance(
        "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
    )

    private lateinit var conversationsRef: DatabaseReference
    private lateinit var messagesRef: DatabaseReference
    private lateinit var usersRef: DatabaseReference

    private val messages = mutableStateListOf<Message>()

    private var currentUid: String? = null
    private var otherUid: String? = null
    private var otherName: String? = null
    private var userFullName: String? = null
    private var userEmail: String? = null
    private var userPhone: String? = null

    var messageText by mutableStateOf("")
    var profileImageUrl by mutableStateOf<String?>(null)
    var profileBitmap by mutableStateOf<Bitmap?>(null)
    var conversationKey by mutableStateOf<String?>(null)

    private var messagesListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!inter.isConnected(this)) {
            inter.showCustomDialog(this)
        }

        currentUid = intent.getStringExtra("userId")
        otherUid = intent.getStringExtra("otherUid")
        otherName = intent.getStringExtra("otherName")
        userFullName = intent.getStringExtra("userFullName")
        userEmail = intent.getStringExtra("userEmail")
        userPhone = intent.getStringExtra("userPhone")

        conversationsRef = db.getReference("conversations")
        messagesRef = db.getReference("messages")
        usersRef = db.getReference("users")

        loadProfilePhoto()
        findOrCreateConversation()

        setContent {
            val speechLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                Log.i("DEMO-REQUESTCODE", "speechResult")
                Log.i("DEMO-RESULTCODE", result.resultCode.toString())

                if (result.resultCode == Activity.RESULT_OK) {
                    val text = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    if (!text.isNullOrEmpty()) {
                        messageText = text[0]
                        Log.i("DEMO-ANSWER", text[0])
                    }
                }
            }

            ChatScreen(
                currentUid = currentUid,
                otherName = otherName ?: "Chat",
                profileImageUrl = profileImageUrl,
                profileBitmap = profileBitmap,
                messages = messages,
                messageText = messageText,
                onMessageChange = { messageText = it },
                onBackClick = {
                    startActivity(
                        Intent(this, UsersActivity::class.java).apply {
                            putExtra("userId", currentUid)
                            putExtra("userFullName", userFullName)
                            putExtra("userEmail", userEmail)
                            putExtra("userPhone", userPhone)
                        }
                    )
                    finish()
                },
                onVoiceClick = {
                    val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(
                            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                        )
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "el-GR")
                        putExtra(
                            RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE,
                            "el-GR"
                        )
                    }

                    try {
                        speechLauncher.launch(speechIntent)
                    } catch (a: ActivityNotFoundException) {
                        Toast.makeText(
                            applicationContext,
                            "Η αναγνώριση φωνής δεν υποστηρίζεται στη συσκευή σας",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onSendClick = { sendMessage() },
                onProfileClick = {
                    startActivity(
                        Intent(this, AccountActivity::class.java).apply {
                            putUserExtras(currentUid, userFullName, userEmail, userPhone)
                        }
                    )
                },

                onChatClick = {
                    startActivity(
                        Intent(this, UsersActivity::class.java).apply {
                            putUserExtras(currentUid, userFullName, userEmail, userPhone)
                        }
                    )
                    finish()
                },

                onConferenceClick = {
                    startActivity(
                        Intent(this, GeneralConferenceActivity::class.java).apply {
                            putUserExtras(currentUid, userFullName, userEmail, userPhone)
                        }
                    )
                    finish()
                },

                onNotesClick = {
                    startActivity(
                        Intent(this, NotesActivity::class.java).apply {
                            putUserExtras(currentUid, userFullName, userEmail, userPhone)
                        }
                    )
                    finish()
                },

                onLogoutClick = {
                    logout(currentUid)
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
        val uid = currentUid
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

    private fun findOrCreateConversation() {
        conversationsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val left = ds.child("leftUser_id").getValue(String::class.java)
                    val right = ds.child("rightUser_id").getValue(String::class.java)

                    val match = left != null && right != null &&
                            ((left == currentUid && right == otherUid) ||
                                    (left == otherUid && right == currentUid))

                    if (match) {
                        conversationKey = ds.key
                        loadMessages()
                        return
                    }
                }
                createConversation()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun createConversation() {
        val newRef = conversationsRef.push()
        conversationKey = newRef.key
        if (conversationKey == null) return

        val conv = HashMap<String, Any?>()
        conv["id"] = conversationKey
        conv["lastMessage"] = ""
        conv["leftUser_id"] = otherUid
        conv["rightUser_id"] = currentUid
        conv["timeLastMessage"] = nowString()

        newRef.setValue(conv).addOnSuccessListener { loadMessages() }
    }

    private fun loadMessages() {
        messagesListener?.let { messagesRef.removeEventListener(it) }

        messagesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()

                for (ds in snapshot.children) {
                    val convId = ds.child("conversationId").getValue(String::class.java)
                    if (conversationKey == null || convId == null || conversationKey != convId) continue

                    val m = Message()
                    m.id = ds.child("id").getValue(String::class.java)
                    m.conversationId = convId
                    m.sender_id = ds.child("sender_id").getValue(String::class.java)
                    m.receiver_id = ds.child("receiver_id").getValue(String::class.java)
                    m.messageText = ds.child("messageText").getValue(String::class.java)
                    m.timeMessage = ds.child("timeMessage").getValue(String::class.java)
                    m.statusOfMessage = ds.child("statusOfMessage").getValue(String::class.java)

                    messages.add(m)
                }

                messages.sortWith { a, b ->
                    when {
                        a.timeMessage == null || b.timeMessage == null -> 0
                        else -> a.timeMessage.compareTo(b.timeMessage)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        messagesRef.addValueEventListener(messagesListener as ValueEventListener)
    }

    private fun sendMessage() {
        val text = messageText.trim()
        val convKey = conversationKey
        if (TextUtils.isEmpty(text) || convKey == null) return

        val now = nowString()
        val newMsgRef = messagesRef.push()
        val key = newMsgRef.key ?: return

        val msg = HashMap<String, Any?>()
        msg["id"] = key
        msg["conversationId"] = convKey
        msg["sender_id"] = currentUid
        msg["receiver_id"] = otherUid
        msg["messageText"] = text
        msg["statusOfMessage"] = "1"
        msg["timeMessage"] = now

        newMsgRef.setValue(msg)
        conversationsRef.child(convKey).child("lastMessage").setValue(text)
        conversationsRef.child(convKey).child("timeLastMessage").setValue(now)

        val senderName = userFullName ?: "User"
        val targetUid = otherUid
        if (!targetUid.isNullOrBlank()) {
            db.getReference("users").child(targetUid).child("fcmToken")
                .get().addOnSuccessListener { snapshot ->
                    val token = snapshot.getValue(String::class.java)
                    if (!token.isNullOrEmpty()) {
                        NotificationSender.send(
                            token,
                            "new_message",
                            senderName,
                            text
                        )
                    }
                }
        }

        messageText = ""
    }

    private fun nowString(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .format(Date())
    }

    override fun onDestroy() {
        super.onDestroy()
        messagesListener?.let { messagesRef.removeEventListener(it) }
    }
}