package com.example.mobile_application_project.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_application_project.R
import com.example.mobile_application_project.model.UserModel

class UserAdapter(
    private var userList: List<UserModel>,
    private val onEditClick: (UserModel) -> Unit,
    private val onDeleteClick: (UserModel) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount() = userList.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userName: TextView = itemView.findViewById(R.id.user_name)
        private val userEmail: TextView = itemView.findViewById(R.id.user_email)
        private val editButton: Button = itemView.findViewById(R.id.button_edit)
        private val deleteButton: Button = itemView.findViewById(R.id.button_delete)

        fun bind(user: UserModel) {
            userName.text = user.name
            userEmail.text = user.email
            editButton.setOnClickListener { onEditClick(user) }
            deleteButton.setOnClickListener { onDeleteClick(user) }
        }
    }

    fun updateList(newList: List<UserModel>) {
        userList = newList
        notifyDataSetChanged()
    }
}
