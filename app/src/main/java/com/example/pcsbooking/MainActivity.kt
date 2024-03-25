package com.example.pcsbooking

import com.example.pcsbooking.ui.authentication.LoginActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.pcsbooking.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.startBtn).setOnClickListener {
            handleStartButtonClick()
        }
    }
        private fun handleStartButtonClick() {
            val start = Intent (this, LoginActivity::class.java)
            startActivity(start)
        }

}