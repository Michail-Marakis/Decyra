package com.example.phasmatic.ui.Forum.forum

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.phasmatic.data.model.ForumReview
import com.example.phasmatic.extras.InternetConnection
import com.example.phasmatic.extras.ProfileImageManager
import com.example.phasmatic.ui.Forum.new_review.NewReviewActivity
import com.example.phasmatic.ui.Forum.personal_review.ReviewDetailActivity
import com.example.phasmatic.ui.modeSelection.ModeSelectionActivity
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val likedReviewIds = mutableStateMapOf<String, Boolean>()

class ForumComposeActivity : AppCompatActivity() {

    private val inter = InternetConnection()

    private lateinit var forumRef: DatabaseReference
    private lateinit var countriesRef: DatabaseReference
    private lateinit var universitiesRef: DatabaseReference
    private lateinit var usersRef: DatabaseReference
    private lateinit var reviewLikesRef: DatabaseReference

    private var userId: String? = null
    private var userFullName: String? = null
    private var userEmail: String? = null
    private var userPhone: String? = null

    private val allReviews = mutableStateListOf<ForumReview>()
    private val filteredReviews = mutableStateListOf<ForumReview>()

    private val countryList = mutableStateListOf<String>()
    private val uniList = mutableStateListOf<String>()

    var selectedType by mutableStateOf<String?>(null)
    var selectedCountry by mutableStateOf<String?>(null)
    var selectedUniversity by mutableStateOf<String?>(null)

    var profileImageUrl by mutableStateOf<String?>(null)
    var profileBitmap by mutableStateOf<Bitmap?>(null)

    private var reviewsListener: ValueEventListener? = null

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

        val db = FirebaseDatabase.getInstance(
            "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
        )

        forumRef = db.getReference("forum_reviews")
        countriesRef = db.getReference("countries")
        universitiesRef = db.getReference("universities")
        usersRef = db.getReference("users")
        reviewLikesRef = db.getReference("review_likes")

        loadProfilePhoto()
        loadCountries()
        loadReviews()

