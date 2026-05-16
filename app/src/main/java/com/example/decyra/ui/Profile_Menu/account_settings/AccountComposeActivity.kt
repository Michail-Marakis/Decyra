package com.example.decyra.ui.Profile_Menu.account_settings

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.decyra.extras.InternetConnection
import com.example.decyra.extras.ProfileImageManager
import com.example.decyra.ui.Profile_Menu.edit_account_settings.EditProfileActivity
import com.example.decyra.ui.Profile_Menu.edit_academic_account_settings.EditUserInfoActivity
import com.example.decyra.ui.modeSelection.ModeSelectionActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException

class AccountComposeActivity : AppCompatActivity() {

    companion object {
        private const val SUPABASE_URL = "https://sbzxqcwvbbgbpykyvmfa.supabase.co"
        private const val SUPABASE_KEY = "YOUR_SUPABASE_KEY"
        private const val SUPABASE_BUCKET = "avatars"
    }

    private lateinit var usersRef: DatabaseReference
    private lateinit var userInfoRef: DatabaseReference

    private val httpClient = OkHttpClient()
    private val inter = InternetConnection()

    private var userId: String? = null
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

    var isUploading by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!inter.isConnected(this)) {
            inter.showCustomDialog(this)
        }

        userId = intent.getStringExtra("userId")
        userFullName = intent.getStringExtra("userFullName")
        userEmail = intent.getStringExtra("userEmail")
        userPhone = intent.getStringExtra("userPhone")

        val firebaseDb = FirebaseDatabase.getInstance(
            "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
        )
        usersRef = firebaseDb.getReference("users")
        userInfoRef = firebaseDb.getReference("user_info")

        loadUser()
        loadUserInfo()

        setContent {
            val galleryLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                uri?.let {
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                        profileBitmap = bitmap
                        ProfileImageManager.saveBitmap(this, userId, bitmap)
                        uploadImageToSupabase(bitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            val cameraLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.TakePicturePreview()
            ) { bitmap: Bitmap? ->
                bitmap?.let {
                    profileBitmap = it
                    ProfileImageManager.saveBitmap(this, userId, it)
                    uploadImageToSupabase(it)
                }
            }

            AccountScreen(
                userFullName = txtFullName,
                userEmail = txtEmail,
                userPhone = txtPhone,
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
                isUploading = isUploading,
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
                onEditProfileClick = {
                    startActivity(
                        Intent(this, EditProfileActivity::class.java).apply {
                            putExtra("userId", userId)
                            putExtra("userFullName", userFullName)
                            putExtra("userEmail", userEmail)
                            putExtra("userPhone", userPhone)
                        }
                    )
                },
                onEditAcademicClick = {
                    startActivity(
                        Intent(this, EditUserInfoActivity::class.java).apply {
                            putExtra("userId", userId)
                        }
                    )
                },
                onChangePhotoClick = {
                    showImageOptions(
                        onTakePhoto = { cameraLauncher.launch(null) },
                        onChooseGallery = { galleryLauncher.launch("image/*") }
                    )
                }
            )
        }
    }

    private fun showImageOptions(
        onTakePhoto: () -> Unit,
        onChooseGallery: () -> Unit
    ) {
        val options = arrayOf("Take photo", "Choose from gallery")

        AlertDialog.Builder(this)
            .setTitle("Profile Photo")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> onTakePhoto()
                    1 -> onChooseGallery()
                }
            }
            .show()
    }

    private fun uploadImageToSupabase(bitmap: Bitmap) {
        val uid = userId ?: return

        isUploading = true

        Thread {
            try {
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos)
                val imageBytes = bos.toByteArray()

                val path = "$uid.jpg"
                val url = "$SUPABASE_URL/storage/v1/object/$SUPABASE_BUCKET/$path"

                val body = imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())

                val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("apikey", SUPABASE_KEY)
                    .addHeader("Authorization", "Bearer $SUPABASE_KEY")
                    .addHeader("Content-Type", "image/jpeg")
                    .addHeader("x-upsert", "true")
                    .build()

                val response = httpClient.newCall(request).execute()

                if (!response.isSuccessful) {
                    runOnUiThread {
                        isUploading = false
                        Toast.makeText(
                            this,
                            "Upload failed: ${response.code}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    return@Thread
                }

                val publicUrl = "$SUPABASE_URL/storage/v1/object/public/$SUPABASE_BUCKET/$path"
                saveProfileImageUrlToFirebase(publicUrl)

                runOnUiThread {
                    isUploading = false
                    profileImageUrl = "$publicUrl?v=${System.currentTimeMillis()}"
                    Toast.makeText(this, "Profile photo updated", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    isUploading = false
                    Toast.makeText(
                        this,
                        "Upload error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }.start()
    }

    private fun saveProfileImageUrlToFirebase(url: String) {
        val uid = userId ?: return
        usersRef.child(uid).child("profileImageUrl").setValue(url)
    }

    private fun loadUser() {
        val uid = userId ?: return

        usersRef.child(uid).get().addOnSuccessListener { snapshot ->
            if (!snapshot.exists()) return@addOnSuccessListener

            userFullName = snapshot.child("fullName").getValue(String::class.java)
            userEmail = snapshot.child("email").getValue(String::class.java)
            userPhone = snapshot.child("phoneNumber").getValue(String::class.java)
            val firebaseUrl = snapshot.child("profileImageUrl").getValue(String::class.java)

            txtFullName = userFullName ?: "-"
            txtEmail = userEmail ?: "-"
            txtPhone = userPhone ?: "-"

            if (!firebaseUrl.isNullOrEmpty()) {
                profileImageUrl = "$firebaseUrl?v=${System.currentTimeMillis()}"
                profileBitmap = null
            } else {
                val cached = ProfileImageManager.loadBitmap(this, uid)
                if (cached != null) {
                    profileBitmap = cached
                    profileImageUrl = null
                } else {
                    profileBitmap = null
                    profileImageUrl = null
                }
            }
        }
    }

    private fun loadUserInfo() {
        val uid = userId ?: return

        userInfoRef.child(uid).get().addOnSuccessListener { snapshot ->
            if (!snapshot.exists()) return@addOnSuccessListener

            txtUniversity = snapshot.child("university").getValue(String::class.java) ?: "-"
            txtAcademicLevel = snapshot.child("academicLevel").getValue(String::class.java) ?: "-"
            txtField = snapshot.child("field").getValue(String::class.java) ?: "-"
            txtLanguages = snapshot.child("languages").getValue(String::class.java) ?: "-"

            val gpaObj = snapshot.child("gpa").value
            txtGpa = gpaObj?.toString() ?: "-"

            val budgetObj = snapshot.child("budgetPerYear").value
            txtBudget = budgetObj?.toString() ?: "-"

            val yearObj = snapshot.child("yearOfStudies").value
            txtYearOfStudies = yearObj?.toString() ?: "-"

            txtAdvisorType = snapshot.child("advisorType").getValue(String::class.java) ?: "-"
        }
    }

    override fun onResume() {
        super.onResume()
        loadUser()
        loadUserInfo()
    }
}