package com.example.mobile_application_project.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile_application_project.databinding.ActivityProfileBinding
import com.example.mobile_application_project.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val email = intent.getStringExtra("USER_EMAIL")?.replace(".", ",")
        if (email == null) {
            Log.e("ProfileActivity", "Email is null")
            finish()
            return
        }

        database = FirebaseDatabase.getInstance().reference.child("Users").child(email)

        // Fetch user data and update UI
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                if (user != null) {
                    binding.tvProfileUserName.text = user.name ?: "N/A"
                    binding.tvProfileEmail.text = user.email ?: "N/A"
                    binding.etProfilePhone.setText(user.phone ?: "")
                    binding.etProfileAddress.setText(user.address ?: "")
                } else {
                    Log.e("ProfileActivity", "User data is null")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ProfileActivity", "Database error: ${error.message}")
            }
        })

        binding.btnEditProfile.setOnClickListener {
            val updatedPhone = binding.etProfilePhone.text.toString()
            val updatedAddress = binding.etProfileAddress.text.toString()

            val updates = mapOf(
                "phone" to updatedPhone,
                "address" to updatedAddress
            )

            database.updateChildren(updates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    Log.e("ProfileActivity", "Failed to update user profile.")
                }
            }
        }

        binding.btnHome.setOnClickListener {
            navigateHome()
        }

        binding.btnSignOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun navigateHome() {
        val email = intent.getStringExtra("USER_EMAIL")?.replace(".", ",")
        val intent = Intent(this@ProfileActivity, MainActivity::class.java)
        intent.putExtra("USER_EMAIL", email)
        startActivity(intent)
        finish()
    }
}
