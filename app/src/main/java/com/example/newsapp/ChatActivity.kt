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
import ru.nikartm.support.ImageBadgeView

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

    private  lateinit var imageBadgeViewMessage: ImageBadgeView

    // This valuables are mutable  as users are free to add messages as they wish to
    var senderRoom: String?=null
    var receiverRoom: String?=null

    //check whether user has registered a  new chat
    private lateinit var mAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerChats=findViewById(R.id.recyclerChats)
        SendBTN=findViewById(R.id.SendBTN)
        textContainer=findViewById(R.id.textContainer)
        userMsgName=findViewById(R.id.userMsgName)
        imageBadgeViewMessage=findViewById(R.id.badgeMessage)

        //firebase auth instance
        mAuth= FirebaseAuth.getInstance()



        //update the badge count when firebase register a new  message item

        var messageNotificationCount=0

        imageBadgeViewMessage.setOnClickListener {
            imageBadgeViewMessage.badgeValue=0

        }



    //get data from the intent passed from the Contacts adapter
        val userId=intent.getStringExtra("name")
        userMsgName.text=userId

        //this is the subject who is going  to receive the message
        //gets  the uid of the currently selected receiver

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
        val data2=ArrayList<User>()


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
                    //when  the data has changed in the firebase
                    //only those users who  have a ne message will have a  new message notification

                    //clear the data list to avoid duplication

                    data.clear()

                    for (postSnapshot in snapshot.children){
                        val message=postSnapshot.getValue(Message::class.java)
                        data.add(message!!)

                        //when  a new message is added to the list
                        //increment the value of the badgeCount
                        //store the number of the sent messages an show the count in badge
                        //only show notifications on the update user id chats
                        //updates are only showed when specific uid has received an update
                        //the  count is populated in the receiver uid
                        //from the sender end the count is not incremented


                    }


                        messageNotificationCount++

                        imageBadgeViewMessage.badgeValue=messageNotificationCount

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