package com.example.newsapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.ChatActivity
import com.example.newsapp.R
import com.example.newsapp.firebase.data.User

class ContactsAdapter(val context: Context,val  userList: ArrayList<User>):RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view: View =LayoutInflater.from(context).inflate(R.layout.users_contacts,parent,false)
        return  ContactsViewHolder(view)

    }



    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val currentUser=userList[position]
        holder.textName.text=currentUser.name


        holder.textName.setOnClickListener {
            //This will reference to the populate registered user accounts from firebase
           // Toast.makeText(context, "contact clicked", Toast.LENGTH_SHORT).show()

            //start the chat activity and take current username and the uid of the  current user

            val intent=Intent(context,ChatActivity::class.java)
            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)
            context.startActivity(intent)


        }


    }

    override fun getItemCount(): Int {
        return  userList.size
    }


    class ContactsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        //map the container in  the user contacts
        var textName=itemView.findViewById<Button>(R.id.textContact)


    }


}