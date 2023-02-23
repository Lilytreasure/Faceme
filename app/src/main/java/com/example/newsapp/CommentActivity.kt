package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.CommentsAdapter
import com.example.newsapp.adapters.ContactsAdapter
import com.example.newsapp.firebase.data.Comments
import com.example.newsapp.firebase.data.User

class CommentActivity : AppCompatActivity() {
    //add the recyclerView
    //send comment button
    //commentholder
    //Show the  comment and the name of the  user who have submitted the comment
    //add data to the recycler view

    private lateinit var recyclerComments: RecyclerView
    private lateinit var  commentTextContainer: EditText
    private lateinit var SendComment: ImageButton
    private  lateinit var commentsAdapter: CommentsAdapter
    private lateinit var comments: ArrayList<Comments>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        recyclerComments=findViewById(R.id.recyclerComments)
        commentTextContainer=findViewById(R.id.commentTextContainer)
        SendComment=findViewById(R.id.SendComment)

       val data = ArrayList<Comments>()

       // comments= ArrayList()
        recyclerComments.layoutManager= LinearLayoutManager(this)


     SendComment.setOnClickListener {

         val commentD: String=commentTextContainer.text.toString()
         val data1=Comments(commentD,"2")
         data.add(data1)


         commentsAdapter= CommentsAdapter(this@CommentActivity,data)
         recyclerComments.adapter=commentsAdapter
         commentsAdapter.notifyDataSetChanged()


     }







        //end of the onCreate method
    }
}