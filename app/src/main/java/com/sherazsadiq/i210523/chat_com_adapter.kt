package com.sherazsadiq.i210523

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class chat_com_adapter(private val users: MutableList<User>) : RecyclerView.Adapter<chat_com_adapter.UserViewHolder>() {
    private var filtereduser: List<User> = users
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.user_image)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickeduser: User? = filtereduser[position]
                    val intent = Intent(itemView.context, MentorChatActivity::class.java)
                    //intent.putExtra("user", clickeduser)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_com_item, parent, false)
        return UserViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val temp_user = filtereduser[position]
        val profilePicture = temp_user?.profilePicture
        if (profilePicture != null) {
            Glide.with(holder.itemView.context).load(profilePicture)
                .centerCrop()
                .circleCrop()
                .into(holder.userImage)
        } else {
            // Handle the case when profilePicture is null
        }
    }
    override fun getItemCount() = filtereduser.size
    fun updateusers(newusers: List<User>) {
        users.addAll(newusers)
        filtereduser = users.distinctBy { it.email }
        notifyDataSetChanged()
    }
}