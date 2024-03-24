package com.sherazsadiq.i210523

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream
import java.util.UUID

class EditProfileActivity : AppCompatActivity() {

    // Define a constant for the image request code
    private val PICK_IMAGE_REQUEST = 1
    // Define a variable for the selected image URI
    private var imageUri: Uri? = null
    // Define Firebase Storage reference
    private val storageRef = Firebase.storage.reference
    // Define Firebase Database reference
    private val databaseRef = FirebaseDatabase.getInstance().getReference("Users")
    // Define ImageView as a class-level variable
    private lateinit var selectImage: ImageView

    private lateinit var countrySpinner: Spinner
    private lateinit var citySpinner: Spinner

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        loadUserInfo()

        val backBtn = findViewById<ImageButton>(R.id.backArrowButton)
        backBtn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        val updateButton = findViewById<Button>(R.id.updateButton)
        updateButton.setOnClickListener {
            val inputName = findViewById<TextView>(R.id.editName).text.toString()
            val inputEmail = findViewById<TextView>(R.id.editEmail).text.toString()
            val inputContactNumber = findViewById<TextView>(R.id.editContactNumber).text.toString()
            val inputCountry = countrySpinner.selectedItem.toString()
            val inputCity = citySpinner.selectedItem.toString()

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(userId!!).child("UserInfo")

            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Check if the user exists in the database
                    if (dataSnapshot.exists()) {
                        // Fetch current email and password
                        val currentEmail = dataSnapshot.child("email").getValue(String::class.java)
                        val currentPassword = dataSnapshot.child("password").getValue(String::class.java)

                        // Check if current email and password are not null
                        if (!currentEmail.isNullOrEmpty() && !currentPassword.isNullOrEmpty()) {
                            // Upload email to Authentication for next time login
                            updateEmail(inputEmail, currentEmail, currentPassword)

                            // Update data in the database
                            updateData(inputName, inputEmail, inputContactNumber, inputCountry, inputCity)
                            Toast.makeText(this@EditProfileActivity, "Profile Updated", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@EditProfileActivity, "Current email or password is null or empty", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@EditProfileActivity, "User not found in the database", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("TAG", "Error getting user data.", databaseError.toException())
                    Toast.makeText(this@EditProfileActivity, "Error getting user data", Toast.LENGTH_SHORT).show()
                }
            })

        }

