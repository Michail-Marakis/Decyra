package com.example.decyra.frontend.conference.conference_screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.example.decyra.extras.InternetConnection
import com.example.decyra.extras.ProfileImageManager
import com.example.decyra.frontend.Chat.users_to_chat.UsersActivity
import com.example.decyra.frontend.Profile_Menu.account_settings.AccountActivity
import com.example.decyra.frontend.conference.general_conference.GeneralConferenceActivity
import com.example.decyra.frontend.login.LoginActivity
import com.example.decyra.frontend.notes.Notes.NotesActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceConfig
import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceFragment

class ConferenceComposeActivity : AppCompatActivity() {

    private val inter = InternetConnection()

    private var permissionsGranted by mutableStateOf(false)
    private var showPermissionDenied by mutableStateOf(false)

    private var userId: String = ""
    private var code: String = ""
    private var userName: String = "User"

    private var userFullName: String? = null
    private var userEmail: String? = null
    private var userPhone: String? = null

    private var fragmentAttached = false

    private var profileImageUrl: String? = null

    private var profileBitmap: Bitmap? = null

    private lateinit var usersRef: DatabaseReference

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = result[Manifest.permission.CAMERA] == true &&
                result[Manifest.permission.RECORD_AUDIO] == true

        permissionsGranted = granted
        showPermissionDenied = !granted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!inter.isConnected(this)) {
            inter.showCustomDialog(this)
        }

        userId = intent.getStringExtra("userId").orEmpty()
        code = intent.getStringExtra("code").orEmpty()
        userName = intent.getStringExtra("userFullName").orEmpty().ifBlank { "User" }
        userEmail = intent.getStringExtra("userEmail")
        userPhone = intent.getStringExtra("userPhone")
        profileImageUrl = intent.getStringExtra("profileImageUrl")


        permissionsGranted = hasAllPermissions()

        val db = FirebaseDatabase.getInstance(
            "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
        )

        usersRef = db.getReference("users")

        loadProfilePhoto()

        setContent {
            ConferenceRoomScreen(
                userName = userName,
                roomCode = code,
                permissionsGranted = permissionsGranted,
                showPermissionDenied = showPermissionDenied,
                profileImageUrl = profileImageUrl,
                profileBitmap = profileBitmap,
                onBackClick = {
                    finish()
                },
                onRequestPermissions = {
                    requestPermissions()
                },
                onDismissPermissionWarning = {
                    showPermissionDenied = false
                },
                onFragmentHostReady = { containerId ->
                    if (permissionsGranted && !fragmentAttached) {
                        attachConferenceFragment(containerId)
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

    private fun hasAllPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
        )
    }

    private fun attachConferenceFragment(containerId: Int) {
        if (userId.isBlank() || code.isBlank()) {
            Toast.makeText(this, "Missing conference data", Toast.LENGTH_SHORT).show()
            return
        }

        val appID = 750984672L
        val appSign = "407fa63422294713ac2817e379891688f659af2e652fa3f45d3e428c90c4e888"
        val config = ZegoUIKitPrebuiltVideoConferenceConfig()

        val fragment = ZegoUIKitPrebuiltVideoConferenceFragment.newInstance(
            appID,
            appSign,
            userId,
            userName,
            code,
            config
        )

        supportFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .commitNowAllowingStateLoss()

        fragmentAttached = true
    }
}
