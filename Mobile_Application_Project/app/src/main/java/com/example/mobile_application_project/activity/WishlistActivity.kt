package com.example.mobile_application_project.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_application_project.Adapter.WishListAdapter
import com.example.mobile_application_project.Model.ItemsModel
import com.example.mobile_application_project.databinding.ActivityWishlistBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class WishlistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWishlistBinding
    private lateinit var database: DatabaseReference
    private lateinit var wishlistAdapter: WishListAdapter
    private var wishlistItems = mutableListOf<ItemsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference
        val email = intent.getStringExtra("USER_EMAIL")

        if (email != null) {
            fetchWishlistItems(email.replace(".", ","))
        } else {
            Log.e("WishlistActivity", "User email is null")
        }

        binding.recyclerViewWishlist.layoutManager = LinearLayoutManager(this)
        wishlistAdapter = WishListAdapter(this, wishlistItems)
        binding.recyclerViewWishlist.adapter = wishlistAdapter

    }

    private fun fetchWishlistItems(email: String) {
        binding.progressBar.visibility = View.VISIBLE
        val wishlistRef = database.child("Wishlists").child(email)
        wishlistRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                wishlistItems.clear()
                if (snapshot.exists()) {
                    for (itemSnapshot in snapshot.children) {
                        val item = itemSnapshot.getValue(ItemsModel::class.java)
                        if (item != null) {
                            wishlistItems.add(item)
                        }
                    }
                }

                if (wishlistItems.isEmpty()) {
                    binding.textViewNoWishlist.visibility = View.VISIBLE
                } else {
                    binding.textViewNoWishlist.visibility = View.GONE
                }

                wishlistAdapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("WishlistActivity", "Database error: ${error.message}")
                binding.progressBar.visibility = View.GONE
            }
        })
    }
}
