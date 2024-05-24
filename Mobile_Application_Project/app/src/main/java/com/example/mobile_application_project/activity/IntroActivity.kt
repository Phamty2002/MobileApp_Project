package com.example.mobile_application_project.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mobile_application_project.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the USER_EMAIL from the intent
        val email = intent.getStringExtra("USER_EMAIL")
        Log.d("IntroActivity", "Received USER_EMAIL: $email")

        binding.startBtn.setOnClickListener {
            if (email != null) {
                // Start MainActivity with the USER_EMAIL
                val intent = Intent(this@IntroActivity, MainActivity::class.java)
                intent.putExtra("USER_EMAIL", email)
                startActivity(intent)
                finish()
            } else {
                // Log an error and show a Toast message if the email is null
                Log.e("IntroActivity", "USER_EMAIL is null")
                Toast.makeText(this, "Error: USER_EMAIL is null", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
