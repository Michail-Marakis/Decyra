package com.example.decyra.frontend.forget

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.decyra.extras.InternetConnection
import com.example.decyra.frontend.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class ForgetComposeActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private val inter = InternetConnection()

    var email by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var infoMessage by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!inter.isConnected(this)) {
            inter.showCustomDialog(this)
        }

        mAuth = FirebaseAuth.getInstance()

        setContent {
            ForgetPasswordScreen(
                email = email,
                isLoading = isLoading,
                infoMessage = infoMessage,
                onEmailChange = { email = it },
                onBackClick = {
                    if (!inter.isConnected(this)) {
                        inter.showCustomDialog(this)
                    }
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                },
                onSendClick = {
                    sendResetEmail()
                }
            )
        }
    }

    private fun sendResetEmail() {
        val userEmail = email.trim()

        if (userEmail.isEmpty()) {
            infoMessage = "Enter your email"
            return
        }

        isLoading = true
        infoMessage = null

        mAuth.sendPasswordResetEmail(userEmail)
            .addOnCompleteListener { task ->
                isLoading = false

                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Password reset email sent. Check your inbox.",
                        Toast.LENGTH_LONG
                    ).show()

                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    val msg = task.exception?.message ?: "Error sending reset email"
                    infoMessage = msg
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
            }
    }
}