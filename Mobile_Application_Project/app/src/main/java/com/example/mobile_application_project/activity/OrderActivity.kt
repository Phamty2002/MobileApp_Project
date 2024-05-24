package com.example.mobile_application_project.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_application_project.adapter.OrderAdapter
import com.example.mobile_application_project.databinding.ActivityOrderBinding
import com.example.mobile_application_project.model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var orderAdapter: OrderAdapter
    private var orders: MutableList<Order> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        binding.recyclerViewOrders.layoutManager = LinearLayoutManager(this)
        orderAdapter = OrderAdapter(orders)
        binding.recyclerViewOrders.adapter = orderAdapter

        val userId = auth.currentUser?.uid
        if (userId != null) {
            Log.d("OrderActivity", "Fetching orders for userId: $userId")
            fetchOrders(userId)
        } else {
            Log.e("OrderActivity", "User ID is null")
        }
    }

    private fun fetchOrders(userId: String) {
        binding.progressBar.visibility = View.VISIBLE
        database.child("Orders").orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    orders.clear()
                    if (snapshot.exists()) {
                        for (orderSnapshot in snapshot.children) {
                            val order = orderSnapshot.getValue(Order::class.java)
                            if (order != null) {
                                Log.d("OrderActivity", "Order found: ${order.orderId}")
                                orders.add(order)
                            }
                        }
                    } else {
                        Log.d("OrderActivity", "No orders found for userId: $userId")
                    }
                    binding.progressBar.visibility = View.GONE
                    if (orders.isEmpty()) {
                        binding.textViewNoOrders.visibility = View.VISIBLE
                    } else {
                        binding.textViewNoOrders.visibility = View.GONE
                    }
                    orderAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("OrderActivity", "Database error: ${error.message}")
                    binding.progressBar.visibility = View.GONE
                }
            })
    }
}
