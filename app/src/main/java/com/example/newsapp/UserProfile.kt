package com.example.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class UserProfile : AppCompatActivity() {

    //Add listeners to various elements in the items

    private lateinit var  LogoutCard: CardView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)



        LogoutCard=findViewById(R.id.LogoutCard)


        LogoutCard.setOnClickListener {
            val intent=Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }









        //end of the onCreate Method
    }
}