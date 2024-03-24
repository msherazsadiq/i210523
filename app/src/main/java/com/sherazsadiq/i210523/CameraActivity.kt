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
        /*
        // Assuming you have buttons with the IDs camera_photo, camera_slo_mo, camera_square, and camera_portrait
        val photoButton: Button = findViewById(R.id.camera_photo)
        photoButton.setOnClickListener {
            // Implement photo capture functionality
            capturePhoto()
        }


        val sloMoButton: Button = findViewById(R.id.camera_slo_mo)
        sloMoButton.setOnClickListener {
            // Implement slo-mo functionality
            captureSloMo()
        }

        val squareButton: Button = findViewById(R.id.camera_square)
        squareButton.setOnClickListener {
            // Implement square photo capture functionality
            captureSquarePhoto()
        }

        val portraitButton: Button = findViewById(R.id.camera_portrait)
        portraitButton.setOnClickListener {
            // Implement portrait photo capture functionality
            capturePortraitPhoto()
        }

         */
    }

    /*
    private fun capturePhoto() {
        // Implement photo capture functionality
    }

    private fun captureSloMo() {
        // Implement slo-mo functionality
    }

    private fun captureSquarePhoto() {
        // Implement square photo capture functionality
    }

    private fun capturePortraitPhoto() {
        // Implement portrait photo capture functionality
    }

     */
}