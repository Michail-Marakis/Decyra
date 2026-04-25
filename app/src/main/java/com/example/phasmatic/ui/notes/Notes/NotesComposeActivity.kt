package com.example.phasmatic.ui.notes.Notes

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.phasmatic.data.model.Note
import com.example.phasmatic.extras.InternetConnection
import com.example.phasmatic.extras.ProfileImageManager
import com.example.phasmatic.ui.modeSelection.ModeSelectionActivity
import com.example.phasmatic.ui.notes.NotesScreen
import com.example.phasmatic.ui.notes.add_note.AddNoteActivity
import com.google.firebase.database.*

class NotesComposeActivity : AppCompatActivity() {

    private val inter = InternetConnection()

    private lateinit var notesRef: DatabaseReference
    private lateinit var usersRef: DatabaseReference

    private var userId: String? = null
    private var userFullName: String? = null
    private var userEmail: String? = null
    private var userPhone: String? = null

    private val notesList = mutableStateListOf<Note>()

    var profileImageUrl by mutableStateOf<String?>(null)
    var profileBitmap by mutableStateOf<Bitmap?>(null)

    private var notesListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!inter.isConnected(this)) {
            inter.showCustomDialog(this)
        }

        val incomingIntent = intent
        userId = incomingIntent.getStringExtra("userId")
        userFullName = incomingIntent.getStringExtra("userFullName")
        userEmail = incomingIntent.getStringExtra("userEmail")
        userPhone = incomingIntent.getStringExtra("userPhone")

        val firebaseDb = FirebaseDatabase.getInstance(
            "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
        )

        notesRef = firebaseDb.getReference("notes")
        usersRef = firebaseDb.getReference("users")

        loadProfilePhoto()
        loadNotes()

        setContent {
            NotesScreen(
                notes = notesList,
                profileImageUrl = profileImageUrl,
                profileBitmap = profileBitmap,
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
                onAddNoteClick = {
                    startActivity(
                        Intent(this, AddNoteActivity::class.java).apply {
                            putExtra("userId", userId)
                            putExtra("userFullName", userFullName)
                            putExtra("userEmail", userEmail)
                            putExtra("userPhone", userPhone)
                        }
                    )
                },
                onNoteClick = { note ->
                    startActivity(
                        Intent(this, AddNoteActivity::class.java).apply {
                            putExtra("userId", userId)
                            putExtra("userFullName", userFullName)
                            putExtra("userEmail", userEmail)
                            putExtra("userPhone", userPhone)
                            putExtra("noteId", note.id)
                            putExtra("title", note.title)
                            putExtra("description", note.description)
                            putExtra("createdTime", note.createdTime)
                            putExtra("pinned", note.isPinned)
                        }
                    )
                },
                onDeleteNote = { note -> deleteNote(note) },
                onPinNote = { note -> pinNote(note) },
                onUnpinNote = { note -> unpinNote(note) }
            )
        }
    }

    private fun loadNotes() {
        val uid = userId ?: return

        notesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val loaded = mutableListOf<Note>()

                for (child in snapshot.children) {
                    val note = child.getValue(Note::class.java)
                    if (note != null) {
                        loaded.add(note)
                    }
                }

                loaded.sortWith { n1, n2 ->
                    when {
                        n1.isPinned && !n2.isPinned -> -1
                        !n1.isPinned && n2.isPinned -> 1
                        else -> java.lang.Long.compare(n2.createdTime, n1.createdTime)
                    }
                }

                notesList.clear()
                notesList.addAll(loaded)
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        notesRef.orderByChild("user_id").equalTo(uid)
            .addValueEventListener(notesListener as ValueEventListener)
    }

    private fun deleteNote(note: Note) {
        val noteId = note.id ?: return

        notesRef.child(noteId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Delete failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun pinNote(note: Note) {
        val noteId = note.id ?: return

        notesRef.child(noteId).child("pinned").setValue(true)
            .addOnSuccessListener {
                Toast.makeText(this, "Note pinned", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Pin failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun unpinNote(note: Note) {
        val noteId = note.id ?: return

        notesRef.child(noteId).child("pinned").setValue(false)
            .addOnSuccessListener {
                Toast.makeText(this, "Note unpinned", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Unpin failed: ${e.message}", Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        super.onDestroy()
        notesListener?.let { listener ->
            userId?.let { uid ->
                notesRef.orderByChild("user_id").equalTo(uid).removeEventListener(listener)
            }
        }
    }
}