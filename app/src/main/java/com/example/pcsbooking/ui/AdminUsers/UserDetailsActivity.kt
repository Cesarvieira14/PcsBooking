package com.example.pcsbooking.ui.AdminUsers

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pcsbooking.Model.User
import com.example.pcsbooking.R
import com.google.firebase.database.FirebaseDatabase

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var userName: String
    private lateinit var userEmail: String
    private lateinit var userPhone: String
    private var isAdmin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        userId = intent.getStringExtra("USER_ID") ?: ""
        userName = intent.getStringExtra("USER_NAME") ?: ""
        userEmail = intent.getStringExtra("USER_EMAIL") ?: ""
        userPhone = intent.getStringExtra("USER_PHONE") ?: ""
        isAdmin = intent.getBooleanExtra("USER_IS_ADMIN", false)

        findViewById<TextView>(R.id.userIdTextView).text = "User ID: $userId"
        findViewById<TextView>(R.id.userNameTextView).text = "Name: $userName"
        findViewById<TextView>(R.id.userEmailTextView).text = "Email: $userEmail"
        findViewById<TextView>(R.id.userPhoneTextView).text = "Phone: $userPhone"
        findViewById<TextView>(R.id.userRoleTextView).text =
            if (isAdmin) "Role: Admin" else "Role: User"

        findViewById<Button>(R.id.updateRoleButton).setOnClickListener { updateUserRole() }
        findViewById<Button>(R.id.deleteUserButton).setOnClickListener { deleteUser() }
    }

    private fun updateUserRole() {
        val database = FirebaseDatabase.getInstance().getReference("users/$userId")
        val updatedUser = User(userName, userEmail, userPhone, !isAdmin)
        database.setValue(updatedUser).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "User role updated", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to update user role", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteUser() {
        val database = FirebaseDatabase.getInstance().getReference("users/$userId")
        database.removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to delete user", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
