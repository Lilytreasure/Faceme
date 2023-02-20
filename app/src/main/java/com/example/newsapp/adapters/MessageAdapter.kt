package com.example.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.firebase.data.Message
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context,val messageList: ArrayList<Message>) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    //create separate identifiers to  choose the  layout to inflate
    //only inflate the layout of the selected  id
    val ITEM_SENT=1
    val ITEM_Recived=2



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
        val currentMessage=messageList[position]

        if (holder.javaClass==sentViewholder::class.java){

            //map the items for the sentViewHolder
            //type cast  the viewHolder
            val viewholder=holder as sentViewholder
            holder.sentMessage.text=currentMessage.message


        }else{

            //map the items for the receivedViewHolder
            val viewholder=holder as receivedViewholder

            holder.recivedMessage.text=currentMessage.message


        }


    }


    override fun getItemViewType(position: Int): Int {

        val currentMessage=messageList[position]

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