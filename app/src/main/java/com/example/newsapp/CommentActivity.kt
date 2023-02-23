package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.CommentsAdapter
import com.example.newsapp.firebase.data.Comments
import com.squareup.picasso.Picasso
import java.time.Duration
import java.time.Instant
import java.time.ZoneId


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
    private lateinit var news_titleComent: TextView
    private lateinit var imgComent: ImageView
    private lateinit var news_publication_timeComent: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        recyclerComments=findViewById(R.id.recyclerComments)
        commentTextContainer=findViewById(R.id.commentTextContainer)
        SendComment=findViewById(R.id.SendComment)

        //fetch  data  from the intent
        news_titleComent=findViewById(R.id.news_titleComent)
        imgComent=findViewById(R.id.imgComent)
        news_publication_timeComent=findViewById(R.id.news_publication_timeComent)


        val headline=intent.getStringExtra("headline")
        val picture=intent.getStringExtra("image")
        val timepublished=intent.getStringExtra("time")

        //use the local time to calculate the time difference

        val currentTimeInHours = Instant.now().atZone(ZoneId.of("Asia/Kolkata"))
        val newsTimeInHours = Instant.parse(timepublished).atZone(ZoneId.of("Asia/Kolkata"))
        val hoursDifference = Duration.between(currentTimeInHours, newsTimeInHours)
        val hoursAgo = " " + hoursDifference.toHours().toString().substring(1) + " hour ago"


        news_titleComent.text=headline.toString()

        news_publication_timeComent.text=hoursAgo

        //loading the image
        Picasso.get()
            .load(picture)
            .fit()
            .centerCrop()
            .error(R.drawable.nopic)
            .into(imgComent)



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