        setContent {
            ForumScreen(
                reviews = filteredReviews,
                countries = countryList,
                universities = uniList,
                selectedType = selectedType,
                selectedCountry = selectedCountry,
                selectedUniversity = selectedUniversity,
                profileImageUrl = profileImageUrl,
                profileBitmap = profileBitmap,
                currentUserId = userId,
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
                onTypeSelected = { type ->
                    selectedType = if (selectedType == type) null else type
                    applyFilters()
                },
                onCountrySelected = { country ->
                    selectedCountry = country
                    selectedUniversity = null
                    uniList.clear()
                    if (!country.isNullOrBlank()) {
                        loadUniversitiesForCountry(country)
                    }
                    applyFilters()
                },
                onUniversitySelected = { university ->
                    selectedUniversity = university
                    applyFilters()
                },
                onAddReviewClick = {
                    startActivity(
                        Intent(this, NewReviewActivity::class.java).apply {
                            putExtra("userId", userId)
                            putExtra("userFullName", userFullName)
                        }
                    )
                },
                onReviewClick = { review ->
                    startActivity(
                        Intent(this, ReviewDetailActivity::class.java).apply {
                            putExtra("userId", userId)
                            putExtra("userFullName", userFullName)
                            putExtra("userEmail", userEmail)
                            putExtra("userPhone", userPhone)
                            putExtra("modeType", "master")
                            putExtra("type", review.type)
                            putExtra("university", review.university)
                            putExtra("country", review.country)
                            putExtra("userName", review.user_name)
                            putExtra("rating", review.rating)
                            putExtra("text", review.text)
                            putExtra("likes", review.likes)
                            putExtra("reviewId", review.id)
                            putExtra("Views", review.views)
                        }
                    )
                },
                onLikeToggle = { review, currentlyLiked ->
                    toggleLike(review, currentlyLiked)
                },
                isReviewLiked = { review ->
                    val reviewId = review.id
                    reviewId != null && likedReviewIds[reviewId] == true
                }
            )
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

    private fun loadCountries() {
        countriesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                countryList.clear()
                for (child in snapshot.children) {
                    val name = child.child("name").getValue(String::class.java)
                    if (!name.isNullOrBlank()) {
                        countryList.add(name)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun loadUniversitiesForCountry(countryName: String) {
        universitiesRef
            .orderByChild("country")
            .equalTo(countryName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    uniList.clear()

                    for (child in snapshot.children) {
                        val uniCountry = child.child("country").getValue(String::class.java)
                        val uniName = child.child("name").getValue(String::class.java)

                        if (!uniCountry.isNullOrBlank() &&
                            uniCountry.equals(countryName, ignoreCase = true) &&
                            !uniName.isNullOrBlank()
                        ) {
                            uniList.add(uniName)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun loadReviews() {
        reviewsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allReviews.clear()

                for (child in snapshot.children) {
                    val r = ForumReview()

                    r.id = child.key
                    r.user_id = child.child("user_id").getValue(String::class.java)
                    r.user_name = child.child("user_name").getValue(String::class.java)
                    r.type = child.child("type").getValue(String::class.java)
                    r.university = child.child("university").getValue(String::class.java)
                    r.country = child.child("country").getValue(String::class.java)
                    r.text = child.child("text").getValue(String::class.java)

                    val ratingDouble = child.child("rating").getValue(Double::class.java)
                    r.rating = ratingDouble?.toFloat() ?: 0f

                    val likesLong = child.child("likes").getValue(Long::class.java)
                    r.likes = likesLong?.toInt() ?: 0

                    val commentsLong = child.child("comments").getValue(Long::class.java)
                    r.comments = commentsLong?.toInt() ?: 0

                    val viewsLong = child.child("views").getValue(Long::class.java)
                    r.views = viewsLong?.toInt() ?: 0

                    r.timestamp = child.child("created_at").getValue(String::class.java)

                    allReviews.add(r)
                }

                allReviews.sortWith { a, b ->
                    val diff = b.views.compareTo(a.views)
                    if (diff != 0) diff
                    else {
                        if (a.timestamp != null && b.timestamp != null) {
                            b.timestamp!!.compareTo(a.timestamp!!)
                        } else 0
                    }
                }

                loadLikesStateAndApply()
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        forumRef.orderByChild("views").addValueEventListener(reviewsListener as ValueEventListener)
    }

    private fun loadLikesStateAndApply() {
        val uid = userId
        if (uid.isNullOrBlank()) {
            applyFilters()
            return
        }

        if (allReviews.isEmpty()) {
            applyFilters()
            return
        }

        var remaining = allReviews.size

        allReviews.forEach { review ->
            val reviewId = review.id
            if (reviewId.isNullOrBlank()) {
                remaining--
                if (remaining == 0) applyFilters()
            } else {
                val likeKey = "${reviewId}_${uid}"
                reviewLikesRef.child(likeKey).get()
                    .addOnSuccessListener { snap ->
                        likedReviewIds[reviewId] = snap.exists()
                        remaining--
                        if (remaining == 0) applyFilters()
                    }
                    .addOnFailureListener {
                        likedReviewIds[reviewId] = false
                        remaining--
                        if (remaining == 0) applyFilters()
                    }
            }
        }
    }

    private fun toggleLike(review: ForumReview, currentlyLiked: Boolean) {
        val uid = userId ?: return
        val reviewId = review.id ?: return
        val likeKey = "${reviewId}_${uid}"
        val likeRef = reviewLikesRef.child(likeKey)
        val reviewLikesCountRef = forumRef.child(reviewId).child("likes")

        if (currentlyLiked) {
            review.likes = maxOf(0, review.likes - 1)
            likedReviewIds[reviewId] = false
            applyFilters()

            reviewLikesCountRef.setValue(review.likes)
            likeRef.removeValue().addOnFailureListener { e ->
                Toast.makeText(this, "Unlike failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            review.likes += 1
            likedReviewIds[reviewId] = false
            applyFilters()

            reviewLikesCountRef.setValue(review.likes)

            val likedAt = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
            ).format(Date())

            likeRef.child("review_id").setValue(reviewId)
            likeRef.child("user_id").setValue(uid)
            likeRef.child("liked_at").setValue(likedAt)
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Like failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun applyFilters() {
        filteredReviews.clear()

        for (r in allReviews) {
            var ok = true

            if (selectedType != null && selectedType != r.type) ok = false
            if (selectedCountry != null && selectedCountry != r.country) ok = false
            if (selectedUniversity != null && selectedUniversity != r.university) ok = false

            if (ok) filteredReviews.add(r)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        reviewsListener?.let {
            forumRef.removeEventListener(it)
        }
    }
}
