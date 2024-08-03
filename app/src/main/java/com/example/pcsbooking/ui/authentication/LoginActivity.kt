package com.example.pcsbooking.ui.authentication

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.pcsbooking.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.example.pcsbooking.R
import com.example.pcsbooking.ui.admin.AdminActivity
import com.example.pcsbooking.ui.home.HomeActivity

class LoginActivity : AppCompatActivity() {

    // Firebase authentication and database references
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Set the layout

        // Initialize FirebaseAuth and DatabaseReference
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference

        // Set up click listeners for the login, register, and forgot password buttons
        findViewById<Button>(R.id.LoginBtn).setOnClickListener {
            handleLoginButtonClick()
        }

        findViewById<Button>(R.id.RegisterBtn).setOnClickListener {
            handleRegisterButtonClick()
        }

        findViewById<TextView>(R.id.forgotPassword).setOnClickListener {
            handleForgotPassClick()
        }
    }

    // Handles login button click
    private fun handleLoginButtonClick() {
        val email = findViewById<EditText>(R.id.LoginEmail).text.toString()
        val password = findViewById<EditText>(R.id.LoginPassword).text.toString()
        // Check if email or password fields are empty
        if (email.isBlank() || password.isBlank()) {
            showToast("Please enter email and password")
            return
        }
        // Attempt to sign in with provided email and password
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        // Retrieve user data from the database
                        database.child("users").child(user.uid).get()
                            .addOnSuccessListener {
                                val userData = it.getValue(User::class.java)
                                if (userData != null) {
                                    // Navigate to the appropriate activity based on user role
                                    if (userData.admin) {
                                        val start = Intent(this, AdminActivity::class.java)
                                        startActivity(start)
                                    } else {
                                        val start = Intent(this, HomeActivity::class.java)
                                        startActivity(start)
                                    }
                                    finish() // Close the LoginActivity
                                } else {
                                    showToast("User data not found")
                                }
                            }
                            .addOnFailureListener { exception ->
                                showToast("Failed to retrieve user data: ${exception.message}")
                            }
                    }
                } else {
                    showToast("Login failed: ${task.exception?.message}")
                }
            }
    }

    // Handles register button click
    private fun handleRegisterButtonClick() {
        val start = Intent(this, RegisterActivity::class.java)
        startActivity(start) // Navigate to the RegisterActivity
    }

    // Handles forgot password link click
    private fun handleForgotPassClick() {
        // Create and configure an AlertDialog for password reset
        val builder = AlertDialog.Builder(this@LoginActivity)
        val dialogView = layoutInflater.inflate(R.layout.popup_forgot, null)
        val emailBox = dialogView.findViewById<EditText>(R.id.emailBox)
        builder.setView(dialogView)
        val dialog = builder.create()

        // Set up click listener for the reset button in the dialog
        dialogView.findViewById<Button>(R.id.btnReset).setOnClickListener {
            val userEmail = emailBox.text.toString()
            // Validate email input
            if (userEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                Toast.makeText(
                    this@LoginActivity,
                    "Enter your registered email ",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            // Send password reset email
            auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Check your email", Toast.LENGTH_SHORT)
                        .show()
                    dialog.dismiss() // Close the dialog on success
                } else {
                    Toast.makeText(this@LoginActivity, "Unable to send, failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        // Set up click listener for the cancel button in the dialog
        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        // Set dialog background to transparent
        dialog.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog.show()
    }

    // Displays Toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
