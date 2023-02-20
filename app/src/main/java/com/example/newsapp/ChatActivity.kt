package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.MessageAdapter
import com.example.newsapp.firebase.data.Message
import com.example.newsapp.firebase.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    //this activity will populate  the actual chats
    //User choose the subjects from the list of registered users
    //show the sent and  the unsent messages
    //Sent and received messages container have different shading to differentiate
    //allow sender to resend failed messages

    private  lateinit var recyclerChats: RecyclerView
    private lateinit var SendBTN: ImageButton
    private lateinit var textContainer: EditText
    private lateinit var  userMsgName: Button
    private lateinit var  messageAdapter:  MessageAdapter
    private lateinit var messageList: ArrayList<Message>

    private lateinit var mDbRef: DatabaseReference

    // This valuables are mutable  as users are free to add messages as they wish to
    var senderRoom: String?=null
    var receiverRoom: String?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerChats=findViewById(R.id.recyclerChats)
        SendBTN=findViewById(R.id.SendBTN)
        textContainer=findViewById(R.id.textContainer)
        userMsgName=findViewById(R.id.userMsgName)



    //get data from the intent passed from the Contacts adapter
        val userId=intent.getStringExtra("name")
        userMsgName.text=userId

        //this is the subject who is going  to receive the message
        val receiverUid=intent.getStringExtra("uid")
        //add the SenderUid
        val senderUid=FirebaseAuth.getInstance().currentUser?.uid
        //instantiate the database reference
        mDbRef=FirebaseDatabase.getInstance().getReference()




        //The sender room will have both the senderUid and  the receiverUid
        //the messages will be stored in unique rooms--sender and receiver room
        senderRoom=receiverUid+senderUid
        receiverRoom=senderUid+receiverUid


        messageList= ArrayList()

        val data = ArrayList<Message>()


        //set the adapter  to the recyclerView
        messageAdapter=MessageAdapter(this,data)
        //adding message data  to the recyclerView
        recyclerChats.layoutManager=LinearLayoutManager(this)
        recyclerChats.adapter=messageAdapter



        //Data will be fetched from firebase --Fetch data logic
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //traverse through all  the children in snapshot using the  for loop

                    //clear the data list to avoid duplication
                    data.clear()

                    for (postSnapshot in snapshot.children){
                        val message=postSnapshot.getValue(Message::class.java)
                        data.add(message!!)

                    }
                    messageAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {


                }


            })




//write  to firebase
        SendBTN.setOnClickListener {
          val message=textContainer.text.toString()
            //The message has two parameters -The message itself and the senderId
            val messageOb=Message(message,senderUid)

            //create a chats node that will hold the chats data
            //The chats node will have a child sender and receiver room that will hold the message data

            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageOb).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageOb)
                }
//clear the text container after sending the message
            textContainer.setText("")

        }




        //end of the onCreate  method
    }
}