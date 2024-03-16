package com.sherazsadiq.i210523

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)


        val goBackButton = findViewById<ImageButton>(R.id.backArrowButton)
        goBackButton.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
            finish()
        }


        val newPassword = findViewById<EditText>(R.id.newPassword)
        val confirmPassword = findViewById<EditText>(R.id.confirmPassword)
        val resetPasswordButton = findViewById<Button>(R.id.resetPasswordButton)

        resetPasswordButton.setOnClickListener {

            /*
            val user = auth.currentUser
            val newPasswordText = newPassword.text.toString()
            val confirmPasswordText = confirmPassword.text.toString()

            if (newPasswordText == confirmPasswordText) {
                user?.updatePassword(newPasswordText)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Password updated successfully.", Toast.LENGTH_SHORT).show()
                            Log.d("TAG", "User password updated.")
                        }
                    }
            } else {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
            }
             */

            val intent = Intent(this, login::class.java)
            startActivity(intent)
            finish()
        }

        val noReset = findViewById<Button>(R.id.noresetPassworsLoginButton)
        noReset.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
            finish()
        }

    }
}