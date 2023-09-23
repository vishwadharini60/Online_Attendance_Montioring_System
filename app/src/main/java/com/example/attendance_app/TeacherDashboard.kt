package com.example.attendance_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.User

class TeacherDashboard : AppCompatActivity() {

    private lateinit var listview: ListView


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_dashboard)

        val id = intent.getStringExtra("id")
        val teachid=id      //storing a copy of id
        //Toast.makeText(this, "id:$id", Toast.LENGTH_SHORT).show()


        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val reference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("teachers").child(id!!)
        val classref:DatabaseReference=FirebaseDatabase.getInstance().reference.child("teachers").child(id!!).child("sem")

        val myList = ArrayList<String>()
        listview = findViewById(R.id.listView)


        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val uidValue = snapshot.child("uid").value

                //to verify if the the ID belongs to the same user or not
                if (uid == uidValue) {
                    Toast.makeText(this@TeacherDashboard, "verified", Toast.LENGTH_SHORT).show()

                    val eventListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            dataSnapshot.children.forEach { childSnapshot ->
                                val value = childSnapshot.value as? String
                                value?.let {
                                    myList.add(it)
                                }
                            }

                            // Set up the adapter and attach it to the ListView
                            val adapter = ArrayAdapter(this@TeacherDashboard, android.R.layout.simple_list_item_1, myList)
                            listview.adapter = adapter

                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    }
                    classref.addListenerForSingleValueEvent(eventListener)

                    // Handling Item listener for list
                    listview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                        val selectedClass: String = listview.getItemAtPosition(position) as String
                        // pass selectedItem to the next activity

                            val intent= Intent(this@TeacherDashboard,TeacherActivity::class.java)
                            intent.putExtra("selectedClass",selectedClass)
                            intent.putExtra("teachid",teachid)
                            startActivity(intent)
                            finish()
                           // Toast.makeText(this@TeacherDashboard, selectedClass, Toast.LENGTH_SHORT).show()
                        }
                }
                else {
                    //if incorrect ID jump to login activity
                    Toast.makeText(this@TeacherDashboard, "invalid", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@TeacherDashboard, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }
}
