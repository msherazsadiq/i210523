package com.sherazsadiq.i210523

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchResults : AppCompatActivity() {

    private lateinit var adapter: SearchMentorAdapter
    private val mentors = mutableListOf<Mentor>()
    private val databaseRef = FirebaseDatabase.getInstance().getReference("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        val myRecyclerview = findViewById<RecyclerView>(R.id.SearchMentorRecyclerView)
        adapter = SearchMentorAdapter(mentors)
        myRecyclerview.layoutManager = LinearLayoutManager(this)
        myRecyclerview.adapter = adapter

        val searchT = intent.getStringExtra("searchText")
        readDatabase(searchT)


        val back_arrow = findViewById<ImageButton>(R.id.backArrowButton)
        back_arrow.setOnClickListener {
            val intent = Intent(this, LetsFind::class.java)
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

    private fun readDatabase(searchText: String?) {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach { snapshot ->
                    val mentorInfo = snapshot.child("MentorInfo").getValue(Mentor::class.java)
                    if (mentorInfo != null) {
                        val mentor = Mentor(
                            mentorInfo.name,
                            mentorInfo.description,
                            mentorInfo.status,
                            mentorInfo.sessionPrice,
                            mentorInfo.mentorPicture,
                            mentorInfo.mentorVideo,
                            mentorInfo.mentorID,
                            mentorInfo.rating

                        )
                        mentors.add(mentor)
                    }
                }
                val filteredMentors = if(searchText != null){
                    mentors.filter { it.name.contains(searchText, ignoreCase = true) }
                } else {
                    mentors
                }
                adapter.updateMentors(filteredMentors)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }


}