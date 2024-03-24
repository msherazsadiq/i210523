package com.sherazsadiq.i210523

import android.content.Intent
import android.content.SharedPreferences
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ProfileActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // function to load profile picture and use info
        val profilePicView = findViewById<ImageView>(R.id.profilePicture)
        loadProfilePicture(profilePicView)
        loadUserInfo()

        val backBtn = findViewById<ImageButton>(R.id.backArrowButton)
        backBtn.setOnClickListener {
            onBackPressed()

        }

        val logoutBtn = findViewById<TextView>(R.id.threeDots)
        logoutBtn.setOnClickListener {
            logout()
            val intent = Intent(this, login::class.java)
            startActivity(intent)
            finish()

        }



        val editProfile1 = findViewById<FrameLayout>(R.id.editprofile1)
        //val editProfile2 = findViewById<FrameLayout>(R.id.editprofile2)

        editProfile1.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)

        }

        /*
        editProfile2.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)

        }
        */

        val bookedSessionBtn = findViewById<ImageButton>(R.id.bookedSessionButton)
        bookedSessionBtn.setOnClickListener{
            val intent = Intent(this, BookedSessionActivity::class.java)
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

    private fun logout(){
        auth.signOut()

        // Clear the SharedPreferences
        val sharedPreferencesPicture: SharedPreferences = getSharedPreferences("ProfilePicture", Context.MODE_PRIVATE)
        sharedPreferencesPicture.edit().clear().apply()

        val sharedPreferencesInfo: SharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        sharedPreferencesInfo.edit().clear().apply()

    }


    private fun loadProfilePicture(profilePicView: ImageView){
        val user = auth.currentUser
        val uid = user?.uid

        // Get the reference to the user in the Firebase Database
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid!!).child("UserInfo")

        // Get the shared preferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("ProfilePicture", Context.MODE_PRIVATE)

        // Get the profile picture URI from the shared preferences
        var profilePicUri = sharedPreferences.getString("profilePictureUri", null)

        if (profilePicUri == null) {
            // If the profile picture URI is not in the shared preferences, get it from the Firebase Database
            userRef.child("profilePicture").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get the profile picture URI
                    profilePicUri = dataSnapshot.getValue(String::class.java)

                    // Save the profile picture URI in the shared preferences
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("profilePictureUri", profilePicUri)
                    editor.apply()

                    // Load the image into the ImageView using Glide
                    loadImage(profilePicUri, profilePicView)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors
                }
            })
        } else {
            // If the profile picture URI is in the shared preferences, load the image into the ImageView
            loadImage(profilePicUri, profilePicView)
        }
    }

    private fun loadImage(profilePicUri: String?, profilePicView: ImageView) {

        Glide.with(this@ProfileActivity)
            .load(profilePicUri)
            .centerCrop() // Use centerCrop to make the image fit in the ImageView
            .circleCrop() // Use circleCrop to make the image circular
            .into(profilePicView)
    }

    private fun loadUserInfo() {
        val user = auth.currentUser
        val uid = user?.uid

        // Get the reference to the user in the Firebase Database
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid!!).child("UserInfo")

        // Get the shared preferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

        // Get the user's name and city from the shared preferences
        var name = sharedPreferences.getString("name", null)
        var city = sharedPreferences.getString("city", null)

        if (name == null || city == null) {
            // If the user's name or city is not in the shared preferences, get them from the Firebase Database
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get the user's name and city
                    name = dataSnapshot.child("name").getValue(String::class.java)
                    city = dataSnapshot.child("city").getValue(String::class.java)

                    // Save the user's name and city in the shared preferences
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("name", name)
                    editor.putString("city", city)
                    editor.apply()

                    // Display the user's name and city in the TextViews
                    displayUserInfo(name, city)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors
                }
            })
        } else {
            // If the user's name and city are in the shared preferences, display them in the TextViews
            displayUserInfo(name, city)
        }
    }

    private fun displayUserInfo(name: String?, city: String?) {
        val nameTextView = findViewById<TextView>(R.id.myName)
        val cityTextView = findViewById<TextView>(R.id.myCity)

        nameTextView.text = name
        cityTextView.text = city
    }
}