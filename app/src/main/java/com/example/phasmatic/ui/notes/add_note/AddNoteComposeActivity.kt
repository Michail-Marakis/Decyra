package com.example.phasmatic.ui.notes.add_note

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.phasmatic.data.model.Note
import com.example.phasmatic.extras.InternetConnection
import com.example.phasmatic.extras.ProfileImageManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddNoteComposeActivity : AppCompatActivity() {

    private lateinit var notesRef: DatabaseReference
    private lateinit var usersRef: DatabaseReference

    private val inter = InternetConnection()

    private var userId: String? = null
    private var userFullName: String? = null
    private var userEmail: String? = null
    private var userPhone: String? = null

    private var editingNoteId: String? = null
    private var editingCreatedTime: Long = 0L

    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var titleError by mutableStateOf<String?>(null)
    var isSaving by mutableStateOf(false)

    var profileImageUrl by mutableStateOf<String?>(null)
    var profileBitmap by mutableStateOf<Bitmap?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!inter.isConnected(this)) {
            inter.showCustomDialog(this)
        }

        val intent = intent
        userId = intent.getStringExtra("userId")
        userFullName = intent.getStringExtra("userFullName")
        userEmail = intent.getStringExtra("userEmail")
        userPhone = intent.getStringExtra("userPhone")

        editingNoteId = intent.getStringExtra("noteId")
        title = intent.getStringExtra("title") ?: ""
        description = intent.getStringExtra("description") ?: ""
        editingCreatedTime = intent.getLongExtra("createdTime", 0L)

        val firebaseDb = FirebaseDatabase.getInstance(
            "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
        )
        notesRef = firebaseDb.getReference("notes")
        usersRef = firebaseDb.getReference("users")

        loadProfilePhoto()

        setContent {
            AddNoteScreen(
                title = title,
                description = description,
                titleError = titleError,
                isEditing = editingNoteId != null,
                isSaving = isSaving,
                profileImageUrl = profileImageUrl,
                profileBitmap = profileBitmap,
                onTitleChange = {
                    title = it
                    if (it.trim().isNotEmpty()) titleError = null
                },
                onDescriptionChange = {
                    description = it
                },
                onBackClick = { finish() },
                onSaveClick = { saveNote() }
            )
        }
    }

    private fun saveNote() {
        val uid = userId
        val noteTitle = title.trim()
        val noteDescription = description.trim()

        if (noteTitle.isEmpty()) {
            titleError = "Title is required"
            return
        }

        if (uid.isNullOrEmpty()) {
            Toast.makeText(this, "User id missing", Toast.LENGTH_SHORT).show()
            return
        }

        isSaving = true

        if (editingNoteId == null) {
            val noteId = notesRef.push().key
            if (noteId == null) {
                isSaving = false
                Toast.makeText(this, "Failed to create note id", Toast.LENGTH_SHORT).show()
                return
            }

            val createdTime = System.currentTimeMillis()
            val note = Note(noteId, noteTitle, noteDescription, createdTime, uid, false)

            notesRef.child(noteId).setValue(note)
                .addOnSuccessListener {
                    isSaving = false
                    Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    isSaving = false
                    Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            val note = Note(
                editingNoteId,
                noteTitle,
                noteDescription,
                if (editingCreatedTime == 0L) System.currentTimeMillis() else editingCreatedTime,
                uid,
                false
            )

            notesRef.child(editingNoteId!!).setValue(note)
                .addOnSuccessListener {
                    isSaving = false
                    Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    isSaving = false
                    Toast.makeText(this, "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
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

    override fun onResume() {
        super.onResume()
        loadProfilePhoto()
    }
}