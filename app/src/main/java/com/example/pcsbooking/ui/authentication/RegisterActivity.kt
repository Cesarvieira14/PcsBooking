package com.example.pcsbooking.ui.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.pcsbooking.Model.User
import com.example.pcsbooking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        findViewById<Button>(R.id.ConfirmBtn).setOnClickListener {
            handleConfirmButtonClick()
        }
    }

    private fun handleConfirmButtonClick() {
        val email = findViewById<EditText>(R.id.editTxtEmail).text.toString()
        val fullName = findViewById<EditText>(R.id.editTxtFullname).text.toString()
        val phoneNo = findViewById<EditText>(R.id.editTxtPhoneNumber).text.toString()
        val password = findViewById<EditText>(R.id.editTxtPassword).text.toString()
        val confirmPassword = findViewById<EditText>(R.id.editTxtConfirmPassword).text.toString()

        if (email.isBlank() || fullName.isBlank() || phoneNo.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            showToast("Please fill all fields")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Invalid email address")
            return
        }

        if (!phoneNo.matches("^\\d{6,15}$".toRegex())) {
            showToast("Invalid phone number")
            return
        }

        if (password != confirmPassword) {
            showToast("Passwords do not match")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileUpdateTask ->
                            if (profileUpdateTask.isSuccessful) {
                                val database = FirebaseDatabase.getInstance().reference
                                val userData = User(email, fullName, phoneNo, false) // Normal user
                                user.uid?.let { userId ->
                                    database.child("users").child(userId).setValue(userData)
                                        .addOnSuccessListener {
                                            showToast("Registration successful")
                                            finish()
                                        }
                                        .addOnFailureListener { exception ->
                                            showToast("Failed to save user data: ${exception.message}")
                                        }
                                }
                            } else {
                                showToast("Failed to update profile")
                            }
                        }
                } else {
                    showToast("Registration failed: ${task.exception?.message}")
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
