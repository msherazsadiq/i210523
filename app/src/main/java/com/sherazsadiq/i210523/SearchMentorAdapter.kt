package com.sherazsadiq.i210523

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily


class SearchMentorAdapter(private var mentors: MutableList<Mentor>) : RecyclerView.Adapter<SearchMentorAdapter.MentorViewHolder>() {
    private var filteredMentors: MutableList<Mentor> = mentors.toMutableList()

    inner class MentorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mentorName: TextView = itemView.findViewById(R.id.searchMentorName)
        val mentorDescription: TextView = itemView.findViewById(R.id.searchMentorDescription)
        val mentorStatus: TextView = itemView.findViewById(R.id.searchMentorAvailability)
        val mentorPrice: TextView = itemView.findViewById(R.id.searchMentorPrice)
        val mentorImage: ShapeableImageView = itemView.findViewById(R.id.searchMentorImage)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedMentor: Mentor? = mentors[position]
                    val intent = Intent(itemView.context, MentorProfileActivity::class.java)
                    intent.putExtra("mentor", clickedMentor)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_users_search_mentor_recycler, parent, false)
        return MentorViewHolder(view)
    }

    override fun onBindViewHolder(holder: MentorViewHolder, position: Int) {
        val mentor = mentors[position]
        holder.mentorName.text = mentor.name
        holder.mentorDescription.text = mentor.description
        holder.mentorStatus.text = mentor.status
        holder.mentorPrice.text = mentor.sessionPrice
        Glide.with(holder.mentorImage.context).load(mentor.mentorPicture)
            .centerCrop()
            .into(holder.mentorImage)

        // Set the corners to be rounded
        val radius = holder.mentorImage.context.resources.getDimension(R.dimen.search_mentor_picture)
        holder.mentorImage.shapeAppearanceModel = holder.mentorImage.shapeAppearanceModel
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, radius)
            .build()
    }

    override fun getItemCount() = filteredMentors.size

    fun updateMentors(newMentors: List<Mentor>) {
        mentors.clear()
        mentors.addAll(newMentors)
        filteredMentors = mentors
        notifyDataSetChanged()
    }

}