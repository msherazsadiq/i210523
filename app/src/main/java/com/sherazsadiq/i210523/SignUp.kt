package com.sherazsadiq.i210523

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sherazsadiq.i210523.R

class SignUp : AppCompatActivity() {

    private lateinit var countrySpinner: Spinner
    private lateinit var citySpinner: Spinner

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        countrySpinner = findViewById(R.id.countryspinner)
        citySpinner = findViewById(R.id.cityspinner)

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

        val Account_loginButton = findViewById<TextView>(R.id.haveAccount_LoginButton)
        Account_loginButton.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
            finish()

        }

        val SignUp_Button = findViewById<TextView>(R.id.signupbutton)
        SignUp_Button.setOnClickListener {
            Toast.makeText(this, "Verify Number", Toast.LENGTH_SHORT).show()

            val editText = findViewById<EditText>(R.id.signupContactNumber)
            val contactNumber = editText.text.toString()

            val intent = Intent(this, VerifyContactNumber::class.java).also {
                it.putExtra("contactNumber", contactNumber)
                startActivity(it)

            }
        }
    }

}


