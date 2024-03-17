package com.sherazsadiq.i210523

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import java.io.Serializable
import java.util.UUID

data class Mentor(
    val name: String = "",
    val description: String = "",
    val status: String = "",
    val sessionPrice: String = "",
    val mentorPicture: String? = "",
    val mentorVideo: String? = "",
    val mentorID: String = "",
    val rating: Float = 0.0f

) : Serializable

class AddNewMentor : AppCompatActivity() {

    // Define a constant for the image request code
    private val PICK_IMAGE_REQUEST = 1
    // Define a constant for the image request code
    private val PICK_VIDEO_REQUEST = 2
    // Define a variable for the selected image URI
    private var imageUri: Uri? = null
    // Define a variable for the selected video URI
    private var videoUri: Uri? = null
    // Define Firebase Storage reference
    private val storageRef = Firebase.storage.reference
    // Define Firebase Database reference
    private val databaseRef = FirebaseDatabase.getInstance().getReference("Users")

    private lateinit var mentor_status: Spinner


    private var mentorPictureURI: String? = null
    private var mentorVideoURI: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_mentor)

        mentor_status = findViewById(R.id.mentorStatus)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.mentor_status,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mentor_status.adapter = adapter

        mentor_status.setSelection(0)

        mentor_status.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Set the selected item's text color and text size
                val selectedView = parent?.getChildAt(0) as? TextView
                selectedView?.setTextColor(resources.getColor(R.color.darkGray))
                selectedView?.textSize = 14f

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing here
            }
        }

        val backArrow = findViewById<ImageButton>(R.id.backArrowButton)
        backArrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val uploadMentorPhotoBtn = findViewById<ImageButton>(R.id.uploadMentorPhotoButton)
        uploadMentorPhotoBtn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }

        val uploadMentorVideoBtn = findViewById<ImageButton>(R.id.uploadMentorVideoButton)
        uploadMentorVideoBtn.setOnClickListener {
            val intent = Intent()
            intent.type = "video/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST)
        }


        val uploadBtn = findViewById<Button>(R.id.uploadButton)
        uploadBtn.setOnClickListener {
            storeMentorInDataBase()

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

    private fun storeMentorInDataBase() {
        val mentorName = findViewById<TextView>(R.id.newMentorName)
        val mentorDescription = findViewById<TextView>(R.id.newMentorDescription)
        val mentorStatus = mentor_status.selectedItem.toString()
        val mentorSessionPrice = findViewById<TextView>(R.id.newMentorPrice)

        val user = FirebaseAuth.getInstance().currentUser

        val mentor = Mentor(
            mentorName.text.toString(),
            mentorDescription.text.toString(),
            mentorStatus,
            mentorSessionPrice.text.toString(),
            mentorPictureURI,
            mentorVideoURI,
            user!!.uid,
            0.0f
        )


        val userRef = databaseRef.child(user!!.uid).child("MentorInfo").setValue(mentor)


    }

    // Override onActivityResult method
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check if the request code is the same as the PICK_IMAGE_REQUEST
        if (requestCode == PICK_IMAGE_REQUEST) {
            // Get the URI of the selected image
            imageUri = data?.data

            // Upload the image to Firebase Storage
            val ref = storageRef.child("images/${UUID.randomUUID()}")
            val uploadTask = ref.putFile(imageUri!!)
            uploadTask.addOnSuccessListener {
                // Get the download URL
                ref.downloadUrl.addOnSuccessListener { uri ->

                    mentorPictureURI = uri.toString()

                }
            }
        }
        else if(requestCode == PICK_VIDEO_REQUEST){
            // Get the URI of the selected video
            videoUri = data?.data

            // Upload the video to Firebase Storage
            val ref = storageRef.child("videos/${UUID.randomUUID()}")
            val uploadTask = ref.putFile(videoUri!!)
            uploadTask.addOnSuccessListener {
                // Get the download URL
                ref.downloadUrl.addOnSuccessListener { uri ->

                    mentorVideoURI = uri.toString()

                }
            }

        }
    }
}