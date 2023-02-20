package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

class ChatActivity : AppCompatActivity() {

    //this activity will populate  the actual chats
    //User choose the subjects from the list of registered users
    //show the sent and  the unsent messages
    //Sent and received messages container have different shading to differentiate
    //allow sender to resend failed messages

    private  lateinit var recyclerChats: RecyclerView
    private lateinit var SendBTN: Button
    private lateinit var textContainer: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerChats=findViewById(R.id.recyclerChats)
        SendBTN=findViewById(R.id.SendBTN)
        textContainer=findViewById(R.id.textContainer)





        //end of the onCreate  method
    }
}