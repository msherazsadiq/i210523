package com.sherazsadiq.i210523

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MentorVideoCallActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_video_call)

        val callEndBtn = findViewById<ImageButton>(R.id.mentor_video_call_end)
        callEndBtn.setOnClickListener {
            finish()

        }
    }
}