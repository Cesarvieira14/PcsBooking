package com.example.pcsbooking

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pcsbooking.Model.User
import com.example.pcsbooking.databinding.ActivityMainBinding
import com.example.pcsbooking.ui.authentication.LoginActivity
import com.example.pcsbooking.ui.admin.AdminActivity
import com.example.pcsbooking.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Check if user is already logged in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is signed in, navigate to the appropriate activity
            checkUserRole(currentUser.uid)
        } else {
            // No user is signed in, show the main layout
            binding.startBtn.setOnClickListener {
                navigateToLogin()
            }
        }
    }

    private fun checkUserRole(userId: String) {
        database.child("users").child(userId).get()
            .addOnSuccessListener { snapshot ->
                val userData = snapshot.getValue(User::class.java)
                if (userData != null) {
                    val intent = if (userData.admin) {
                        Intent(this, AdminActivity::class.java)
                    } else {
                        Intent(this, HomeActivity::class.java)
                    }
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    // User data not found, navigate to login
                    showToast("User data not found. Please log in again.")
                    navigateToLogin()
                }
            }
            .addOnFailureListener { exception ->
                // Error occurred while fetching user data
                showToast("Error fetching user data: ${exception.message}")
                navigateToLogin()
            }
    }

    private fun navigateToLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()  // Ensure the MainActivity is not kept in the back stack
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

