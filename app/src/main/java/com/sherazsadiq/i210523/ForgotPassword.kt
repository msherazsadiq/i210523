package com.sherazsadiq.i210523

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ForgotPassword : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val backArrow = findViewById<ImageButton>(R.id.backArrowButton)
        backArrow.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
            finish()
        }

        val sendEmail = findViewById<Button>(R.id.emailSendButton)
        sendEmail.setOnClickListener {
            /*
            val emailAddress = findViewById<EditText>(R.id.forgotEmail)
            Firebase.auth.sendPasswordResetEmail(emailAddress.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "Email sent.")
                    }
                }

             */
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        val loginback = findViewById<Button>(R.id.rememberPasswordLoginButton)
        loginback.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
            finish()
        }



    }
}