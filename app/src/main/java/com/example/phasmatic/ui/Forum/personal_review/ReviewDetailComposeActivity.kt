package com.example.phasmatic.ui.Forum.personal_review

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
import com.example.phasmatic.data.model.ReviewComment
import com.example.phasmatic.data.model.User
import com.example.phasmatic.data.model.UserInfo
import com.example.phasmatic.extras.InternetConnection
import com.example.phasmatic.extras.ProfileImageManager
import com.example.phasmatic.ui.Chat.users_to_chat.UsersActivity
import com.example.phasmatic.ui.Profile_Menu.account_settings.AccountActivity
import com.example.phasmatic.ui.Profile_Menu.public_profile.PublicProfileActivity
import com.example.phasmatic.ui.conference.general_conference.GeneralConferenceActivity
import com.example.phasmatic.ui.login.LoginActivity
import com.example.phasmatic.ui.notes.Notes.NotesActivity
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReviewDetailComposeActivity : AppCompatActivity() {

    private val inter = InternetConnection()

    private lateinit var reviewCommentsRef: DatabaseReference
    private lateinit var usersRef: DatabaseReference
    private lateinit var userInfoRef: DatabaseReference
    private lateinit var forumRef: DatabaseReference
    private lateinit var reviewViewsRef: DatabaseReference

    private var userId: String? = null
    private var userFullName: String? = null
    private var userEmail: String? = null
    private var userPhone: String? = null
    private var reviewId: String? = null

    private val comments = mutableStateListOf<ReviewComment>()
    private val userNameMap = mutableMapOf<String, String>()
    private val userAcademicMap = mutableMapOf<String, String>()

    var profileImageUrl by mutableStateOf<String?>(null)
    var profileBitmap by mutableStateOf<Bitmap?>(null)

    var commentText by mutableStateOf("")
    var isSendingComment by mutableStateOf(false)

    private var commentsListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!inter.isConnected(this)) {
            inter.showCustomDialog(this)
        }

        val incomingIntent = intent

        val type = incomingIntent.getStringExtra("type")
        val university = incomingIntent.getStringExtra("university")
        val country = incomingIntent.getStringExtra("country")
        val userName = incomingIntent.getStringExtra("userName")
        val rating = incomingIntent.getFloatExtra("rating", 0f)
        val text = incomingIntent.getStringExtra("text")
        val likes = incomingIntent.getIntExtra("likes", 0)

        userId = incomingIntent.getStringExtra("userId")
        userFullName = incomingIntent.getStringExtra("userFullName")
        userEmail = incomingIntent.getStringExtra("userEmail")
        userPhone = incomingIntent.getStringExtra("userPhone")
        reviewId = incomingIntent.getStringExtra("reviewId")

        val db = FirebaseDatabase.getInstance(
            "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
        )

        reviewCommentsRef = db.getReference("review_comments")
        usersRef = db.getReference("users")
        userInfoRef = db.getReference("user_info")
        forumRef = db.getReference("forum_reviews")
        reviewViewsRef = db.getReference("review_views")

        loadProfilePhoto()
        loadAllUsersAndInfoThenComments()

        if (!reviewId.isNullOrBlank() && !userId.isNullOrBlank()) {
            registerView()
        }

        setContent {
            ReviewDetailScreen(
                type = type,
                university = university,
                country = country,
                userName = userName,
                rating = rating,
                text = text,
                likes = likes,
                comments = comments,
                userNameMap = userNameMap,
                userAcademicMap = userAcademicMap,
                profileImageUrl = profileImageUrl,
                profileBitmap = profileBitmap,
                commentText = commentText,
                isSendingComment = isSendingComment,
                onCommentTextChange = { commentText = it },
                onBackClick = { finish() },
                onSendCommentClick = {
                    if (commentText.isNotBlank()) {
                        postComment(commentText.trim())
                    }
                },
                onCommentUserClick = { comment ->
                    startActivity(
                        Intent(this, PublicProfileActivity::class.java).apply {
                            putExtra("currentUserId", userId)
                            putExtra("userId", comment.user_id)
                            putExtra("userFullName", userFullName)
                            putExtra("userEmail", userEmail)
                            putExtra("userPhone", userPhone)
                        }
                    )
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

    private fun registerView() {
        val rid = reviewId ?: return
        val uid = userId ?: return

        val viewKey = "${rid}_${uid}"
        val viewRef = reviewViewsRef.child(viewKey)

        viewRef.get().addOnSuccessListener { snap ->
            if (snap.exists()) return@addOnSuccessListener

            val viewedAt = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
            ).format(Date())

            viewRef.child("review_id").setValue(rid)
            viewRef.child("user_id").setValue(uid)
            viewRef.child("viewed_at").setValue(viewedAt)

            forumRef.child(rid).child("views")
                .get()
                .addOnSuccessListener { countSnap ->
                    val current = countSnap.getValue(Long::class.java)
                    val newVal = (current ?: 0) + 1
                    forumRef.child(rid).child("views").setValue(newVal)
                }
        }
    }

    private fun loadAllUsersAndInfoThenComments() {
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(userSnap: DataSnapshot) {
                userNameMap.clear()
                for (child in userSnap.children) {
                    val u = child.getValue(User::class.java)
                    if (u != null) {
                        userNameMap[child.key ?: ""] = u.fullName ?: "User"
                    }
                }

                userInfoRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(infoSnap: DataSnapshot) {
                        userAcademicMap.clear()
                        for (child in infoSnap.children) {
                            val ui = child.getValue(UserInfo::class.java)
                            if (ui != null) {
                                val uid = ui.userId ?: child.key ?: ""

                                val uni = ui.university ?: "-"
                                val level = ui.academicLevel ?: ""
                                val field = ui.field ?: ""

                                val academic = buildString {
                                    if (level.isNotBlank()) append("$level · ")
                                    if (field.isNotBlank()) append("$field · ")
                                    append(uni)
                                }

                                userAcademicMap[uid] = academic
                            }
                        }

                        loadComments()
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun loadComments() {
        val rid = reviewId
        if (rid.isNullOrBlank()) return

        commentsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                comments.clear()
                for (child in snapshot.children) {
                    val c = child.getValue(ReviewComment::class.java)
                    if (c != null) {
                        c.id = child.key
                        comments.add(c)
                    }
                }

                val commentsCount = comments.size
                forumRef.child(rid).child("comments").setValue(commentsCount)
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        reviewCommentsRef
            .orderByChild("review_id")
            .equalTo(rid)
            .addValueEventListener(commentsListener as ValueEventListener)
    }

    private fun postComment(text: String) {
        val rid = reviewId
        if (rid.isNullOrBlank()) return

        isSendingComment = true

        val key = reviewCommentsRef.push().key
        if (key == null) {
            isSendingComment = false
            Toast.makeText(this, "Error creating comment", Toast.LENGTH_SHORT).show()
            return
        }

        val createdAt = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        ).format(Date())

        val c = ReviewComment()
        c.id = key
        c.review_id = rid
        c.user_id = userId
        c.user_name = userFullName
        c.academic_profile = userAcademicMap[userId]
        c.comment_text = text
        c.created_at = createdAt

        reviewCommentsRef.child(key).setValue(c)
            .addOnCompleteListener { task ->
                isSendingComment = false
                if (!task.isSuccessful) {
                    Toast.makeText(this, "Failed to add comment", Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }

                commentText = ""
                Toast.makeText(this, "Comment added", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        commentsListener?.let {
            reviewCommentsRef.removeEventListener(it)
        }
    }
}
