package com.example.pcsbooking.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.pcsbooking.R
import com.example.pcsbooking.ui.home.HomeActivity
import com.example.pcsbooking.ui.home.HomeFragment

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.LoginBtn).setOnClickListener{
            handleLoginButtonClick()
        }

        findViewById<Button>(R.id.RegisterBtn).setOnClickListener{
            handleRegisterButtonClick()
        }
    }

    private fun handleLoginButtonClick() {
        val start = Intent (this, HomeActivity::class.java)
        startActivity(start)
    }
    private fun handleRegisterButtonClick() {
        val start = Intent (this, RegisterActivity::class.java)
        startActivity(start)
    }
}