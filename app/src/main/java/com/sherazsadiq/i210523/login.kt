package com.sherazsadiq.i210523

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class login : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show()
        val username = findViewById<EditText>(R.id.loginEmail)
        val password = findViewById<EditText>(R.id.loginPassword)
        val loginButton = findViewById<Button>(R.id.loginbutton)

        loginButton.setOnClickListener {
            if (username.text.toString() == "" && password.text.toString() == "") {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                Log.e("Login", "Login Successful")

                // Go to the MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()


            } /*else if (username.text.toString() == "" && password.text.toString() == "") {
                Toast.makeText(this, "Please enter your username and password", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }*/

            Log.i("Login", "Username: ${username.text} Password: ${password.text}")
        }

        val SignUP_Button = findViewById<Button>(R.id.noAccount_SignupButton)
        SignUP_Button.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }

        val forgot = findViewById<Button>(R.id.forgotPasswordButton)
        forgot.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
            finish()
        }

    }
}