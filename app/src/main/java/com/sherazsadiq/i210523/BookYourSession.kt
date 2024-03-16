package com.sherazsadiq.i210523

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class BookYourSession : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_your_session)

        val back_arrow = findViewById<ImageButton>(R.id.backArrowButton)
        back_arrow.setOnClickListener {
            val intent = Intent(this, MentorProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        val chatBtn = findViewById<ImageButton>(R.id.chatButton)
        chatBtn.setOnClickListener {
            val intent = Intent(this, MentorChatActivity::class.java)
            startActivity(intent)
            finish()
        }

        val phoneBtn = findViewById<ImageButton>(R.id.phoneButton)
        phoneBtn.setOnClickListener {
            val intent = Intent(this, MentorAudioCallActivity::class.java)
            startActivity(intent)
            finish()
        }

        val videoBtn = findViewById<ImageButton>(R.id.videoCallButton)
        videoBtn.setOnClickListener {
            val intent = Intent(this, MentorVideoCallActivity::class.java)
            startActivity(intent)
            finish()
        }

        val bookBtn = findViewById<Button>(R.id.bookAppointmentButton)
        bookBtn.setOnClickListener {
            val intent = Intent(this, MentorProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}