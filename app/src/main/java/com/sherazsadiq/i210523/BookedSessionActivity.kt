package com.sherazsadiq.i210523

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class BookedSessionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booked_session)

        val backBtn = findViewById<ImageButton>(R.id.backArrowButton)
        backBtn.setOnClickListener {
            finish()
        }

    }
}