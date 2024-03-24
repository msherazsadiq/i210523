package com.sherazsadiq.i210523

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean
import okhttp3.MediaType.Companion.toMediaTypeOrNull


val db = Firebase.firestore
val currentTime = Date()
val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
val formattedTime = formatter.format(currentTime)
data class message_class(
    val messagetype: String = "",
    val sender: String = "",
    val receiver: String = "",
    var message: String = "",
    val time: String = "",
    var key: String = ""

)


var user: User? = null

@SuppressLint("MissingInflatedId")


class MentorChatActivity : AppCompatActivity() {
    companion object {
        private const val PICK_FILE_REQUEST_CODE = 1
        private const val PICK_PHOTO_REQUEST_CODE = 2
        private const val TAKE_PHOTO_REQUEST_CODE = 3
        private const val CAMERA_PERMISSION_REQUEST_CODE = 4

    }

    var progressBar: ProgressBar? = null
    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null
    private lateinit var adapter: inbox_adapter
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    private val databaseRef = FirebaseDatabase.getInstance().getReference("Users")
    val message_list = mutableListOf<message_class>()
    private var recordingThread: Thread? = null
    private var isRecording = AtomicBoolean(false)
    private var recordingProgress = 0
    private var messagecheck = false
    private val MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1
    private var vm: message_class? = null

