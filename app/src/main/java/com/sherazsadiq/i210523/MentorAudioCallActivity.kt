package com.sherazsadiq.i210523

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MentorAudioCallActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_audio_call)

        val callEndBtn = findViewById<ImageButton>(R.id.mentor_audio_call_end)
        callEndBtn.setOnClickListener {
            finish()

        }
    }
}