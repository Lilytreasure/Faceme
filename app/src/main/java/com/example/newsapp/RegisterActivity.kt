package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RegisterActivity : AppCompatActivity() {

    // add a  model class to collect the log in credentials
    //validated using firebase authorization
    //enable username and password authentication

    //Todo-- Add the password confrim
    //Todo--reject entries that do  not follow  the email policy








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
}