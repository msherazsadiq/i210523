package com.sherazsadiq.i210523

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class login : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.loginEmail)
        val password = findViewById<EditText>(R.id.loginPassword)
        val loginButton = findViewById<Button>(R.id.loginbutton)

        loginButton.setOnClickListener {
            if (username.text.toString() == "" && password.text.toString() == "") {
                Toast.makeText(this, "Please enter your username and password", Toast.LENGTH_SHORT).show()
            } else {
                authenticateUser(username.text.toString(), password.text.toString())
            }


            Log.i("Login", "Username: ${username.text} Password: ${password.text}")
        }

        val SignUP_Button = findViewById<Button>(R.id.noAccount_SignupButton)
        SignUP_Button.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
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

    private fun authenticateUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login", "signInWithEmail:success")
                    val user = auth.currentUser

                    // Check if the user exists in the Firebase Database
                    val userRef = FirebaseDatabase.getInstance().getReference("Users").child(user!!.uid)
                    userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // User exists in the Firebase Database, proceed as usual
                                val sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putBoolean("isLoggedIn", true)
                                editor.apply()

                                val intent = Intent(this@login, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // User does not exist in the Firebase Database, sign them out
                                auth.signOut()
                                Toast.makeText(baseContext, "User does not exist.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle any errors
                        }
                    })
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()

                    // If the account is deleted, clear the shared preferences
                    if (task.exception is FirebaseAuthInvalidUserException) {
                        val sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.clear()
                        editor.apply()
                    }
                }
            }
    }

}