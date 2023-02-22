package com.example.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.firebase.data.Comments


class CommentsAdapter (val context: Context, val commentsList: ArrayList<Comments>):RecyclerView.Adapter<CommentsAdapter.commentViewholder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): commentViewholder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.comment_holder,parent,false)
        return commentViewholder(view)

    }


    override fun onBindViewHolder(holder: commentViewholder, position: Int) {
        val commentsData = commentsList[position]

        holder.sentComment.text=commentsData.comment

    }

    override fun getItemCount(): Int {
       return commentsList.size
    }



    class commentViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //map the items in the sent message layout

        val sentComment=itemView.findViewById<TextView>(R.id.commentTxt)


    }




}