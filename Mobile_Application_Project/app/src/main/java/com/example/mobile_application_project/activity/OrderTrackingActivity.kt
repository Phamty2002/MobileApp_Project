package com.example.mobile_application_project.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_application_project.R
import com.example.mobile_application_project.adapter.OrderAdapter
import com.example.mobile_application_project.model.Order
import com.example.mobile_application_project.model.OrderItem

class OrderTrackingActivity : AppCompatActivity() {

    private lateinit var orderRecyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    private var orderList: MutableList<Order> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_tracking)

        // Initialize the RecyclerView
        orderRecyclerView = findViewById(R.id.recyclerView_orders)
        orderRecyclerView.layoutManager = LinearLayoutManager(this)
        orderAdapter = OrderAdapter(orderList)
        orderRecyclerView.adapter = orderAdapter

        // Fetch the orders (this could be from a database or an API)
        fetchOrders()
    }

    private fun fetchOrders() {
        // Dummy data for demonstration
        val dummyOrders = listOf(
            Order(
                "1", "user1", "John Doe", listOf(
                    OrderItem("item1", "Pink singlet", 2, 35.0),
                    OrderItem("item2", "Jean Coat", 1, 55.0)
                ), 90.0, "2024-05-23 10:00", "Pending"
            ),
            Order(
                "2", "user2", "Jane Smith", listOf(
                    OrderItem("item3", "White Shirt", 3, 25.0),
                    OrderItem("item4", "Blue Jeans", 2, 40.0)
                ), 155.0, "2024-05-23 11:00", "Completed"
            )
        )
        orderList.addAll(dummyOrders)
        orderAdapter.notifyDataSetChanged()
    }
}
