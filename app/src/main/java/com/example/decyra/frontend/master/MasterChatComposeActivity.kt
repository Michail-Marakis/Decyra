package com.example.decyra.frontend.master

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import com.example.decyra.backend.ai.callbacks.ChatCallback
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.decyra.backend.ai.clients.OpenAIChatClient
import com.example.decyra.extras.HTMLFileExporter
import com.example.decyra.extras.InternetConnection
import com.example.decyra.extras.ProfileImageManager
import com.example.decyra.frontend.Profile_Menu.account_settings.AccountActivity
import com.example.decyra.frontend.conference.general_conference.GeneralConferenceActivity
import com.example.decyra.frontend.login.LoginActivity
import com.example.decyra.frontend.Chat.users_to_chat.UsersActivity
import com.example.decyra.frontend.notes.Notes.NotesActivity
import com.example.decyra.frontend.shared_chat.UnifiedChatScreen
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MasterChatComposeActivity : AppCompatActivity() {

    private val inter = InternetConnection()
    private lateinit var chatClient: OpenAIChatClient
    private lateinit var usersRef: DatabaseReference

    private var userId: String? = null
    private var userFullName: String? = null
    private var userEmail: String? = null
    private var userPhone: String? = null
    private var userExpectations: String? = null

    var profileImageUrl by mutableStateOf<String?>(null)
    var profileBitmap by mutableStateOf<Bitmap?>(null)

    var inputText by mutableStateOf("")
    var llmReply by mutableStateOf("")
    var isSending by mutableStateOf(false)

    val chatMessages = mutableStateListOf<String>()

    private val CREATE_HTML_FILE = 2003
    private val REQUEST_SPEECH_RECOGNIZER = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!inter.isConnected(this)) {
            inter.showCustomDialog(this)
        }

        val source = intent
        userId = source.getStringExtra("userId")
        userFullName = source.getStringExtra("userFullName")
        userEmail = source.getStringExtra("userEmail")
        userPhone = source.getStringExtra("userPhone")
        userExpectations = source.getStringExtra("userExpectations")

        inputText = userExpectations.orEmpty()

        val firebaseDb = FirebaseDatabase.getInstance(
            "CLOUD_PATH"
        )
        usersRef = firebaseDb.getReference("users")

        chatClient = OpenAIChatClient(this)

        loadProfilePhoto()

        setContent {
            UnifiedChatScreen(
                title = "Master",
                subtitle = "Assistant",
                userFullName = userFullName,
                profileImageUrl = profileImageUrl,
                profileBitmap = profileBitmap,
                inputText = inputText,
                messages = chatMessages,
                isSending = isSending,
                onInputChange = { inputText = it },
                onBackClick = { finish() },
                onSendClick = { sendMessage() },
                onVoiceClick = { startSpeechRecognizer() },
                onSaveClick = { exportHtml() },
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

    private fun sendMessage() {
        val userMsg = inputText.trim()
        if (userMsg.isEmpty() || isSending) return

        chatMessages.add("You: $userMsg")
        inputText = ""
        isSending = true

        val convoId = "${userFullName}-MASTER"

        chatClient.sendMessage(
            1,
            convoId,
            userMsg,
            userFullName,
            object : ChatCallback {
                override fun onSuccess(reply: String) {
                    runOnUiThread {
                        chatMessages.add("Assistant:\n$reply")
                        llmReply = reply
                        isSending = false
                    }
                }

                override fun onError(error: String) {
                    runOnUiThread {
                        val displayMsg = when{
                            error == "TIMEOUT_ERROR" ->
                                "Cant connect to the server fast enough. Try again later"
                            else ->
                                "Unexpected error occurred. (Information: $error)"
                        }
                        chatMessages.add("Assistant:\n$displayMsg")
                        isSending = false
                    }
                }
            }
        )
    }

    private fun exportHtml() {
        if (llmReply.trim().isEmpty()) {
            Toast.makeText(this, "No response to export", Toast.LENGTH_SHORT).show()
            return
        }

        val htmlIntent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/html"
            putExtra(Intent.EXTRA_TITLE, "master_${System.currentTimeMillis()}.html")
        }

        startActivityForResult(htmlIntent, CREATE_HTML_FILE)
    }

    private fun startSpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "el-GR")
            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "el-GR")
        }

        try {
            startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER)
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(
                applicationContext,
                "Η αναγνώριση φωνής δεν υποστηρίζεται στη συσκευή σας",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CREATE_HTML_FILE) {
            if (resultCode == Activity.RESULT_OK && data?.data != null) {
                val success = HTMLFileExporter.exportToHtml(this, data.data, llmReply)
                if (!success) {
                    Toast.makeText(this, "HTML export failed", Toast.LENGTH_SHORT).show()
                }
            }
            return
        }

        if (requestCode == REQUEST_SPEECH_RECOGNIZER) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (!text.isNullOrEmpty()) {
                    inputText = text[0]
                }
            }
        }
    }
}