package com.example.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.firebase.data.Message
import com.example.newsapp.firebase.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageAdapter(val context: Context,val messageList: ArrayList<Message>) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    //create separate identifiers to  choose the  layout to inflate
    //only inflate the layout of the selected  id
    val ITEM_SENT=1
    val ITEM_Recived=2

    private lateinit var mDbRef: DatabaseReference

    var senderRoom: String?=null
    var receiverRoom: String?=null
    private lateinit var mAuth: FirebaseAuth
  private  lateinit var user:ArrayList<User>

  

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //attach the layout according to the viewHolder
        //ie -- The sent and receive message layout
        //inflate the  layout according to the viewtype
        //getItemViewType takes integer variables to uniquely identify  the different view Types
        if (viewType==1){
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent_message,parent,false)
            return sentViewholder(view)

        }else{

            val view: View = LayoutInflater.from(context).inflate(R.layout.received_message,parent,false)
            return receivedViewholder(view)

        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        //use the if  block to sort out  between the holders
        //ie -- sent  and the received holder
        //map the state of the message
        //either sent  or not sent if fails during send
        val currentmessage=messageList[position]

        user=ArrayList()

        val userdata=ArrayList<User>()




        mDbRef=FirebaseDatabase.getInstance().getReference()
        mAuth= FirebaseAuth.getInstance()

        val data = ArrayList<Message>()

        val senderUid=FirebaseAuth.getInstance().currentUser?.uid
      val   receiverUid= mAuth.currentUser?.uid.toString()


      //  val receiverUid=currentUser.uid
        val messageuid=currentmessage.senderId



       senderRoom=receiverUid+senderUid
        receiverRoom=senderUid+receiverUid




        val builder=AlertDialog.Builder(context)
        builder.setTitle("Delete message")
        //proceed to delete
        builder.setPositiveButton("Yes"){dialogInterface, which->

            //listen to firebase and delete the selected message by the message id
            //get the uid from  the  database
            //get the message


            mDbRef.child("chats").child(senderRoom!!).child("messages")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        //traverse through all  the children in snapshot using the  for loop
                        //when  the data has changed in the firebase
                        //only those users who  have a ne message will have a  new message notification

                        //clear the data list to avoid duplication

                        data.clear()

                        for (postSnapshot in snapshot.children){
                            val message=postSnapshot.getValue(Message::class.java)
                            data.add(message!!)
                            if (message.senderId==messageuid){
                                postSnapshot.ref.removeValue()

                            }


                        }


                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })


            Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show()

        }
        //cancel the delete
        builder.setNeutralButton("Cancel"){dialogInterface, which->
            Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show()

        }

        //assert not
        builder.setNegativeButton("No"){dialogInterface,which->
            Toast.makeText(context, "delete canceled", Toast.LENGTH_SHORT).show()

        }

        val alertDialog:AlertDialog=builder.create()
        alertDialog.setCancelable(false)









//end of dialog
        val currentMessage=messageList[position]

        if (holder.javaClass==sentViewholder::class.java){

            //map the items for the sentViewHolder
            //type cast  the viewHolder
            val viewholder=holder as sentViewholder


            holder.sentMessage.text=currentMessage.message


//delete the sent  message

         holder.sentMessage.setOnClickListener {
             alertDialog.show()
         }



        }else{

            //map the items for the receivedViewHolder
            val viewholder=holder as receivedViewholder

            holder.recivedMessage.text=currentMessage.message

            //delete the  received message

            holder.recivedMessage.setOnClickListener {

                alertDialog.show()
            }


        }











    }




    override fun getItemViewType(position: Int): Int {

        val currentMessage=messageList[position]
        //if the message id corresponds to the currentUserId then the message is identified as sent
        //else of the message has a different id -it is marked as received





        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_Recived
        }

    }



    override fun getItemCount(): Int {
        return messageList.size

    }






    //add  both the sent and the received viewholder on the same adapter and
    //populate the adapter according to their  references



    class sentViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //map the items in the sent message layout

        val sentMessage=itemView.findViewById<TextView>(R.id.SentMessage)


    }



    class receivedViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //map the items in the received message layout


        val recivedMessage=itemView.findViewById<TextView>(R.id.ReceivedMessage)


    }




}