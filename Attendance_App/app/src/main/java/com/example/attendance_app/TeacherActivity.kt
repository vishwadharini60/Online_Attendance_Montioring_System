package com.example.attendance_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TeacherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        val id= intent.getStringExtra("id")

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val email = user?.email
        val database = FirebaseDatabase.getInstance()
// Create a reference to the "students" node
        val teachRef = database.reference.child("teachers").child(id!!)

        //Create a child node for user information
        //val userRef = teachRef.child(uid!!)
        val userMap = HashMap<String, Any>()
        userMap["uid"] = uid!!
        userMap["email"] = email!!
        teachRef.updateChildren(userMap)

        // Create a child node for the list of integers
        /*val classRef = teachRef.child("sem")
        val values = sid!!
        classRef.setValue(values)*/



        /*userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val name = dataSnapshot.child("sem").value as String
                    val textView = findViewById<TextView>(R.id.textView)
                    textView.text = name
                }
            }




        })*/
    }

    }





