package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.CommentsAdapter
import com.example.newsapp.adapters.ContactsAdapter
import com.example.newsapp.firebase.data.Comments
import com.example.newsapp.firebase.data.Message
import com.example.newsapp.firebase.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
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

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var userList: ArrayList<User>

    private var comentSender:String=""



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


        //firebase modules
        mAuth= FirebaseAuth.getInstance()

        val senderUid=FirebaseAuth.getInstance().currentUser?.uid


        mDbRef=FirebaseDatabase.getInstance().getReference()
       // val sendername=FirebaseAuth.getInstance().currentUser?.email


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

        val nameData=ArrayList<User>()



        commentsAdapter= CommentsAdapter(this@CommentActivity,data)
        recyclerComments.adapter=commentsAdapter





       // comments= ArrayList()
       // recyclerComments.layoutManager= LinearLayoutManager(this)


        //add the comments firebase on a different node and fetch the comments made on a certain item




         //add the  comments on a spare node identified  by the uid
        //add the name of the user
        //det the name of the user who submitted the comment
        mDbRef.child("credentials").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                //traverse through the items in the database to sort out individual items
                for (postSnapshot in snapshot.children){
                    //clear the list before adding values to avoid duplication when
                    //data set is changed

                    nameData.clear()
                    val currentUser=postSnapshot.getValue(User::class.java)

                    nameData.add(currentUser!!)
                    if (currentUser.uid==senderUid){

                        comentSender=currentUser.name.toString()

//                        for (nameslisted in nameData){
//                            comentSender= nameslisted.name.toString()
//
//                        }

                       // println(comentSender+ "name of the sender ")

                    }



                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })



        //add the name of the comment sender and the comment
        //put the comments in firebase


     SendComment.setOnClickListener {
         //obtain the user name



         val commentD: String=commentTextContainer.text.toString()
//         val data1=Comments(commentD,"2")
//         data.add(data1)

         val commentOb=Comments(commentD,comentSender)

         try {

             mDbRef.child("Comments").child(senderUid!!).child("sentComment").push().setValue(commentOb)

             //clear the comment text container when the comment is successfully sent

             commentTextContainer.setText("")


         }catch (e: Exception){

             println("an error occured")

         }


//
//         commentsAdapter= CommentsAdapter(this@CommentActivity,data)
//         recyclerComments.adapter=commentsAdapter
//         commentsAdapter.notifyDataSetChanged()


     }
        //Add the comments from firebase in the comments container
        //add  data--in the  Comments list

        mDbRef.child("Comments").child("sentComment")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //traverse through all  the children in snapshot using the  for loop
                    //when  the data has changed in the firebase
                    //only those users who  have a ne message will have a  new message notification

                    //clear the data list to avoid duplication

                    data.clear()

                    for (postSnapshot in snapshot.children){
                        val comments=postSnapshot.getValue(Comments::class.java)
                        data.add(comments!!)

                    }

                    commentsAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })





        //end of the onCreate method
    }
}