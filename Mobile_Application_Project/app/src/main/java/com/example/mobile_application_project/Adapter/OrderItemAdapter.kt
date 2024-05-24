package com.example.mobile_application_project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_application_project.databinding.OrderItemBinding
import com.example.mobile_application_project.model.OrderItem

class OrderItemAdapter(private val items: List<OrderItem>) : RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>() {

    class OrderItemViewHolder(val binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val item = items[position]
        holder.binding.productNameText.text = item.title ?: item.itemId // Use title for item name
        holder.binding.quantityText.text = "Quantity: ${item.quantity}"
    }

    override fun getItemCount(): Int = items.size
}
