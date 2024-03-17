package com.sherazsadiq.i210523

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


class MentorProfileActivity : AppCompatActivity() {

    var mentorMe: Mentor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_profile)

        mentorMe = intent.getSerializableExtra("mentor") as Mentor

        val mentorNameText = findViewById<TextView>(R.id.mentorName)
        mentorNameText.text = mentorMe?.name

        val mentorPic = mentorMe?.mentorPicture
        val mentorProfileImage = findViewById<ImageView>(R.id.mentorProfilePicture)
        Glide.with(this@MentorProfileActivity).load(mentorPic)
            .centerCrop()
            .circleCrop()
            .into(mentorProfileImage)

        val mentorDescriptionText = findViewById<TextView>(R.id.mentorDescription)
        mentorDescriptionText.text = mentorMe?.description

        val mentorRatingText = findViewById<TextView>(R.id.mentorRating)
        mentorRatingText.text = mentorMe?.rating.toString()



        val back_arrow = findViewById<ImageButton>(R.id.backArrowButton)
        back_arrow.setOnClickListener {
            val intent = Intent(this, SearchResults::class.java)
            startActivity(intent)
            finish()
        }

        val drop_review = findViewById<ImageButton>(R.id.dropReviewButton)
        drop_review.setOnClickListener {
            val clickedMentor: Mentor? = mentorMe
            val intent = Intent(this, DropReview::class.java)
            intent.putExtra("mentor", clickedMentor)
            startActivity(intent)
            finish()
        }

        val joinCommunity = findViewById<ImageButton>(R.id.joinCommunityButton)
        joinCommunity.setOnClickListener {
            val intent = Intent(this, CommunityChat::class.java)
            startActivity(intent)
            finish()
        }


        val bookSession = findViewById<Button>(R.id.bookSessionButton)
        bookSession.setOnClickListener {
            val clickedMentor: Mentor? = mentorMe
            val intent = Intent(this, BookYourSession::class.java)
            intent.putExtra("mentor", clickedMentor)
            startActivity(intent)
            finish()
        }



    }
}