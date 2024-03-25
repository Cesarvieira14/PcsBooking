package com.example.pcsbooking.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.pcsbooking.R
import com.example.pcsbooking.ui.home.HomeActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        findViewById<Button>(R.id.LoginBtn).setOnClickListener{
            handleLoginButtonClick()
        }

        findViewById<Button>(R.id.RegisterBtn).setOnClickListener{
            handleRegisterButtonClick()
        }
    }

    private fun handleLoginButtonClick() {
        val email = findViewById<EditText>(R.id.LoginEmail).text.toString()
        val password = findViewById<EditText>(R.id.LoginPassword).text.toString()

        if (email.isBlank() || password.isBlank()) {
            showToast("Please enter email and password")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    showToast("Login successful")
                    val start = Intent (this, HomeActivity::class.java)
                    startActivity(start)
                    finish() // Close the com.example.pcsbooking.ui.authentication.LoginActivity
                } else {
                    // If sign in fails, display a message to the user.
                    showToast("Login failed: ${task.exception?.message}")
                }
            }
    }

    private fun handleRegisterButtonClick() {
        val start = Intent (this, RegisterActivity::class.java)
        startActivity(start)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

