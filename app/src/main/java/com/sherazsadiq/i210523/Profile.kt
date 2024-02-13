package com.sherazsadiq.i210523

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val backBtn = findViewById<ImageButton>(R.id.backArrowButton)
        backBtn.setOnClickListener {
            onBackPressed()

        }

        val editProfile1 = findViewById<FrameLayout>(R.id.editprofile1)
        val editProfile2 = findViewById<FrameLayout>(R.id.editprofile2)

        editProfile1.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)

        }

        editProfile2.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)

        }

        val bookedSessionBtn = findViewById<ImageButton>(R.id.bookedSessionButton)
        bookedSessionBtn.setOnClickListener{
            val intent = Intent(this, BookedSession::class.java)
            startActivity(intent)

        }





        val home_Button = findViewById<ImageButton>(R.id.homeButton)
        home_Button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

        val search_Button = findViewById<ImageButton>(R.id.searchButton)
        search_Button.setOnClickListener {
            val intent = Intent(this, LetsFind::class.java)
            startActivity(intent)

        }

        val addMentor_Button = findViewById<ImageButton>(R.id.addMentorButton)
        addMentor_Button.setOnClickListener {
            val intent = Intent(this, AddNewMentor::class.java)
            startActivity(intent)

        }

        val chat_Button = findViewById<ImageButton>(R.id.chatButton)
        chat_Button.setOnClickListener {
            val intent = Intent(this, Chats::class.java)
            startActivity(intent)

        }

        val profile_Button = findViewById<ImageButton>(R.id.profileButton)
        profile_Button.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)

        }



    }
}