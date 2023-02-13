package com.example.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    //add firebase auth
    // Google auth
    //Save the user login state
    //Log out the user when   they request to log out
    //Google auth --- use-prompt user  for email password
    private lateinit var NewUserRegisterBtn : Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        NewUserRegisterBtn=findViewById(R.id.NewUserRegisterBtn)



        NewUserRegisterBtn.setOnClickListener {
            val intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)

        }





//End of onCreate Method
    }
}