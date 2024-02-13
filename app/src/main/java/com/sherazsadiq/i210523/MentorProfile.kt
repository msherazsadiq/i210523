package com.sherazsadiq.i210523

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class MentorProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_profile)

        val back_arrow = findViewById<ImageButton>(R.id.backArrowButton)
        back_arrow.setOnClickListener {
            val intent = Intent(this, SearchResults::class.java)
            startActivity(intent)
            finish()
        }

        val drop_review = findViewById<ImageButton>(R.id.dropReviewButton)
        drop_review.setOnClickListener {
            val intent = Intent(this, DropReview::class.java)
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
            val intent = Intent(this, BookYourSession::class.java)
            startActivity(intent)
            finish()
        }



    }
}