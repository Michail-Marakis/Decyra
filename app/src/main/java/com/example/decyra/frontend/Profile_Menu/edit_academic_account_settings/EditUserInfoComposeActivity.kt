package com.example.decyra.frontend.Profile_Menu.edit_academic_account_settings

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.decyra.backend.domain.UserInfo
import com.example.decyra.extras.InternetConnection
import com.google.firebase.database.*

class EditUserInfoComposeActivity : AppCompatActivity() {

    private lateinit var userInfoRef: DatabaseReference
    private lateinit var universitiesRef: DatabaseReference
    private val inter = InternetConnection()

    private var userId: String? = null

    private val universityList = mutableStateListOf<String>()

    var selectedUniversity by mutableStateOf("")
    var selectedYear by mutableStateOf("1")

    var academicLevel by mutableStateOf("")
    var languages by mutableStateOf("")
    var gpa by mutableStateOf("")
    var field by mutableStateOf("")
    var budget by mutableStateOf("")

    var selectedAdvisorType by mutableStateOf<String?>(null)
    var isSaving by mutableStateOf(false)
    var isLoading by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!inter.isConnected(this)) {
            inter.showCustomDialog(this)
        }

        userId = intent.getStringExtra("userId")

        val firebaseDb = FirebaseDatabase.getInstance(
            "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
        )
        userInfoRef = firebaseDb.getReference("user_info")
        universitiesRef = firebaseDb.getReference("universities")

        loadUniversitiesIntoState()

        setContent {
            EditUserInfoScreen(
                universities = universityList,
                selectedUniversity = selectedUniversity,
                selectedYear = selectedYear,
                academicLevel = academicLevel,
                languages = languages,
                gpa = gpa,
                field = field,
                budget = budget,
                selectedAdvisorType = selectedAdvisorType,
                isSaving = isSaving,
                isLoading = isLoading,
                onBackClick = { finish() },
                onUniversitySelected = { selectedUniversity = it },
                onYearSelected = { selectedYear = it },
                onAcademicLevelChange = { academicLevel = it },
                onLanguagesChange = { languages = it },
                onGpaChange = { gpa = it },
                onFieldChange = { field = it },
                onBudgetChange = { budget = it },
                onAdvisorSelected = { selectedAdvisorType = it },
                onSaveClick = { saveUserInfo() }
            )
        }
    }

    private fun loadUniversitiesIntoState() {
        universitiesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                universityList.clear()

                for (child in snapshot.children) {
                    val name = child.child("name").getValue(String::class.java)
                    val country = child.child("country").getValue(String::class.java)

                    if (name != null && country == "Ελλάδα") {
                        universityList.add(name)
                    }
                }

                prefillUserInfo()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@EditUserInfoComposeActivity,
                    "Failed to load universities",
                    Toast.LENGTH_SHORT
                ).show()

                prefillUserInfo()
            }
        })
    }

    private fun prefillUserInfo() {
        val uid = userId
        if (uid.isNullOrEmpty()) {
            isLoading = false
            return
        }

        userInfoRef.child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val info = snapshot.getValue(UserInfo::class.java)
                        if (info != null) {
                            val uni = info.university
                            if (!uni.isNullOrEmpty() && universityList.contains(uni)) {
                                selectedUniversity = uni
                            } else if (universityList.isNotEmpty() && selectedUniversity.isBlank()) {
                                selectedUniversity = universityList.first()
                            }

                            academicLevel = info.academicLevel ?: ""
                            languages = info.languages ?: ""
                            gpa = info.gpa?.toString() ?: ""
                            field = info.field ?: ""
                            budget = info.budgetPerYear?.toString() ?: ""
                            selectedYear = info.yearOfStudies?.toString() ?: "1"
                            selectedAdvisorType = info.advisorType
                        }
                    } else if (universityList.isNotEmpty() && selectedUniversity.isBlank()) {
                        selectedUniversity = universityList.first()
                    }

                    isLoading = false
                }

                override fun onCancelled(error: DatabaseError) {
                    isLoading = false
                }
            })
    }

    private fun saveUserInfo() {
        val uid = userId
        if (uid.isNullOrEmpty()) {
            Toast.makeText(this, "User id missing", Toast.LENGTH_SHORT).show()
            return
        }

        val university = selectedUniversity
        val academic = academicLevel.trim()
        val langs = languages.trim()
        val gpaStr = gpa.trim()
        val fieldStr = field.trim()
        val budgetStr = budget.trim()
        val yearStr = selectedYear
        val advisorType = selectedAdvisorType

        if (university.isBlank() || academic.isEmpty() || langs.isEmpty() ||
            gpaStr.isEmpty() || fieldStr.isEmpty() || budgetStr.isEmpty() || yearStr.isBlank()
        ) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val budgetValue: Double
        val yearValue: Int

        try {
            budgetValue = budgetStr.toDouble()
            yearValue = yearStr.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Invalid numeric values", Toast.LENGTH_SHORT).show()
            return
        }

        if (advisorType == null) {
            Toast.makeText(this, "Please choose an advisor", Toast.LENGTH_SHORT).show()
            return
        }

        val advisorImage = when (advisorType) {
            "male" -> "male.png"
            "female" -> "female.png"
            else -> "robot.png"
        }

        val info = UserInfo(
            uid,
            university,
            academic,
            langs,
            gpaStr,
            fieldStr,
            budgetValue,
            yearValue,
            advisorType,
            advisorImage
        )

        isSaving = true

        userInfoRef.child(uid)
            .setValue(info)
            .addOnSuccessListener {
                isSaving = false
                Toast.makeText(this, "Academic info updated", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                isSaving = false
                Toast.makeText(this, "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}