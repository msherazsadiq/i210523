package com.sherazsadiq.i210523

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class Review(
    var clientID: String? = null,
    var mentorID: String? = null,
    val reviewText: String? = null,
    var rating: Float? = null
)

class DropReview : AppCompatActivity() {

    private var mentorMe: Mentor? = null

    private val databaseRef = FirebaseDatabase.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drop_review)

        mentorMe = intent.getSerializableExtra("mentor") as Mentor

        val back = findViewById<ImageButton>(R.id.backArrowButton)
        back.setOnClickListener {
            val intent = Intent(this, SearchResults::class.java)
            startActivity(intent)
            finish()
        }


        val mentorNameTextView: TextView = findViewById(R.id.mentorName)
        mentorNameTextView.text = mentorMe?.name

        val profilePicture = mentorMe?.mentorPicture
        val profileImage: ImageView = findViewById(R.id.mentorProfilePicture)
        Glide.with(this@DropReview).load(profilePicture)
            .centerCrop()
            .circleCrop()
            .into(profileImage)

        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val messagebox = findViewById<EditText>(R.id.feedBackBox)




        val submit = findViewById<Button>(R.id.submitfeedbackbutton)
        submit.setOnClickListener {
            val rating: Float = ratingBar.rating
            val review_text: String = messagebox.text.toString()
            addReview(rating, review_text)
            calculateAndAddRatingToMentorProfile()
            val intent = Intent(this, MentorProfileActivity::class.java)
            intent.putExtra("mentor", mentorMe)
            startActivity(intent)
            finish()
        }



    }

    private fun calculateAndAddRatingToMentorProfile() {
        // Get a reference to the Reviews node
        val reviewsRef = databaseRef.getReference("Reviews")

        // Query for reviews where the mentorID matches the current mentor's ID
        val query = reviewsRef.orderByChild("mentorID").equalTo(mentorMe?.mentorID)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var totalRating = 0f
                var count = 0

                // Loop through the snapshot and calculate the total rating
                for (reviewSnapshot in dataSnapshot.children) {
                    val review = reviewSnapshot.getValue(Review::class.java)
                    totalRating += review?.rating ?: 0f
                    count++
                }

                // Calculate the average rating
                val averageRating = if (count > 0) String.format("%.2f", totalRating / count).toFloat() else 0f

                // Update the mentor's rating in the database
                val user = FirebaseAuth.getInstance().currentUser
                val mentorRef = databaseRef.getReference("Users").child(mentorMe?.mentorID!!).child("MentorInfo")
                mentorRef.child("rating").setValue(averageRating)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        })

    }


    private fun addReview(ratings: Float, Review_text: String) {

        val user = FirebaseAuth.getInstance().currentUser
        val review = Review(
            clientID = user?.uid,
            mentorID = mentorMe?.mentorID,
            reviewText = Review_text,
            rating = ratings
        )


        val newSessionRef = databaseRef.getReference("Reviews").push()

        // Use the unique key to set the value of the new session
        newSessionRef.setValue(review)
    }



}