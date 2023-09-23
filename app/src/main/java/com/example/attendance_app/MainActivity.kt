package com.example.attendance_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.os.Handler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN    //Hides the status bar
        setContentView(R.layout.activity_main)

        //Animations
        val topAnim=AnimationUtils.loadAnimation(this,R.anim.top_animation)         //loading the animation ie xml files
        val bottomAnim=AnimationUtils.loadAnimation(this,R.anim.bottom_animation)
        val appName = findViewById<TextView>(R.id.appName)
        val logo = findViewById<ImageView>(R.id.logo)
        logo.startAnimation(topAnim)                        //setting animation to desired xml components
        appName.startAnimation(bottomAnim)

        //Calling the next activity(login page) after a delay of 5000ms
       Handler().postDelayed({
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)        //start the login activity
            finish()                    //finish current activity
        },5000)

    }
}