package com.example.mobile_application_project.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile_application_project.R
import com.example.mobile_application_project.model.UserModel
import com.google.firebase.database.FirebaseDatabase

class EditUserActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var buttonSave: Button

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        editTextName = findViewById(R.id.edit_text_name)
        editTextEmail = findViewById(R.id.edit_text_email)
        editTextPhone = findViewById(R.id.edit_text_phone)
        editTextAddress = findViewById(R.id.edit_text_address)
        buttonSave = findViewById(R.id.button_save)

        userId = intent.getStringExtra("userId") ?: ""

        // Load existing user data
        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val phone = intent.getStringExtra("phone")
        val address = intent.getStringExtra("address")

        editTextName.setText(name)
        editTextEmail.setText(email)
        editTextPhone.setText(phone)
        editTextAddress.setText(address)

        buttonSave.setOnClickListener {
            saveUser()
        }
    }

    private fun saveUser() {
        val name = editTextName.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val phone = editTextPhone.text.toString().trim()
        val address = editTextAddress.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Ensure we are updating the existing user by retaining the userId
        val user = UserModel(userId, name, email, null, phone, address, null)

        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("Users").child(userId)

        usersRef.setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to update user", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
