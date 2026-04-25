package com.example.phasmatic.ui.Profile_Menu.public_profile

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.phasmatic.extras.InternetConnection
import com.example.phasmatic.extras.ProfileImageManager
import com.example.phasmatic.ui.Chat.chat.ChatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PublicProfileComposeActivity : AppCompatActivity() {

    private val inter = InternetConnection()
    private lateinit var usersRef: DatabaseReference

    private var profileUid: String? = null
    private var currentUserId: String? = null
    private var loadedFullName: String? = null

    private var userFullName: String? = null
    private var userEmail: String? = null
    private var userPhone: String? = null

    var profileImageUrl by mutableStateOf<String?>(null)
    var profileBitmap by mutableStateOf<Bitmap?>(null)

    var txtFullName by mutableStateOf("-")
    var txtEmail by mutableStateOf("-")
    var txtPhone by mutableStateOf("-")

    var txtUniversity by mutableStateOf("-")
    var txtAcademicLevel by mutableStateOf("-")
    var txtField by mutableStateOf("-")
    var txtLanguages by mutableStateOf("-")
    var txtGpa by mutableStateOf("-")
    var txtBudget by mutableStateOf("-")
    var txtYearOfStudies by mutableStateOf("-")
    var txtAdvisorType by mutableStateOf("-")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!inter.isConnected(this)) {
            inter.showCustomDialog(this)
        }

        val intent = intent

        profileUid = intent.getStringExtra("userId")
        currentUserId = intent.getStringExtra("currentUserId")

        userFullName = intent.getStringExtra("userFullName")
        userEmail = intent.getStringExtra("userEmail")
        userPhone = intent.getStringExtra("userPhone")

        val firebaseDb = FirebaseDatabase.getInstance(
            "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
        )
        usersRef = firebaseDb.getReference("users")

        if (!profileUid.isNullOrEmpty()) {
            loadUserBasic()
            loadUserInfo()
            loadProfilePhoto()
        }

        setContent {
            PublicProfileScreen(
                fullName = txtFullName,
                email = txtEmail,
                phone = txtPhone,
                university = txtUniversity,
                academicLevel = txtAcademicLevel,
                field = txtField,
                languages = txtLanguages,
                gpa = txtGpa,
                budget = txtBudget,
                yearOfStudies = txtYearOfStudies,
                advisorType = txtAdvisorType,
                profileImageUrl = profileImageUrl,
                profileBitmap = profileBitmap,
                onBackClick = { finish() },
                onChatClick = {
                    val loggedInUid = currentUserId
                    val otherUid = profileUid
                    if (loggedInUid == null || otherUid == null) return@PublicProfileScreen

                    val i = Intent(this, ChatActivity::class.java).apply {
                        putExtra("userId", loggedInUid)
                        putExtra("otherUid", otherUid)

                        val nameForChat = loadedFullName ?: txtFullName.trim()
                        putExtra("otherName", nameForChat)
                        putExtra("userEmail", userEmail)
                        putExtra("userPhone", userPhone)
                    }

                    startActivity(i)
                }
            )
        }
    }

    private fun loadProfilePhoto() {
        val uid = profileUid
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

    private fun loadUserBasic() {
        val uid = profileUid ?: return

        FirebaseDatabase.getInstance(
            "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
        ).getReference("users").child(uid).get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.exists()) return@addOnSuccessListener

                val fullName = snapshot.child("fullName").getValue(String::class.java)
                val email = snapshot.child("email").getValue(String::class.java)
                val phone = snapshot.child("phoneNumber").getValue(String::class.java)

                loadedFullName = fullName
                txtFullName = fullName ?: "-"
                txtEmail = email ?: "-"
                txtPhone = phone ?: "-"
            }
    }

    private fun loadUserInfo() {
        val uid = profileUid ?: return

        FirebaseDatabase.getInstance(
            "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
        ).getReference("user_info").child(uid).get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.exists()) return@addOnSuccessListener

                val university = snapshot.child("university").getValue(String::class.java)
                val academicLevel = snapshot.child("academicLevel").getValue(String::class.java)
                val field = snapshot.child("field").getValue(String::class.java)
                val languages = snapshot.child("languages").getValue(String::class.java)
                val gpa = snapshot.child("gpa").getValue(String::class.java)
                val budgetPerYear = snapshot.child("budgetPerYear").getValue(Double::class.java)
                val yearOfStudies = snapshot.child("yearOfStudies").getValue(Int::class.java)
                val advisorType = snapshot.child("advisorType").getValue(String::class.java)

                txtUniversity = university ?: "-"
                txtAcademicLevel = academicLevel ?: "-"
                txtField = field ?: "-"
                txtLanguages = languages ?: "-"
                txtGpa = gpa ?: "0.0"
                txtBudget = budgetPerYear?.toString() ?: "0.0"
                txtYearOfStudies = yearOfStudies?.toString() ?: "0"
                txtAdvisorType = advisorType ?: "-"
            }
    }
}