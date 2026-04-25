package com.example.phasmatic.ui.Profile_Menu.edit_account_settings

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.phasmatic.extras.InternetConnection
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditProfileComposeActivity : AppCompatActivity() {

    private lateinit var usersRef: DatabaseReference
    private val inter = InternetConnection()

    private var userId: String? = null

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var phone by mutableStateOf("")

    var isSaving by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!inter.isConnected(this)) {
            inter.showCustomDialog(this)
        }

        userId = intent.getStringExtra("userId")
        name = intent.getStringExtra("userFullName") ?: ""
        email = intent.getStringExtra("userEmail") ?: ""
        phone = intent.getStringExtra("userPhone") ?: ""

        val firebaseDb = FirebaseDatabase.getInstance(
            "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
        )
        usersRef = firebaseDb.getReference("users")

        setContent {
            EditProfileScreen(
                name = name,
                email = email,
                phone = phone,
                isSaving = isSaving,
                onNameChange = { name = it },
                onEmailChange = { email = it },
                onPhoneChange = { phone = it },
                onBackClick = { finish() },
                onSaveClick = { saveProfile() }
            )
        }
    }

    private fun saveProfile() {
        val uid = userId
        val newName = name.trim()
        val newEmail = email.trim()
        val newPhone = phone.trim()

        if (uid.isNullOrEmpty()) {
            Toast.makeText(this, "User id missing", Toast.LENGTH_SHORT).show()
            return
        }

        isSaving = true

        usersRef.child(uid).child("fullName").setValue(newName)
        usersRef.child(uid).child("email").setValue(newEmail)
        usersRef.child(uid).child("phoneNumber").setValue(newPhone)
            .addOnSuccessListener {
                isSaving = false
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                isSaving = false
                Toast.makeText(this, "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}