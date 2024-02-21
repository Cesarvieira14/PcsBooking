package com.example.pcsbooking.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.pcsbooking.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.RegisterBtn).setOnClickListener{
            handleRegisterButtonClick()
        }
    }
    private fun handleRegisterButtonClick() {
        val start = Intent (this, RegisterActivity::class.java)
        startActivity(start)
    }
}