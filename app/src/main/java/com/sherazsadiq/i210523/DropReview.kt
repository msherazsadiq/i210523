package com.sherazsadiq.i210523

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class DropReview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drop_review)


        val back = findViewById<ImageButton>(R.id.backArrowButton)
        back.setOnClickListener {
            val intent = Intent(this, SearchResults::class.java)
            startActivity(intent)
            finish()
        }

        val submit = findViewById<Button>(R.id.submitfeedbackbutton)
        submit.setOnClickListener {
            val intent = Intent(this, MentorProfile::class.java)
            startActivity(intent)
            finish()
        }

    }
}