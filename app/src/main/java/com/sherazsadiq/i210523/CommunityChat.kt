package com.sherazsadiq.i210523

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.bumptech.glide.Glide

data class message(
    var message: String? = "",
    val sender: String? = "",
    val receiver: String? = "",
    val time: String? = ""
)

var mentor_c: Mentor? = null
class CommunityChat<ImageView : Any> : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_FILE_PICKER = 2


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_chat)

        mentor_c = intent.getSerializableExtra("mentor") as Mentor
        val com_name: TextView = findViewById(R.id.CommunityName)
        com_name.text = mentor_c?.name+"'s"

        /*
        val profilePicture = mentor_c?.mentorPicture
        val profileImage: ImageView = findViewById(R.id.CommunityProfile)
        Glide.with(this@CommunityChat).load(profilePicture)
            .centerCrop()
            .circleCrop()
            .into(profileImage)


         */

        val backBtn = findViewById<ImageButton>(R.id.backArrowButton)
        backBtn.setOnClickListener{
            onBackPressed()
        }


        val phoneBtn = findViewById<ImageButton>(R.id.phoneCallButton)
        phoneBtn.setOnClickListener {
            val intent = Intent(this, MentorAudioCallActivity::class.java)
            startActivity(intent)

        }

        val videBtn = findViewById<ImageButton>(R.id.videoCallButton)
        videBtn.setOnClickListener {
            val intent = Intent(this, MentorVideoCallActivity::class.java)
            startActivity(intent)

        }

        val attachFileButton = findViewById<ImageButton>(R.id.addFileButton)
        attachFileButton.setOnClickListener {
            openFilePicker()
        }

        val camBtn = findViewById<ImageButton>(R.id.cameraButton)
        camBtn.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)

        }

        val sendBtn = findViewById<ImageButton>(R.id.sendButton)
        sendBtn.setOnClickListener {
            val intent = Intent(this, CommunityChat::class.java)
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

    // Function to open the file picker
    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*" // Allow any file type
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        startActivityForResult(intent, REQUEST_FILE_PICKER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_FILE_PICKER && resultCode == RESULT_OK) {
            // Handle the selected file
            val selectedFileUri = data?.data
            // Process the selected file URI as needed
        }
    }

}