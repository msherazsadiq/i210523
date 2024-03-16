package com.sherazsadiq.i210523

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout

class ChatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)


        val back_arrow = findViewById<ImageButton>(R.id.backArrowButton)
        back_arrow.setOnClickListener {
            onBackPressed()
        }


        val chat1 = findViewById<LinearLayout>(R.id.johnCopperChat)
        chat1.setOnClickListener {
            val intent = Intent(this, MentorChatActivity::class.java)
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
            val intent = Intent(this, ChatsActivity::class.java)
            startActivity(intent)

        }

        val profile_Button = findViewById<ImageButton>(R.id.profileButton)
        profile_Button.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)

        }






    }
}