        // select image from galley and set it to the image view
        selectImage = findViewById(R.id.editprofilePicture)
        selectImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }

        countrySpinner = findViewById(R.id.editCountry)
        citySpinner = findViewById(R.id.editCity)

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

    // update the Authentication Email
    private fun updateEmail(newEmail: String, currentEmail: String?, password: String?){
        val user = FirebaseAuth.getInstance().currentUser
        //Toast id, email, current email, password
        Toast.makeText(this, "User: $user", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "New Email: $newEmail", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "Current Email: $currentEmail", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "Password: $password", Toast.LENGTH_SHORT).show()

        if(user == null || currentEmail == null || password == null) {
            Toast.makeText(this, "User not found or current email/password is null", Toast.LENGTH_SHORT).show()
            return
        }
        val credential = EmailAuthProvider.getCredential(currentEmail, password)

        user.reauthenticate(credential)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    user.updateEmail(newEmail)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Email updated", Toast.LENGTH_SHORT).show()
                                Log.d("TAG", "User email address updated.")
                            } else {
                                Log.w("TAG", "Error updating email", task.exception)
                            }
                        }
                } else {
                    Log.w("TAG", "Error re-authenticating", it.exception)
                }
            }
    }

    //update data in the database of current user
    private fun updateData(name: String, email: String, contactNumber: String, country: String, city: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if(userId == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            return
        }
        if(name == "" || email == "" || contactNumber == "" || country == "" || city == "") {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        val user = mapOf(
            "name" to name,
            "email" to email,
            "contactNumber" to contactNumber,
            "country" to country,
            "city" to city
        )

        databaseRef.child(userId).child("UserInfo").updateChildren(user as Map<String, Any>)

        // Update the shared preferences with the new user data
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("email", email)
        editor.putString("contactNumber", contactNumber)
        editor.putString("country", country)
        editor.putString("city", city)
        editor.apply()
    }

    private fun loadUserInfo() {
        val user = auth.currentUser
        val uid = user?.uid

        // Get the shared preferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

        // Get the user's name, email, contact number, country, and city from the shared preferences
        var name = sharedPreferences.getString("name", null)
        var email = sharedPreferences.getString("email", null)
        var contactNumber = sharedPreferences.getString("contactNumber", null)
        var country = sharedPreferences.getString("country", null)
        var city = sharedPreferences.getString("city", null)

        // Display the user's data in the EditText fields
        displayUserInfo(name, email, contactNumber, country, city)

        // If the user's data is not in the shared preferences, get them from the Firebase Database
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid!!).child("UserInfo")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if the user's data exists in the Firebase Database
                if (dataSnapshot.exists()) {
                    // Get the user's data
                    name = dataSnapshot.child("name").getValue(String::class.java)
                    email = dataSnapshot.child("email").getValue(String::class.java)
                    contactNumber = dataSnapshot.child("contactNumber").getValue(String::class.java)
                    country = dataSnapshot.child("country").getValue(String::class.java)
                    city = dataSnapshot.child("city").getValue(String::class.java)

                    // Save the user's data in the shared preferences
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("name", name)
                    editor.putString("email", email)
                    editor.putString("contactNumber", contactNumber)
                    editor.putString("country", country)
                    editor.putString("city", city)
                    editor.apply()

                    // Display the user's data in the EditText fields
                    displayUserInfo(name, email, contactNumber, country, city)
                } else {
                    // User's data does not exist in the Firebase Database, sign them out
                    auth.signOut()
                    Toast.makeText(this@EditProfileActivity, "User does not exist.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors
            }
        })
    }

    private fun displayUserInfo(name: String?, email: String?, contactNumber: String?, country: String?, city: String?) {
        val nameEditText = findViewById<EditText>(R.id.editName)
        val emailEditText = findViewById<EditText>(R.id.editEmail)
        val contactNumberEditText = findViewById<EditText>(R.id.editContactNumber)
        val countrySpinner = findViewById<Spinner>(R.id.editCountry)
        val citySpinner = findViewById<Spinner>(R.id.editCity)

        nameEditText.setText(name)
        emailEditText.setText(email)
        contactNumberEditText.setText(contactNumber)
        countrySpinner.setSelection(getIndex(countrySpinner, country))
        citySpinner.setSelection(getIndex(citySpinner, city))
    }

    private fun getIndex(spinner: Spinner, value: String?): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == value) {
                return i
            }
        }
        return 0
    }



    // Override onActivityResult method
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check if the request code is the same as the PICK_IMAGE_REQUEST
        if (requestCode == PICK_IMAGE_REQUEST) {
            // Get the URI of the selected image
            imageUri = data?.data

            // Set the image view to the selected image and make it round
            val imageView = findViewById<ShapeableImageView>(R.id.editprofilePicture)
            val radius = resources.getDimension(R.dimen.default_corner_radius)
            imageView.shapeAppearanceModel = imageView.shapeAppearanceModel
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, radius)
                .build()

            imageView.setImageURI(imageUri)

            // Upload the image to Firebase Storage
            val ref = storageRef.child("images/${UUID.randomUUID()}")
            val uploadTask = ref.putFile(imageUri!!)
            uploadTask.addOnSuccessListener {
                // Get the download URL
                ref.downloadUrl.addOnSuccessListener { uri ->
                    // Save the download URL to the Firebase Database
                    val user = FirebaseAuth.getInstance().currentUser
                    val userRef = databaseRef.child(user!!.uid).child("UserInfo")
                    userRef.child("profilePicture").setValue(uri.toString())

                    // Update the shared preferences with the new image URL
                    val sharedPreferences: SharedPreferences = getSharedPreferences("ProfilePicture", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("profilePictureUri", uri.toString())
                    editor.apply()
                }
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
    }
}