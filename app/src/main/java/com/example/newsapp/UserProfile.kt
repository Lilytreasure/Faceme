package com.example.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth


class UserProfile : AppCompatActivity() {

    //Add listeners to various elements in the items

    private lateinit var  LogoutCard: CardView
    private lateinit var mAuth: FirebaseAuth
    private  lateinit var TxtNameHolder: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        mAuth=FirebaseAuth.getInstance()



        LogoutCard=findViewById(R.id.LogoutCard)
        TxtNameHolder=findViewById(R.id.TxtNameHolder)

        //set the name of the logged user in the textholder
        TxtNameHolder.text= mAuth.currentUser?.email.toString()


        LogoutCard.setOnClickListener {
            mAuth.signOut()
            val intent=Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }









        //end of the onCreate Method
    }
}