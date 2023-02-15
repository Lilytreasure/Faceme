package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.ContactsAdapter
import com.example.newsapp.firebase.User

class MessagesActivity : AppCompatActivity() {
    //add a container to hold the user contacts
    //add a container with a recycler view to allow the actual sending of the messages
    //populate the sent messages on a recyclerview
    //for a successful sent message add right mark
    //on error sending the message --log an error on the message that has not been sent
    //Allow the user to resend the messages that has failed
    private lateinit var  recyclerMessages: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: ContactsAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        recyclerMessages=findViewById(R.id.recyclerMessages)


        userList=ArrayList()
        adapter=ContactsAdapter(this,userList)









//end of OnCreate method
    }

}