package com.example.attendance_app

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.Manifest
import android.content.Intent

import android.telephony.SmsManager
import android.text.TextUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AbsentListActivity : AppCompatActivity() {

    private lateinit var ablistview: ListView
    private lateinit var msgBtn:Button
    private lateinit var logoutbtn:Button
    private lateinit var date: TextView
    private var teachsub: Any? = null
    private var clssub: Any? = null
    private var attendence:Int=0
    private var clsattendence:Int=0
    private var phone:CharSequence=""

    private val permissionRequest = 901


    val studentAbList = ArrayList<String>()
    val studentPList = ArrayList<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {


        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formattedDateTime = currentDateTime.format(formatter)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absent_list)

        msgBtn=findViewById(R.id.msgBtn)

        //Logout button
        logoutbtn=findViewById(R.id.logoutbtn)
        logoutbtn.setOnClickListener {
            logout()
        }

        // receiving a array list from prev activity

        val receivedList = intent.getStringArrayListExtra("absent")
        val rcvdpresentList = intent.getStringArrayListExtra("present")
        val id = intent.getStringExtra("id")
        val cls=intent.getStringExtra("cls")
        //Toast.makeText(this@AbsentListActivity, cls.toString(), Toast.LENGTH_SHORT).show()

        val teachref:DatabaseReference= FirebaseDatabase.getInstance().reference.child("teachers").child(id!!)
        val classref: DatabaseReference = FirebaseDatabase.getInstance().reference.child(cls!!).child("sub")

        ablistview = findViewById(R.id.absentlist)
        date = findViewById(R.id.datetime)

        //setting time
        date.text = formattedDateTime


        //retrieving and updating value of subject of teacher
        teachref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                teachsub = snapshot.child("subject").value
                //Toast.makeText(this@AbsentListActivity, sub.toString(), Toast.LENGTH_SHORT).show()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

         //retrieving  and updating value of subject of class
            val classeventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                clssub = snapshot.child(teachsub!!.toString()).value
                clsattendence = clssub.toInt()
                clsattendence = clsattendence + 1
                val subref: DatabaseReference =FirebaseDatabase.getInstance().reference.child(cls!!).child("sub").child(teachsub!!.toString())

                subref.setValue(clsattendence)
                //Toast.makeText(this@AbsentListActivity, clsattendence.toString(), Toast.LENGTH_SHORT).show()
            }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
            }
        }
        classref.addListenerForSingleValueEvent(classeventListener)

        //making copies of the absent and present list
        for (item in receivedList!!) {
            studentAbList.add(item)
        }
        for (item in rcvdpresentList!!) {
            studentPList.add(item)
        }

        //updating values of the present student
        for(usn in studentPList!!) {
            val studref: DatabaseReference = FirebaseDatabase.getInstance().reference.child("students").child(usn!!).child("sub")

            val eventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val currstudent = snapshot.child(teachsub!!.toString()).value
                    attendence = currstudent.toInt()
                    attendence = attendence + 1

                    // updating values
                    val clsref: DatabaseReference = FirebaseDatabase.getInstance().reference.child("students").child(usn!!).child("sub").child(teachsub!!.toString())

                    // Insert the attendance data into Firebase
                    clsref.setValue(attendence)
                    //Toast.makeText(this@AbsentListActivity, attendence.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle cancellation
                }
            }
            studref.addListenerForSingleValueEvent(eventListener)
        }



        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, studentAbList)
        ablistview.adapter = adapter


        fun sendMessage(number:CharSequence)
        {
            //fetching the number from edit text field
            //val number="589621"
            //fetching your message from edit text
            val msg="YOUR WARD IS ABSENT FOR THE PRESENT SESSION"

            if (number == "" || msg == "")
            {
                //if number or message is empty
                Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
            }
            else
            {
                //if both number and message is not empty
                if (TextUtils.isDigitsOnly(number))
                {
                    val phno=number.toString()
                    //if number contains only digits
                    //creating sms manager object
                    val smsManager: SmsManager = SmsManager.getDefault()
                    //use sms manager api to send the message
                    smsManager.sendTextMessage(phno , null, msg, null, null)
                    //if the message is sent successfully then show toast
                    Toast.makeText(this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    //if number has other than digits then show wrong number
                    Toast.makeText(this, "Wrong Number!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults:
         IntArray)
         {
             super.onRequestPermissionsResult(requestCode, permissions, grantResults)

             //checking if the permissions are granted
             if (requestCode == permissionRequest) {
                 if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                     sendMessage(phone)
                 } else {
                     Toast.makeText(this, "Please provide permission",
                         Toast.LENGTH_SHORT).show()
                 }
             }
         }


        fun sendSMS() {
              //when you press the button
              val permissionCheck =
                  ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
              if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                  //if permission is granted, then send the message
                  sendMessage(phone)
              } else {
                  //if not granted, then seek again
                  ActivityCompat.requestPermissions(
                      this,
                      arrayOf(Manifest.permission.SEND_SMS),
                      permissionRequest
                  )
              }

          }

        //sending message
        msgBtn.setOnClickListener{

            for(musn in studentAbList) {
                val mref: DatabaseReference =
                    FirebaseDatabase.getInstance().reference.child("students").child(musn!!)

                val meventListener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val currphno = snapshot.child("phno").value
                        phone=currphno.toString()
                        sendSMS()
                        //Toast.makeText(this@AbsentListActivity, phone.toString(), Toast.LENGTH_SHORT).show()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle cancellation
                    }
                }
                mref.addListenerForSingleValueEvent(meventListener)

            }
        }

    }

    private fun logout() {
        // Start a new activity, such as a login activity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close the current activity to prevent going back to it after logout
    }
}

//converts string to int
private fun Any?.toInt(): Int {
    return this?.toString()?.toIntOrNull() ?: 0
}











