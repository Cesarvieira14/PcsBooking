package com.example.pcsbooking.ui.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.pcsbooking.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


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

        else
            showToast("sucesssfull")
        // All validations passed, proceed with registration logic
        // For example:
        // registerUser(fullName, email, phoneNo, password)

        // You can finish the activity if registration is successful
        // finish()
        findViewById<EditText>(R.id.editTxtEmail).text.clear()
        findViewById<EditText>(R.id.editTxtFullname).text.clear()
        findViewById<EditText>(R.id.editTxtPhoneNumber).text.clear()
        findViewById<EditText>(R.id.editTxtPassword).text.clear()
        findViewById<EditText>(R.id.editTxtConfirmPassword).text.clear()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}

