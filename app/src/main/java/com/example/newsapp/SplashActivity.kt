package com.example.newsapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    //Entry to splash screen
    //make the default entry screen
    //animate the  splash screen
    private  lateinit var  textViewSplash: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        textViewSplash=findViewById(R.id.textSplash)
        textViewSplash.alpha=0f
        textViewSplash.animate().setDuration(3000).alpha(1f).withEndAction {

            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_in)
            finish()

        }







        //End of the onCretate method
    }
}