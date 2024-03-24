package com.sherazsadiq.i210523



import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase




class user_chat_adapter(private val users: MutableList<User>) : RecyclerView.Adapter<user_chat_adapter.UserViewHolder>() {

    private var filtereduser: List<User> = users
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userImage: ImageView = itemView.findViewById(R.id.user_image)
        val userName: TextView = itemView.findViewById(R.id.user_name)


        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickeduser: User? = filtereduser[position]
                    if (clickeduser != null) {
                        val intent = Intent(itemView.context, MentorChatActivity::class.java)
                        intent.putExtra("user", clickeduser)
                        itemView.context.startActivity(intent)
                    } else {
                        // Handle the case when clickeduser is null
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_items, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val temp_user = filtereduser[position]
        val profilePicture = temp_user?.profilePicture
        Glide.with(holder.itemView.context).load(profilePicture)
            .centerCrop()
            .circleCrop()
            .into(holder.userImage)

        holder.userName.text = temp_user.name
    }

    override fun getItemCount() = filtereduser.size

    fun updateusers(newusers: List<User>) {
        users.addAll(newusers)
        filtereduser = users.distinctBy { it.email }
        notifyDataSetChanged()
    }



}