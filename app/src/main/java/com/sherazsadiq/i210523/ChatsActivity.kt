package com.sherazsadiq.i210523

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.CopyOnWriteArrayList

class ChatsActivity : AppCompatActivity() {

    private lateinit var adapter: user_chat_adapter
    private lateinit var adapter_com: user_chat_adapter

    //getinstance of current user
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val usersList = CopyOnWriteArrayList<User>()
    private val databaseRef = FirebaseDatabase.getInstance().getReference("Users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        val myRecyclerview = findViewById<RecyclerView>(R.id.recyclerchatView)
        adapter = user_chat_adapter(usersList)
        myRecyclerview.layoutManager = LinearLayoutManager(this)
        myRecyclerview.adapter = adapter
        readDatabase()

       /* val myRecyclerview_com = findViewById<RecyclerView>(R.id.recyclerCommunityView)
        adapter_com = chat_com_adapter(usersList)
        myRecyclerview_com.layoutManager = LinearLayoutManager(this)
        myRecyclerview_com.adapter = adapter_com
        readDatabase_com()
*/
        findViewById<ImageButton>(R.id.backArrowButton).setOnClickListener {
            onBackPressed()
        }

        findViewById<ImageButton>(R.id.homeButton).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<ImageButton>(R.id.searchButton).setOnClickListener {
            startActivity(Intent(this, LetsFind::class.java))
        }

        findViewById<ImageButton>(R.id.addMentorButton).setOnClickListener {
            startActivity(Intent(this, AddNewMentor::class.java))
        }

        findViewById<ImageButton>(R.id.profileButton).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun readDatabase() {
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                usersList.clear() // Clear the list before adding any users
                dataSnapshot.children.forEach { snapshot ->
                    if (snapshot.key == currentUser?.uid) {
                        val chatUserIds =
                            snapshot.child("chat").children.mapNotNull { it.getValue(String::class.java) }
                        chatUserIds.forEach { userId ->
                            val userSnapshot = dataSnapshot.child(userId)
                            val user = userSnapshot.child("UserInfo").getValue(User::class.java)
                            if (user != null && usersList.none { it.email == user.email }) {
                                usersList.add(user)
                            }
                        }
                    }
                }
                runOnUiThread {
                    adapter.updateusers(usersList)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun readDatabase_com() {
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                usersList.clear() // Clear the list before adding any users
                dataSnapshot.children.forEach { snapshot ->
                    if (snapshot.key == currentUser?.uid) {
                        val chatUserIds =
                            snapshot.child("Community").children.mapNotNull { it.getValue(String::class.java) }
                        chatUserIds.forEach { userId ->
                            val userSnapshot = dataSnapshot.child(userId)
                            val user = userSnapshot.child("UserInfo").getValue(User::class.java)
                            if (user != null && usersList.none { it.email == user.email }) {
                                usersList.add(user)
                            }
                        }
                    }
                }
                runOnUiThread {
                    adapter_com.updateusers(usersList)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }
}