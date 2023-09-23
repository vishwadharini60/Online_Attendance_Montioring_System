package com.example.attendance_app

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.attendance_app.R.id.studentname
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.security.auth.Subject

class StudentActivity : AppCompatActivity() {

    private lateinit var name:TextView
    private lateinit var usn:TextView
    private lateinit var phno:TextView
    private lateinit var logout:Button

    private lateinit var percentage1: TextView
    private lateinit var present1: TextView
    private lateinit var total1:TextView

    private lateinit var percentage2: TextView
    private lateinit var present2: TextView
    private lateinit var total2:TextView

    private lateinit var percentage3: TextView
    private lateinit var present3: TextView
    private lateinit var total3:TextView

    private var co:Int=0
    private var iot:Int=0
    private var math:Int=0

    private var totalco:Int=0
    private var totaliot:Int=0
    private var totalmath:Int=0

    private var p1:Int=0
    private var p2:Int=0
    private var p3:Int=0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        //Logout button
        logout=findViewById(R.id.logoutButton)
        logout.setOnClickListener {
            logout()
        }


        name=findViewById(R.id.studentname)
        usn=findViewById(R.id.usn)
        phno=findViewById(R.id.phno)


        percentage1=findViewById(R.id.percentage1)
        present1=findViewById(R.id.present1)
        total1=findViewById(R.id.total1)

        percentage2=findViewById(R.id.percentage2)
        present2=findViewById(R.id.present2)
        total2=findViewById(R.id.total2)

        percentage3=findViewById(R.id.percentage3)
        present3=findViewById(R.id.present3)
        total3=findViewById(R.id.total3)


        val user = FirebaseAuth.getInstance().currentUser                 //getting details of current logged in user
        val id=intent.getStringExtra("id")
        val uid = user?.uid
        val reference:DatabaseReference= FirebaseDatabase.getInstance().reference.child("students").child(id!!)

        val studentref:DatabaseReference= FirebaseDatabase.getInstance().reference.child("students").child(id!!).child("sub")

        val classref:DatabaseReference= FirebaseDatabase.getInstance().reference.child("6C").child("sub")


        reference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val uidValue = snapshot.child("uid").value
                val nameValue=snapshot.child("name").value
                val emailValue=snapshot.child("email").value


                //to verify if the the ID belongs to the same user or not
                if(uid==uidValue) {
                        Toast.makeText(this@StudentActivity, "verified", Toast.LENGTH_SHORT).show()
                    name.text = nameValue.toString()
                    usn.text = id.toString()
                    phno.text = emailValue.toString()

                    val subListener = object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            val sub1 = snapshot.child("co").value
                             co = sub1.toInt()
                            present1.text=co.toString()

                            val sub2 = snapshot.child("iot").value
                            iot = sub2.toInt()
                            present2.text=iot.toString()

                            val sub3 = snapshot.child("math").value
                             math = sub3.toInt()
                            present3.text=math.toString()


                        //Toast.makeText(this@StudentActivity, mathattend.toString(), Toast.LENGTH_SHORT).show()
                        }
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    }
                    studentref.addListenerForSingleValueEvent(subListener)

                    val clsListener = object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val clsub1 = snapshot.child("co").value
                            totalco = clsub1.toInt()
                            total1.text=totalco.toString()

                            val clsub2 = snapshot.child("iot").value
                            totaliot = clsub2.toInt()
                            total2.text=totaliot.toString()

                            val clsub3 = snapshot.child("math").value
                            totalmath = clsub3.toInt()
                            total3.text=totalmath.toString()
                            //Toast.makeText(this@StudentActivity, mathattend.toString(), Toast.LENGTH_SHORT).show()

                            p1 = calculatePercentage(co, totalco)
                            percentage1.text = p1.toString()

                            p2 = calculatePercentage(iot, totaliot)
                            percentage2.text = p2.toString()

                            p3 = calculatePercentage(math, totalmath)
                            percentage3.text = p3.toString()

                        }
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    }
                    classref.addListenerForSingleValueEvent(clsListener)

                }
                else{
                    //if incorrect ID jump to login activity
                        Toast.makeText(this@StudentActivity, "invalid", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@StudentActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun logout() {
        // Start a new activity, such as a login activity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close the current activity to prevent going back to it after logout
    }
}

private fun Any?.toInt(): Int {
    return this?.toString()?.toIntOrNull() ?: 0
}

    private fun calculatePercentage(attended: Int, total: Int): Int {
        return if (total != 0) {
            (attended.toFloat() / total.toFloat() * 100).toInt()
        } else {
            0
        }
    }





        /*val id= intent.getStringExtra("id")

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val email = user?.email

        val database = FirebaseDatabase.getInstance()

       //for creating entries in database
        // Create a child node for user information
        val studentsRef = database.reference.child("students").child(id!!)
        val userMap = HashMap<String, Any>()
        userMap["uid"] = uid!!
        userMap["email"]=email!!
        studentsRef.updateChildren(userMap)*/






