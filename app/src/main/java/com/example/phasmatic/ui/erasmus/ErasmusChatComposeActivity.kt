package com.example.phasmatic.ui.erasmus

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.phasmatic.data.ai.OpenAIChatClient
import com.example.phasmatic.extras.HTMLFileExporter
import com.example.phasmatic.extras.InternetConnection
import com.example.phasmatic.extras.ProfileImageManager
import com.example.phasmatic.ui.Chat.users_to_chat.UsersActivity
import com.example.phasmatic.ui.Profile_Menu.account_settings.AccountActivity
import com.example.phasmatic.ui.conference.general_conference.GeneralConferenceActivity
import com.example.phasmatic.ui.login.LoginActivity
import com.example.phasmatic.ui.notes.Notes.NotesActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ErasmusChatComposeActivity : AppCompatActivity() {

    private val inter = InternetConnection()
    private lateinit var chatClient: OpenAIChatClient
    private lateinit var usersRef: DatabaseReference
    private lateinit var userInfoRef: DatabaseReference

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
            "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
        )
        usersRef = firebaseDb.getReference("users")
        userInfoRef = firebaseDb.getReference("user_info")

        chatClient = OpenAIChatClient(this)

        loadProfilePhoto()

        setContent {
            ErasmusChatScreen(
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

        val convoId = "${userFullName}-ERASMUS"

        userInfoRef.child(userId ?: "").get().addOnSuccessListener { snapshot ->
            var uniId = 0
            if (snapshot.exists()) {
                val university = snapshot.child("university").getValue(String::class.java)
                uniId = mapUniversityToId(university)
            }

            chatClient.sendMessage(
                uniId,
                convoId,
                userMsg,
                userFullName,
                object : OpenAIChatClient.ChatCallback {
                    override fun onSuccess(reply: String) {
                        runOnUiThread {
                            chatMessages.add("Assistant:\n$reply")
                            llmReply = reply
                            isSending = false
                        }
                    }

                    override fun onError(error: String) {
                        runOnUiThread {
                            chatMessages.add("Error: $error")
                            isSending = false
                        }
                    }
                }
            )
        }.addOnFailureListener {
            Toast.makeText(this, "Firebase error", Toast.LENGTH_SHORT).show()
            isSending = false
        }
    }

    private fun exportHtml() {
        if (llmReply.trim().isEmpty()) {
            Toast.makeText(this, "No response to export", Toast.LENGTH_SHORT).show()
            return
        }

        val htmlIntent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/html"
            putExtra(Intent.EXTRA_TITLE, "erasmus_${System.currentTimeMillis()}.html")
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
            if (resultCode == RESULT_OK && data?.data != null) {
                val success = HTMLFileExporter.exportToHtml(this, data.data, llmReply)
                if (!success) {
                    Toast.makeText(this, "HTML export failed", Toast.LENGTH_SHORT).show()
                }
            }
            return
        }

        if (requestCode == REQUEST_SPEECH_RECOGNIZER) {
            if (resultCode == RESULT_OK && data != null) {
                val text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (!text.isNullOrEmpty()) {
                    inputText = text[0]
                }
            }
        }
    }

    private fun mapUniversityToId(university: String?): Int {
        if (university == null) return 0
        if (university.contains("Οικονομικό Πανεπιστήμιο Αθηνών")) return 0
        if (university.contains("Πανεπιστήμιο Θεσσαλίας")) return 4
        if (university.contains("Αριστοτέλειο Πανεπιστήμιο Θεσσαλονίκης")) return 5
        if (university.contains("Εθνικό και Καποδιστριακό Πανεπιστήμιο Αθηνών")) return 6
        if (university.contains("Πανεπιστήμιο Κρήτης")) return 7
        if (university.contains("Πανεπιστήμιο Πειραιώς")) return 8
        if (university.contains("Πανεπιστήμιο Πελοποννήσου")) return 9
        if (university.contains("Χαροκόπειο Πανεπιστήμιο")) return 10
        if (university.contains("Ιόνιο Πανεπιστήμιο")) return 11
        return 0
    }
}