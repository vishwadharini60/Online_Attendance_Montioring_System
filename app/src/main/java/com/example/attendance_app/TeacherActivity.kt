package com.example.attendance_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*


class TeacherActivity : AppCompatActivity() {

    private lateinit var listview: ListView
    private lateinit var submit: Button
    val abList = ArrayList<String>()
    val presentList = ArrayList<String>()
    val arrayList = ArrayList<String>()
    private var sub: Any? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        var cls = intent.getStringExtra("selectedClass")
        val id = intent.getStringExtra("teachid")
        //Toast.makeText(this, "id:$id", Toast.LENGTH_SHORT).show()


        listview = findViewById(R.id.listView)
        submit=findViewById(R.id.submitBtn)


        val ref = FirebaseDatabase.getInstance().reference.child(cls!!)

        val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach { childSnapshot ->
                    val value = childSnapshot.value as? String
                    value?.let {
                        arrayList.add(it)
                    }
                }

                // Set up the adapter and attach it to the ListView
                val adapter = MyAdapter(this@TeacherActivity, arrayList)

                adapter.setAdapterCallback(object : MyAdapter.AdapterCallback {
                    override fun onButtonClicked(value: String) {
                        // Handle the button click event here
                        // You can access the clicked value (str) and perform any necessary actions
                        Toast.makeText(this@TeacherActivity, value, Toast.LENGTH_SHORT).show()
                        abList.add(value)

                    }
                })

                listview.adapter = adapter



                submit.setOnClickListener{
                    for(student in arrayList) {
                        for(abstudent in abList){
                            if(student!=abstudent){
                                presentList.add(student)
                            }
                        }
                    }
                    val intent = Intent(this@TeacherActivity,AbsentListActivity::class.java)
                    intent.putStringArrayListExtra("absent",abList)
                    intent.putStringArrayListExtra("present",presentList)
                    intent.putExtra("id",id)
                    intent.putExtra("cls",cls)
                    startActivity(intent)
                    finish()

                }



            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        ref.addListenerForSingleValueEvent(eventListener)
    }

}


       /* //Create a child node for user information
        val userMap = HashMap<String, Any>()
        userMap["uid"] = uid!!
        userMap["email"] = email!!
        teachRef.updateChildren(userMap)*/


        /*userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val name = dataSnapshot.child("sem").value as String
                    val textView = findViewById<TextView>(R.id.textView)
                    textView.text = name
                }
            }
        })*/










