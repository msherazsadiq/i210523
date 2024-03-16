package com.sherazsadiq.i210523

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView

class LetsFind : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lets_find)

        val searchText = findViewById<SearchView>(R.id.searchbar)
        searchText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val intent = Intent(this@LetsFind, SearchResults::class.java)
                intent.putExtra("searchText", query)
                startActivity(intent)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // This method can be used to update the list as the user types in the SearchView.
                // For now, we leave it empty as we're not implementing this functionality.
                return false
            }
        })


        val back_arrow = findViewById<ImageButton>(R.id.backArrowButton)
        back_arrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }




        val home_Button = findViewById<ImageButton>(R.id.homeButton)
        home_Button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
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