    private lateinit var myRecyclerview: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_chat)



        user = intent.getSerializableExtra("user") as User?
        val userName = findViewById<TextView>(R.id.chatWithName)
        userName.text = user?.name

        progressBar = findViewById(R.id.progressBar)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), MY_PERMISSIONS_REQUEST_RECORD_AUDIO)
        }

        val backBtn = findViewById<ImageButton>(R.id.backArrowButton)
        backBtn.setOnClickListener {
            onBackPressed()

        }

        val phoneBtn = findViewById<ImageButton>(R.id.phoneCallButton)
        phoneBtn.setOnClickListener {
            val intent = Intent(this, MentorAudioCallActivity::class.java)
            startActivity(intent)

        }

        val videBtn =  findViewById<ImageButton>(R.id.videoCallButton)
        videBtn.setOnClickListener {
            val intent = Intent(this, MentorVideoCallActivity::class.java)
            startActivity(intent)

        }



        val attachFileButton = findViewById<ImageButton>(R.id.addFileButton)
        attachFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
        }

        val sendPhoto = findViewById<ImageButton>(R.id.addPhotoButton)
        sendPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_PHOTO_REQUEST_CODE)
        }


        val camBtn = findViewById<ImageButton>(R.id.cameraButton)
        camBtn.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
            } else {
                openCamera()
            }

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


        //*********************************   Recycler View   *********************************

        layoutManager = LinearLayoutManager(this)
        myRecyclerview = findViewById<RecyclerView>(R.id.message_recycler_view)
        adapter = inbox_adapter(message_list,user!!.id)
        myRecyclerview.layoutManager = LinearLayoutManager(this)
        myRecyclerview.adapter = adapter
        readDatabase()

        //********************************** Send Message **********************************


        val sendButton: ImageView = findViewById(R.id.sendButton)
        sendButton.setOnClickListener {
            if(messagecheck==false) {
                val msg_box: EditText = findViewById(R.id.messageEditText)
                val msg = msg_box.text.toString()

                val message = message_class(
                    "text",
                    currentUser?.uid.toString(),
                    user!!.id,
                    msg,
                    formattedTime
                )
                AddMessageToDatabase(message)
                msg_box.setText("")
            }
            else{

            }

        }


        //********************************** Record audio **********************************

        val recordButton: ImageView = findViewById(R.id.voiceicon)
        recordButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                if (isRecording.get()) {
                    stopRecording()
                    val message = message_class(
                        "voice",
                        currentUser?.uid.toString(),
                        user!!.id,
                        audioFile.toString(),
                        formattedTime
                    )
                    AddMessageToDatabase(message)
                    audioFile = null
                } else {
                    startRecording()
                }
            } else {
                Toast.makeText(this, "Permission to record audio is not granted", Toast.LENGTH_SHORT).show()
            }
        }



    }




    //*********************************   Read database   *********************************

    private fun readDatabase() {
        val messagesRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser?.uid.toString()).child("Messages").child(user!!.id)
        messagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                message_list.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val message = postSnapshot.getValue(message_class::class.java)
                    val messageKey = postSnapshot.key
                    if (message != null) {
                        message.key = messageKey.toString()
                        message_list.add(message)

                    }
                }
                adapter.updatemessages(message_list)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
                Log.e("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }







    //********************************** Add Message to database **********************************
    private fun AddMessageToDatabase(message: message_class) {
        val senderRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser?.uid.toString()).child("Messages").child(user!!.id)
        val receiverRef = FirebaseDatabase.getInstance().getReference("Users").child(user!!.id).child("Messages").child(currentUser?.uid.toString())
        senderRef.push().setValue(message)
        receiverRef.push().setValue(message)



        val chatsRef = FirebaseDatabase.getInstance().getReference("Users").child(user!!.id).child("chat")
        chatsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var isPresent = false
                for (childSnapshot in dataSnapshot.children) {
                    if (childSnapshot.key == currentUser?.uid.toString()) {
                        isPresent = true
                        break
                    }
                }
                if (!isPresent) {
                    // Sender's ID is not present in Users.(Receiver's ID).chats, add it
                    chatsRef.push().setValue(currentUser?.uid.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
            }
        })

        // After adding the message to the database, send a notification.
        sendNotification(user!!.id, message.message)
    }
    //********************************** Send Notification **********************************
    private fun sendNotification(to: String, message: String) {
        val client = okhttp3.OkHttpClient()

        val json = "application/json; charset=utf-8".toMediaTypeOrNull()

        val jsonObject = JSONObject()
        try {
            jsonObject.put("to", to)
            val notificationObject = JSONObject()
            notificationObject.put("title", "New Message")
            notificationObject.put("body", message)
            jsonObject.put("notification", notificationObject)
        } catch (e: JSONException) {
            Log.e("TAG", "JSONException: " + e.message)
        }

        val body = okhttp3.RequestBody.create(json, jsonObject.toString())
        val request = okhttp3.Request.Builder()
            .url("https://fcm.googleapis.com/fcm/send")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "key=AAAAZjC2ctM:APA91bGLo5ixakcLGLLo8sq4FnyRO64T43l-r-1RxOAf6Jgl-T9S165qyluPr_GcdxpdKiftbNz4UTmN_u94SuRKudTBpTpDb_5OzPI0GGj7Eeqrw2zztjEqhkN3lLlvUPmr7sWzg9d-")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("TAG", "onFailure: " + e.message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                Log.i("TAG", "onResponse: " + response.body?.string())
            }
        })
    }

    //********************************** Voice Message **********************************

    private fun startRecording() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            audioFile = File.createTempFile("audio", ".3gp", cacheDir)
            setOutputFile(audioFile?.absolutePath)
            prepare()
            start()
        }
        isRecording.set(true)
        startRecordingThread()
    }


    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        isRecording.set(false)
        progressBar?.progress = 0
    }

    private fun startRecordingThread() {

        recordingThread = Thread {
            while (isRecording.get()) {
                recordingProgress += 1
                runOnUiThread {
                    if (!isFinishing) {
                        progressBar?.progress = recordingProgress
                    }
                }
                try {
                    Thread.sleep(1000) // Update every second
                } catch (e: InterruptedException) {
                    // Handle exception
                    e.printStackTrace()
                }
            }
            recordingProgress = 0
        }
        recordingThread?.start()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val fileUri = data.data.toString()

            val message = message_class(
                "file",
                currentUser?.uid.toString(),
                user!!.id,
                fileUri,
                formattedTime.toString()
            )
            AddMessageToDatabase(message)
        }
        else if (requestCode == PICK_PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val photoUri = data.data.toString()

            val message = message_class(
                "photo",
                currentUser?.uid.toString(),
                user!!.id,
                photoUri,
                formattedTime.toString()
            )
            AddMessageToDatabase(message)
        }
        else if (requestCode == TAKE_PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val photoUri = data.extras?.get("data").toString()

            val message = message_class(
                "photo",
                currentUser?.uid.toString(),
                user!!.id,
                photoUri,
                formattedTime.toString()
            )
            AddMessageToDatabase(message)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE)
    }

}