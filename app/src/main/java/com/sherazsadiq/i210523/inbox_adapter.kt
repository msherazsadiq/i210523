package com.sherazsadiq.i210523

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


val database = FirebaseDatabase.getInstance()
private val databaseRef = FirebaseDatabase.getInstance().getReference("Users")

class inbox_adapter(private var messages: MutableList<message_class>, private val currentUserId: String?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var filteredMentors: MutableList<message_class> = messages.toMutableList()

    inner class SendingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message_text: TextView = itemView.findViewById(R.id.message_text)
        var time_text: TextView = itemView.findViewById(R.id.time_text)
        init {
            itemView.setOnLongClickListener {
                showEditDeleteOptions(adapterPosition, "sending", itemView.context)
                true
            }
        }
    }

    inner class ReceivingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message_text: TextView = itemView.findViewById(R.id.message_text)
        var time_text: TextView = itemView.findViewById(R.id.time_text)

        init {
            itemView.setOnLongClickListener {
                showEditDeleteOptions(adapterPosition, "receiving", itemView.context)
                true
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (filteredMentors[position].sender != currentUserId) 0 else 1
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.right_side_message_sending, parent, false)
            SendingViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.left_side_message_receiving, parent, false)
            ReceivingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mentor = filteredMentors[position]
        if (holder is SendingViewHolder) {
            holder.message_text.text = mentor.message
            holder.time_text.text = mentor.time
        } else if (holder is ReceivingViewHolder) {
            holder.message_text.text = mentor.message
            holder.time_text.text = mentor.time
        }
    }

    override fun getItemCount() = filteredMentors.size

    fun updatemessages(newMentors: List<message_class>) {
        filteredMentors.clear()
        filteredMentors.addAll(newMentors)
        notifyDataSetChanged()
    }

    private fun showEditDeleteOptions(position: Int, type: String, context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_delete, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
        val alertDialog = builder.show()

        val editOption: TextView = dialogView.findViewById(R.id.edit_option)
        val deleteOption: TextView = dialogView.findViewById(R.id.delete_option)

        if(type != "receiving") {
            editOption.setOnClickListener {
                show_update(position, type, context)
                alertDialog.dismiss()
            }
        }

        deleteOption.setOnClickListener {
            val messageKey = messages[position].key
            val uidr:String = messages[position].receiver
            val uids:String= messages[position].sender

            if(currentUserId == uids){
                databaseRef.child(uids).child("Messages").child(uidr).child(messageKey).removeValue()
                databaseRef.child(uidr).child("Messages").child(uids).child(messageKey).removeValue()
            }
            else{
                databaseRef.child(uids).child("Messages").child(uidr).child(messageKey).removeValue()
                //databaseRef.child(uidr).child("Messages").child(uids).child(messageKey).removeValue()
            }

            removeMessage(position)
            alertDialog.dismiss()
        }

    }
    fun removeMessage(position: Int) {
        messages.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun show_update(position: Int, type: String, context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.show_edit_button, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
        val alertDialog = builder.show()
        val editMessage: EditText = dialogView.findViewById(R.id.edit_message)
        val updateButton: Button = dialogView.findViewById(R.id.update_button)


        updateButton.setOnClickListener {
            val newMessage = editMessage.text.toString()
            updateMessage(position, newMessage)
            alertDialog.dismiss()
        }
    }


    fun updateMessage(position: Int, newMessage: String) {
        val messageToUpdate = messages[position]
        val messageKey = messageToUpdate.key
        val receiverId = messageToUpdate.receiver
        val senderId = messageToUpdate.sender

        // Update the message in the sender's database
        databaseRef.child(senderId).child("Messages").child(receiverId).child(messageKey).child("message").setValue(newMessage)

        // Update the message in the receiver's database
        databaseRef.child(receiverId).child("Messages").child(senderId).child(messageKey).child("message").setValue(newMessage)

        // Update the message in the local list
        messageToUpdate.message = newMessage
        notifyItemChanged(position)
    }
}