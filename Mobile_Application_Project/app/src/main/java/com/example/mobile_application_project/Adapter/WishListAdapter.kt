package com.example.mobile_application_project.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_application_project.Model.ItemsModel
import com.example.mobile_application_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class WishListAdapter(
    private val context: Context,
    private var wishlistItems: MutableList<ItemsModel>
) : RecyclerView.Adapter<WishListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_wishlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = wishlistItems[position]
        holder.itemTitle.text = item.title
        holder.itemPrice.text = "$${item.price}"
        if (item.picUrl.isNotEmpty()) {
            Picasso.get().load(item.picUrl[0]).into(holder.itemImage)
        }

        holder.deleteButton.setOnClickListener {
            removeItem(item, position)
        }
    }

    override fun getItemCount(): Int {
        return wishlistItems.size
    }

    private fun removeItem(item: ItemsModel, position: Int) {
        val email = FirebaseAuth.getInstance().currentUser?.email?.replace(".", ",")
        if (email != null) {
            FirebaseDatabase.getInstance().reference.child("Wishlists").child(email).child(item.orderItemId).removeValue()
                .addOnSuccessListener {
                    wishlistItems.removeAt(position)
                    notifyItemRemoved(position)
                    Toast.makeText(context, "Item removed from wishlist", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to remove item from wishlist", Toast.LENGTH_SHORT).show()
                }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val itemTitle: TextView = itemView.findViewById(R.id.itemTitle)
        val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
    }
}
