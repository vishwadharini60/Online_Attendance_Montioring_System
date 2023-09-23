package com.example.attendance_app

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        val id= intent.getStringExtra("id")

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val email = user?.email
       // val usn=id??
        val database = FirebaseDatabase.getInstance()

        //for creating entries in database
        // Create a child node for user information
        val studentsRef = database.reference.child("students").child(id!!)
        val userMap = HashMap<String, Any>()
        userMap["uid"] = uid!!
        userMap["email"]=email!!
        studentsRef.updateChildren(userMap)



        /*val ref = database.reference

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    //val value = dataSnapshot.getValue(String::class.java)
                    // Handle the retrieved value here
                   val key=dataSnapshot.getValue("usn")
            } else {
                    // Data does not exist at the specified location
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any error that occurred
                Log.e(TAG, "Data retrieval cancelled: ${databaseError.message}")
            }
        })*/



    }
}