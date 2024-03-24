package com.sherazsadiq.i210523

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.Serializable

data class User(
    val name: String = "",
    val email: String = "",
    val contactNumber: String = "",
    val country: String = "",
    val city: String = "",
    val password: String = "",
    val profilePicture: String? = "",
    val id: String = ""
): Serializable


class SignUpActivity : AppCompatActivity(), LifecycleObserver {

    private lateinit var countrySpinner: Spinner
    private lateinit var citySpinner: Spinner

    private val auth = FirebaseAuth.getInstance()


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        countrySpinner = findViewById(R.id.signup_countryspinner)
        citySpinner = findViewById(R.id.signup_cityspinner)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.countries_array,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countrySpinner.adapter = adapter

        countrySpinner.setSelection(0)
        citySpinner.setSelection(0)

        // Set OnItemSelectedListener to handle item selection
        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Set the selected item's text color and text size
                val selectedView = parent?.getChildAt(0) as? TextView
                selectedView?.setTextColor(resources.getColor(R.color.darkGray))
                selectedView?.textSize = 14f

                // Call the updateCitySpinner function with the selected country position
                updateCitySpinner(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing here
            }
        }

        val Account_loginButton = findViewById<TextView>(R.id.haveAccount_LoginButton)
        Account_loginButton.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
            finish()

        }

        // signup button click
        val SignUp_Button = findViewById<TextView>(R.id.signupbutton)
        SignUp_Button.setOnClickListener {
            //Toast.makeText(this, "Verify Number", Toast.LENGTH_SHORT).show()

            val inputName = findViewById<EditText>(R.id.signupName)
            val inputEmail = findViewById<EditText>(R.id.signupEmail)
            val inputContact = findViewById<EditText>(R.id.signupContactNumber)
            val inputContactNumber = inputContact.text.toString()

            val inputCountry = countrySpinner.selectedItem.toString()
            val inputCity = citySpinner.selectedItem.toString()

            val inputPassword = findViewById<EditText>(R.id.signupPassword)
            //check password length must be 8 or more
            if(inputPassword.text.length < 8){
                Toast.makeText(this, "Password must be 8 or more characters", Toast.LENGTH_SHORT).show()
                inputPassword.requestFocus()
                return@setOnClickListener
            }

            if(!checkInputValidation(inputName.text.toString())) return@setOnClickListener
            if(!checkInputValidation(inputEmail.text.toString())) return@setOnClickListener
            if(!checkInputValidation(inputContact.text.toString())) return@setOnClickListener
            if(!checkInputValidation(inputPassword.text.toString())) return@setOnClickListener


            registerUser(inputName.text.toString(), inputEmail.text.toString(), inputContactNumber, inputCountry, inputCity, inputPassword.text.toString())


        }


    }

    private fun updateCitySpinner(countryPosition: Int) {
        val citiesArrayResourceId = when (countryPosition) {
            1 -> R.array.Pakistan_city
            2 -> R.array.India_city
            3 -> R.array.Germany_city
            4 -> R.array.USA_city
            else -> 0
        }

        if (citiesArrayResourceId != 0) {
            val citiesArray: Array<String> = resources.getStringArray(citiesArrayResourceId)
            val cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, citiesArray)
            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            citySpinner.adapter = cityAdapter
        }

        // Set OnItemSelectedListener to handle item selection
        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Set the selected item's text color and text size
                val selectedView = parent?.getChildAt(0) as? TextView
                selectedView?.setTextColor(resources.getColor(R.color.darkGray))
                selectedView?.textSize = 14f

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing here
            }
        }
    }

    private fun checkInputValidation(inputText: String): Boolean {
        if (TextUtils.isEmpty(inputText)) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun registerUser(name: String, email: String, contactNumber: String, country: String, city: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "createUserWithEmail:success")
                    val user = auth.currentUser
                    val id = user?.uid.toString()
                    Toast.makeText(baseContext, "User registered successfully.", Toast.LENGTH_SHORT).show()
                    storeInDatabase(name, email, contactNumber, country, city, password, id)
                    val intent = Intent(this, EditProfileActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun storeInDatabase(name: String, email: String, contactNumber: String, country: String, city: String, password: String, id: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Users")

        val user = User(name, email, contactNumber, country, city, password, "", id)
        myRef.child(auth.currentUser?.uid.toString()).child("UserInfo").setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "storeInDatabase:success")
                } else {
                    Log.w("TAG", "storeInDatabase:failure", task.exception)
                }
            }
    }

}


