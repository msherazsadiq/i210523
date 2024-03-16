package com.sherazsadiq.i210523

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class VideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)


        val camBackBtn = findViewById<ImageView>(R.id.camera_back)
        camBackBtn.setOnClickListener {
            onBackPressed()
        }

        val camVideoBtn = findViewById<Button>(R.id.camera_photo)
        camVideoBtn.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}