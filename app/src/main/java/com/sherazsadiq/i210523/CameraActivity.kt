package com.sherazsadiq.i210523

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView

class CameraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val camBackBtn = findViewById<ImageView>(R.id.camera_back)
        camBackBtn.setOnClickListener {
            onBackPressed()
        }

        val camVideoBtn = findViewById<Button>(R.id.camera_video)
        camVideoBtn.setOnClickListener {
            val intent = Intent(this, VideoActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}