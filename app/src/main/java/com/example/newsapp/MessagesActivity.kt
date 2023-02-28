package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.ContactsAdapter
import com.example.newsapp.firebase.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MessagesActivity : AppCompatActivity() {
    //add a container to hold the user contacts
    //add a container with a recycler view to allow the actual sending of the messages
    //populate the sent messages on a recyclerview
    //for a successful sent message add right mark
    //on error sending the message --log an error on the message that has not been sent
    //Allow the user to resend the messages that has failed
    //add a notifire that  shows the number of  the unread messages
    //once the messages are read the notification counter is decremented
    //catch the user contacts and update when new item is added
    //reference the old items with  the new item and only update when new items are found


    private lateinit var  recyclerMessages: RecyclerView
   private lateinit var userList: ArrayList<User>
    private lateinit var contactsAdapter: ContactsAdapter
    private lateinit var mDbRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        recyclerMessages=findViewById(R.id.recyclerMessages)
        mAuth= FirebaseAuth.getInstance()
        mDbRef=FirebaseDatabase.getInstance().getReference()

        userList=ArrayList()

        val data = ArrayList<User>()


        //setting up the recycler view

        recyclerMessages.layoutManager=LinearLayoutManager(this)


        //fetch data from the database

        mDbRef.child("credentials").addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                //traverse through the items in the database to sort out individual items
                for (postSnapshot in snapshot.children){
                    //clear the list before adding values to avoid duplication when
                    //data set is changed

                    userList.clear()
                    val currentUser=postSnapshot.getValue(User::class.java)
                    if (mAuth.currentUser?.uid  != currentUser?.uid){
                        data.add(currentUser!!)

                    }

                }

                contactsAdapter=ContactsAdapter(this@MessagesActivity,data)
                recyclerMessages.adapter=contactsAdapter
                contactsAdapter.notifyDataSetChanged()


            }

            override fun onCancelled(error: DatabaseError) {

            }

        })












//end of OnCreate method
    }

}