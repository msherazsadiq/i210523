package com.sherazsadiq.i210523

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class Session(
    var clientID: String? = null,
    var mentorID: String? = null,
    val date: String? = null,
    var time: String? = null
)
class BookYourSession : AppCompatActivity() {

    private var SDate:String?=null
    private var STime:String?=null
    private val databaseRef = FirebaseDatabase.getInstance()

    var mentorMe: Mentor? = null
    @SuppressLint("WrongViewCast", "MissingInflatedId", "SetTextI18n", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_your_session)

        mentorMe = intent.getSerializableExtra("mentor") as Mentor

        val mentorNameText = findViewById<TextView>(R.id.mentorName)
        mentorNameText.text = mentorMe?.name

        val mentorPic = mentorMe?.mentorPicture
        val mentorProfileImage = findViewById<ImageView>(R.id.mentorProfilePicture)
        Glide.with(this@BookYourSession).load(mentorPic)
            .centerCrop()
            .circleCrop()
            .into(mentorProfileImage)

        val mentorSessionPriceText = findViewById<TextView>(R.id.mentorSessionPrice)
        mentorSessionPriceText.text = "$" + mentorMe?.sessionPrice + "/Session"

        val mentorRatingText = findViewById<TextView>(R.id.mentorRating)
        mentorRatingText.text = mentorMe?.rating.toString()

        val time1: Button = findViewById(R.id.timeSlot1)
        val time2: Button = findViewById(R.id.timeSlot2)
        val time3: Button = findViewById(R.id.timeSlot3)

        time1.setOnClickListener {
            STime = time1.text.toString()
            (it as Button).setTextColor(Color.WHITE)
            time2.setTextColor(Color.BLACK)
            time3.setTextColor(Color.BLACK)
        }

        time2.setOnClickListener {
            STime = time2.text.toString()
            (it as Button).setTextColor(Color.WHITE)
            time1.setTextColor(Color.BLACK)
            time3.setTextColor(Color.BLACK)
        }

        time3.setOnClickListener {
            STime = time3.text.toString()
            (it as Button).setTextColor(Color.WHITE)
            time1.setTextColor(Color.BLACK)
            time2.setTextColor(Color.BLACK)
        }

        val bookButton = findViewById<Button>(R.id.bookAppointmentButton)
        bookButton.setOnClickListener {
            if (STime != null && SDate != null) {
                addSession(mentorMe?.mentorID ?: "", SDate, STime)
                Toast.makeText(this@BookYourSession, "Session Booked", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@BookYourSession, MainActivity::class.java)
                startActivity(intent)
            } else {
                if (STime == null) {
                    Toast.makeText(this@BookYourSession, "Please select a time", Toast.LENGTH_SHORT)
                        .show()
                }
                if (SDate == null) {
                    Toast.makeText(this@BookYourSession, "Please select a Date", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


        val calendarView: CalendarView = findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note: month is 0-based, so we add 1 to get the correct month
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            SDate = selectedDate.toString()
            Toast.makeText(this, "Selected date is $selectedDate", Toast.LENGTH_SHORT).show()
        }



        val back_arrow = findViewById<ImageButton>(R.id.backArrowButton)
        back_arrow.setOnClickListener {
            val intent = Intent(this, MentorProfileActivity::class.java)
            intent.putExtra("mentor", mentorMe)
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

    }

    private fun addSession(mentorId: String, date: String?, time: String?) {
        val user = FirebaseAuth.getInstance().currentUser
        val session = Session( user!!.uid,mentorId, date, time)

        val newSessionRef = databaseRef.getReference("Sessions").push()
        // Use the unique key to set the value of the new session
        newSessionRef.setValue(session).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Data stored successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to store data: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }

        addChat(mentorId)
        newSessionRef.setValue(session)
    }

    private fun addChat(Mentor_id: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        if (userId != null && Mentor_id != null) {
            val userChatRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("chat")
            userChatRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.children.any { it.value == Mentor_id }) {
                        // If the mentor's ID does not exist in the chat node, push the new ID
                        userChatRef.push().setValue(Mentor_id)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle possible errors.
                }
            })
        }
    }
}