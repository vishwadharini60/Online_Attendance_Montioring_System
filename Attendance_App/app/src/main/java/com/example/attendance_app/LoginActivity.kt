package com.example.attendance_app

import android.R.layout.simple_spinner_dropdown_item
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth



class LoginActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    //val database=FirebaseDatabase.getInstance().reference
    private val auth: FirebaseAuth = Firebase.auth
    private val items= arrayOf("Select role","Student","Teacher")      //spinner content

    private lateinit var uid: TextInputLayout
    private lateinit var username: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var loginbtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //spinner
        val spinner=findViewById<Spinner>(R.id.choose)
        spinner.onItemSelectedListener=this
        val arrayAdapter = ArrayAdapter(this, simple_spinner_dropdown_item, items)
        spinner.adapter=arrayAdapter


        //setting the views
        uid=findViewById(R.id.uid)
        username=findViewById(R.id.username)
        password=findViewById(R.id.password)
        loginbtn=findViewById(R.id.login)

        //login button action
        loginbtn.setOnClickListener {
            val selectedRole = spinner.selectedItemPosition
            if (selectedRole == 0||username.editText?.text.isNullOrEmpty() || password.editText?.text.isNullOrEmpty()) {
                    Toast.makeText(this, "enter all fields", Toast.LENGTH_SHORT).show()
                }
            else {
                val user = username.editText?.text.toString()
                val pass = password.editText?.text.toString()
                credentials(user, pass)
            }
        }
    }

    private fun credentials(user: String, pass: String){

        val spinner=findViewById<Spinner>(R.id.choose)
        val spinnerValue=extractSelectedValueFromSpinner(spinner)

        //authentication
        auth.signInWithEmailAndPassword(user, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "logged in", Toast.LENGTH_SHORT).show()

                    val id=uid.editText?.text.toString()
                    if(spinnerValue=="Student") {
                        val intent = Intent(this, StudentActivity::class.java)
                        intent.putExtra("id",id)             //sending uid to next activity
                        startActivity(intent)
                        finish()
                    }
                    else if(spinnerValue=="Teacher")
                    {
                        val intent = Intent(this, TeacherActivity::class.java)
                        intent.putExtra("id",id)
                        startActivity(intent)
                        finish()
                    }
                }
                else {
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT,).show()

                }
            }
        }

    //To get selected value in spinner
    private fun extractSelectedValueFromSpinner(spinner: Spinner): String {
        val selectedRole = spinner.selectedItemPosition
        return spinner.getItemAtPosition(selectedRole).toString()
    }

    //spinner onItemSelected function
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}