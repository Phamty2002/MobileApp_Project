package com.example.mobile_application_project.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile_application_project.R
import com.google.firebase.auth.FirebaseAuth

class AdminDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dashboard)

        // Initialize the UI elements and set click listeners
        initUI()
    }

    private fun initUI() {
        findViewById<ImageView>(R.id.button_user_management).setOnClickListener {
            // Handle User Management button click
            navigateToUserManagement()
        }

        findViewById<ImageView>(R.id.button_order_tracking).setOnClickListener {
            // Handle Order Tracking button click
            navigateToOrderTracking()
        }

        findViewById<ImageView>(R.id.button_sign_out).setOnClickListener {
            // Handle Sign Out button click
            signOut()
        }
    }

    private fun navigateToUserManagement() {
        val intent = Intent(this, UserManagementActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToOrderTracking() {
        val intent = Intent(this, OrderTrackingActivity::class.java)
        startActivity(intent)
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
        Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show()
    }
}
