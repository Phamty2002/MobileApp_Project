package com.example.mobile_application_project.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_application_project.R
import com.example.mobile_application_project.adapter.UserAdapter
import com.example.mobile_application_project.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserManagementActivity : AppCompatActivity() {

    private lateinit var searchBar: EditText
    private lateinit var recyclerViewUsers: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private var userList: MutableList<UserModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_management)

        searchBar = findViewById(R.id.search_bar)
        recyclerViewUsers = findViewById(R.id.recycler_view_users)

        recyclerViewUsers.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(userList, this::onEditClick, this::onDeleteClick)
        recyclerViewUsers.adapter = userAdapter

        fetchUsers()

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun fetchUsers() {
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("Users")

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserModel::class.java)
                    user?.userId = userSnapshot.key // Ensure the userId is set correctly
                    user?.let { userList.add(it) }
                }
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("UserManagementActivity", "Database Error: ${error.message}")
            }
        })
    }

    private fun filter(text: String) {
        val filteredList = userList.filter { user ->
            user.name?.contains(text, true) == true || user.email?.contains(text, true) == true
        }
        userAdapter.updateList(filteredList)
    }

    private fun onEditClick(user: UserModel) {
        val intent = Intent(this, EditUserActivity::class.java).apply {
            putExtra("userId", user.userId)
            putExtra("name", user.name)
            putExtra("email", user.email)
            putExtra("phone", user.phone)
            putExtra("address", user.address)
        }
        startActivity(intent)
    }

    private fun onDeleteClick(user: UserModel) {
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("Users").child(user.userId!!)

        usersRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show()
                userList.remove(user)
                userAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "Failed to delete user", Toast.LENGTH_SHORT).show()
                Log.e("UserManagementActivity", "Error deleting user: ${task.exception?.message}")
            }
        }
    }
}
