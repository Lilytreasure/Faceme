package com.example.newsapp.adapters

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.CommentActivity
import com.example.newsapp.NewsModel
import com.example.newsapp.R
import com.example.newsapp.firebase.data.Comments
import com.example.newsapp.firebase.data.Likes
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.time.Duration
import java.time.Instant
import java.time.ZoneId


class CustomAdapter(private var newsList: List<NewsModel>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    private lateinit var context: Context
    private lateinit var mClickListener: OnItemClickListener
    private lateinit var mLongClickListener: OnItemLongClickListener
   private var count:Int=0
//    private  lateinit var commentsAdapter: CommentsAdapter
//    private lateinit var comments: ArrayList<Comments>
//    val data = ArrayList<Comments>()

    private lateinit var mDbRef: DatabaseReference
    private val commentId:String="01c"
    private var commentsAmmout: Int=0
    val data = ArrayList<Comments>()
    var  hoursAgo=""

    val likesData=ArrayList<Likes>()

    
    init {
        this.notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int)
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        mLongClickListener = listener
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        context = parent.context

        return ViewHolder(view, mClickListener, mLongClickListener)



        //likebtn
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsData = newsList[holder.adapterPosition]

        holder.headLine.text = newsData.headLine
        val time: String? = newsData.time
        val imgUrl = newsData.image
        //Show an error  image  when the news items to don have an item || an error occurs loading the image

        if (imgUrl.isNullOrEmpty()) {
            Picasso.get()
                .load( R.drawable.news)
                .fit()
                .centerCrop()
                .into(holder.image)
        } else {
            Picasso.get()
                .load(imgUrl)
                .fit()
                .centerCrop()
                .error(R.drawable.news)
                .into(holder.image)
        }

        if (context.toString().contains("SavedNews")) {
            val date = " " + time?.substring(0, time.indexOf('T', 0))
            holder.newsPublicationTime.text = date
        } else {

            //if the hours exceed 24 hours use a day ago\
            val maxDay: Long=24

            val currentTimeInHours = Instant.now().atZone(ZoneId.of("Africa/Nairobi"))
            val newsTimeInHours = Instant.parse(time).atZone(ZoneId.of("Africa/Nairobi"))
            val hoursDifference = Duration.between(currentTimeInHours, newsTimeInHours)
            hoursAgo = " " + hoursDifference.toHours().toString().substring(1) + " hour ago"

            //if the hours exceed 24 hours -indicate as a data and the additional hours
            //computation cannot be between strings
            val tmData=hoursDifference.toHours().toString().substring(1)

             //  handle  the event of more than a day
            //use the time duration to render time in//days,hrs,weeks,months ,year
            val dayData=0

            holder.newsPublicationTime.text =hoursAgo

            if(tmData.toLong() < maxDay){
                holder.newsPublicationTime.text =hoursAgo

            }else if (tmData.toLong()> maxDay){
                val tMdifference=tmData.toLong()-maxDay

                holder.newsPublicationTime.text ="1 day and " +tMdifference+ " hrs ago"

            }else if (tmData.toLong()==maxDay){

                holder.newsPublicationTime.text ="1 day ago"

            }




        }

        var likes=0

        holder.likeBtn.setOnClickListener {
         likes++

            //update the likes count on firebase and on the container
            //if  the id has a like in a news article populate the number of  counts
             //when the like is withdrawn   decrement the number of likes from firebase

          when(likes){

              1-> holder.likeText.text=likes.toString()

              else->{

                  holder.likeText.text=""
                  likes=0
              }

          }



//add the likes to firbase and populate the total number of likes

            var numberOfLikesPerid=  holder.likeText.text.toString()


            //add  the likes to firebase and  display the number of like son the liked container
            //display the numbe rof likes from firebase


        }

        //double tap feature
        

//        holder.comentBTn.setOnClickListener {
//           count++
//
//            val handler=Handler()
//
//            handler.postDelayed({
//                                if (count==1){
//
//                                    holder.commentContainer.isVisible=true
//                                }else if (count==2){
//
//                                    holder.commentContainer.isVisible=false
//
//                                }
//                          count=0
//
//            },500)
//
//
//        }

        //launch the comment activity

        holder.comentBTn.setOnClickListener {
            //will later pass the current view in the container
            //pass the image and the data in the counter


            val intent= Intent(context, CommentActivity::class.java)
            intent.putExtra("headline",newsData.headLine)
            intent.putExtra("time",hoursAgo)
            intent.putExtra("image",imgUrl)
            context.startActivity(intent)



        }


            //share the  news
        //share the news url
        holder.ShareBTn.setOnClickListener {

            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, "Hello, Check this news Item : " + newsData.url)
            intent.type = "text/plain"
            context.startActivity(Intent.createChooser(intent, "Share with :"))

        }






        //handle the comments populator
      //  holder.recyclerComments.layoutManager= LinearLayoutManager(context)




//        holder.SendComment.setOnClickListener {
//
//
//            val commentD: String=holder.commentTextContainer.text.toString()
//            val data1=Comments(commentD,"2")
//            data.add(data1)
//
//
//            commentsAdapter= CommentsAdapter(context,data)
//            holder.recyclerComments.adapter=commentsAdapter
//            commentsAdapter.notifyDataSetChanged()
//
//
//
//
//
//        }
//
//populate  the number of comments in the main view

        mDbRef=FirebaseDatabase.getInstance().getReference()


        try {
            //have a common comment id that display comments to all users \

            mDbRef.child("Comments").child(commentId).child("sentComment")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        //traverse through all  the children in snapshot using the  for loop
                        //when  the data has changed in the firebase
                        //only those users who  have a ne message will have a  new message notification

                        //clear the data list to avoid duplication

                        data.clear()
                        for (postSnapshot in snapshot.children) {
                            val comments = postSnapshot.getValue(Comments::class.java)
                            if (comments!!.newsId==newsData.headLine){

                                data.add(comments!!)

                                commentsAmmout=data.size

                                holder.commentsQuantity.text=commentsAmmout.toString()

                            }



                            println("data from " + comments)

                        }


                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })

        }catch (e:Exception){

            println("error fetching comments")
        }



    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class ViewHolder(
        ItemView: View,
        listener: OnItemClickListener,
        listener2: OnItemLongClickListener
    ) : RecyclerView.ViewHolder(ItemView) {
        val image: ImageView = itemView.findViewById(R.id.img)
        val headLine: TextView = itemView.findViewById(R.id.news_title)
        val newsPublicationTime: TextView = itemView.findViewById(R.id.news_publication_time)
        val likeBtn:LinearLayout=itemView.findViewById(R.id.likeBTn)
        val likeText:TextView=itemView.findViewById(R.id.likeText)
        //find the comment button
        val comentBTn:LinearLayout=itemView.findViewById(R.id.ComentBTn)

//        //populating the comments items
//        val commentContainer:RelativeLayout=itemView.findViewById(R.id.CommentContainer)
//        val recyclerComments:RecyclerView=itemView.findViewById(R.id.recyclerComments)
//        val commentTextContainer:EditText=itemView.findViewById(R.id.commentTextContainer)
//        val SendComment:ImageButton=itemView.findViewById(R.id.SendComment)


        //comment number textview
        val commentsQuantity:TextView=itemView.findViewById(R.id.commentsQuantity)


        //share the news items

        val ShareBTn:LinearLayout=itemView.findViewById(R.id.ShareBTn)



        init {
            ItemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

            ItemView.setOnLongClickListener {
                listener2.onItemLongClick(adapterPosition)
                return@setOnLongClickListener true
            }
        }

    }

}
