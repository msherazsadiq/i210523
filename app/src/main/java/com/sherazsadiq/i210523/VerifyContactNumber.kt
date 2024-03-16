package com.sherazsadiq.i210523

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class VerifyContactNumber : AppCompatActivity() {
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftInMillis: Long = 20000 // 30 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_contact_number)

        val backArrow = findViewById<ImageButton>(R.id.backArrowButton)
        backArrow.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        val contactNumber = intent.getStringExtra("contactNumber")
        val textview = findViewById<TextView>(R.id.phoneNumberTextView).apply{
            text = contactNumber
        }




        val dailOne = findViewById<Button>(R.id.dailOneButton)
        val dailTwo = findViewById<Button>(R.id.dailTwoButton)
        val dailThree = findViewById<Button>(R.id.dailThreeButton)
        val dailFour = findViewById<Button>(R.id.dailFourButton)
        val dailFive = findViewById<Button>(R.id.dailFiveButton)
        val dailSix = findViewById<Button>(R.id.dailSixButton)
        val dailSeven = findViewById<Button>(R.id.dailSevenButton)
        val dailEight = findViewById<Button>(R.id.dailEightButton)
        val dailNine = findViewById<Button>(R.id.dailNineButton)
        val dailZero = findViewById<Button>(R.id.dailZeroButton)
        val dailDel = findViewById<ImageButton>(R.id.dailDeleteButton)

        dailOne.setOnClickListener { appendToVerificationCode("1") }
        dailTwo.setOnClickListener { appendToVerificationCode("2") }
        dailThree.setOnClickListener { appendToVerificationCode("3") }
        dailFour.setOnClickListener { appendToVerificationCode("4") }
        dailFive.setOnClickListener { appendToVerificationCode("5") }
        dailSix.setOnClickListener { appendToVerificationCode("6") }
        dailSeven.setOnClickListener { appendToVerificationCode("7") }
        dailEight.setOnClickListener { appendToVerificationCode("8") }
        dailNine.setOnClickListener { appendToVerificationCode("9") }
        dailZero.setOnClickListener { appendToVerificationCode("0") }
        dailDel.setOnClickListener { removeFromVerificationCode() }





        val verifybutton = findViewById<TextView>(R.id.verifyButton)
        verifybutton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }


        // display timer and after that a button to resend the code and again start timer
        val timerTextView = findViewById<Button>(R.id.resendCodeButton)

        // Initialize and start the countdown timer
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimer()
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                val timerTextView = findViewById<Button>(R.id.resendCodeButton)
                timerTextView.text = "Resend Code" // Display text or enable button for resend
                timerTextView.isEnabled = true // Enable the button after timer finishes
            }
        }.start()

        val resendButton = findViewById<Button>(R.id.resendCodeButton)
        resendButton.isEnabled = false // Disable the button until the timer finishes
        resendButton.setOnClickListener {
            // Reset the timer
            timeLeftInMillis = 20000
            countDownTimer.start()
            Toast.makeText(this, "Code has been resent", Toast.LENGTH_SHORT).show()
        }






    }

    @SuppressLint("SetTextI18n")
    private fun updateTimer() {
        val timerTextView = findViewById<Button>(R.id.resendCodeButton)
        val seconds = (timeLeftInMillis / 1000).toInt()
        val timeLeftFormatted = String.format("%02d", seconds)
        timerTextView.text = "00:$timeLeftFormatted"
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the timer to prevent memory leaks
        countDownTimer.cancel()
    }

    private fun appendToVerificationCode(digit: String) {
        val verificationCode1 = findViewById<EditText>(R.id.verification1)
        val verificationCode2 = findViewById<EditText>(R.id.verification2)
        val verificationCode3 = findViewById<EditText>(R.id.verification3)
        val verificationCode4 = findViewById<EditText>(R.id.verification4)
        val verificationCode5 = findViewById<EditText>(R.id.verification5)

        if (verificationCode1.text.isEmpty()) {
            verificationCode1.setText(digit)
            verificationCode2.requestFocus()
        } else if (verificationCode2.text.isEmpty()) {
            verificationCode2.setText(digit)
            verificationCode3.requestFocus()
        } else if (verificationCode3.text.isEmpty()) {
            verificationCode3.setText(digit)
            verificationCode4.requestFocus()
        } else if (verificationCode4.text.isEmpty()) {
            verificationCode4.setText(digit)
            verificationCode5.requestFocus()
        } else if (verificationCode5.text.isEmpty()) {
            verificationCode5.setText(digit)
        }
    }

    private fun removeFromVerificationCode() {
        val verificationCode1 = findViewById<EditText>(R.id.verification1)
        val verificationCode2 = findViewById<EditText>(R.id.verification2)
        val verificationCode3 = findViewById<EditText>(R.id.verification3)
        val verificationCode4 = findViewById<EditText>(R.id.verification4)
        val verificationCode5 = findViewById<EditText>(R.id.verification5)

        if (!verificationCode5.text.isEmpty()) {
            verificationCode5.text.clear()
            verificationCode5.requestFocus()
        } else if (!verificationCode4.text.isEmpty()) {
            verificationCode4.text.clear()
            verificationCode4.requestFocus()
        } else if (!verificationCode3.text.isEmpty()) {
            verificationCode3.text.clear()
            verificationCode3.requestFocus()
        } else if (!verificationCode2.text.isEmpty()) {
            verificationCode2.text.clear()
            verificationCode2.requestFocus()
        } else if (!verificationCode1.text.isEmpty()) {
            verificationCode1.text.clear()
            verificationCode1.requestFocus()
        }
    }



}