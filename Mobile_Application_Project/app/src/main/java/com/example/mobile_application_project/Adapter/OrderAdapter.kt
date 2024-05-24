package com.example.mobile_application_project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_application_project.databinding.OrderItemUserBinding
import com.example.mobile_application_project.model.Order

class OrderAdapter(private val orders: List<Order>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(val binding: OrderItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = OrderItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.binding.orderIdText.text = "Order ID: ${order.orderId}"
        holder.binding.orderTotalText.text = "Total: $${order.totalPrice}"
        holder.binding.orderTimeText.text = "Order Time: ${order.orderTime}"
        holder.binding.orderStatusText.text = "Status: ${order.status}"

        // Setup nested RecyclerView for order items
        holder.binding.itemsRecyclerView.layoutManager = LinearLayoutManager(holder.binding.itemsRecyclerView.context)
        holder.binding.itemsRecyclerView.adapter = OrderItemAdapter(order.items ?: emptyList())
    }

    override fun getItemCount(): Int = orders.size
}
