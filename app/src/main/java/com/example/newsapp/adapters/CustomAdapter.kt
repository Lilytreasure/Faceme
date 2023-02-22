package com.example.newsapp.adapters

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.NewsModel
import com.example.newsapp.R
import com.example.newsapp.firebase.data.Comments
import com.example.newsapp.firebase.data.User
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
    private lateinit var commentsAdapter: CommentsAdapter
    val commentsData = ArrayList<Comments>()

    private var sentComment:String=""

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
                .load( R.drawable.nopic)
                .fit()
                .centerCrop()
                .into(holder.image)
        } else {
            Picasso.get()
                .load(imgUrl)
                .fit()
                .centerCrop()
                .error(R.drawable.nopic)
                .into(holder.image)
        }

        if (context.toString().contains("SavedNews")) {
            val date = " " + time?.substring(0, time.indexOf('T', 0))
            holder.newsPublicationTime.text = date
        } else {
            val currentTimeInHours = Instant.now().atZone(ZoneId.of("Asia/Kolkata"))
            val newsTimeInHours = Instant.parse(time).atZone(ZoneId.of("Asia/Kolkata"))
            val hoursDifference = Duration.between(currentTimeInHours, newsTimeInHours)
            val hoursAgo = " " + hoursDifference.toHours().toString().substring(1) + " hour ago"
            holder.newsPublicationTime.text = hoursAgo
        }

        var likes=0

        holder.likeBtn.setOnClickListener {
            likes++
            holder.likeText.text= likes.toString()

        }
        

        holder.comentBTn.setOnClickListener {
           count++

            val handler=Handler()

            handler.postDelayed({
                                if (count==1){

                                    holder.commentsContainer.isVisible=true
                                }else if (count==2){

                                    holder.commentsContainer.isVisible=false

                                }
                          count=0

            },500)


        }


        //sending the comment button

        //setting up thr recycler view


        holder.sendComment.setOnClickListener {
            sentComment= holder.commentTextContainer.text.toString()
            val cmnt=Comments(sentComment,"3");
            commentsData.add(cmnt)

            for (cmnts in commentsData){
                println(cmnts)
            }


        }

        holder.recyclerComments.layoutManager= LinearLayoutManager(context)
        commentsAdapter=CommentsAdapter(context,commentsData)
        holder.recyclerComments.adapter=commentsAdapter
        commentsAdapter.notifyDataSetChanged()



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
        val commentsContainer:LinearLayout=itemView.findViewById(R.id.commentsContainer)
        val sendComment:ImageButton=itemView.findViewById(R.id.SendComment)
        //find the recyclerView comments
        val recyclerComments:RecyclerView=itemView.findViewById(R.id.recyclerComments)

        //comment textcontainer
        val commentTextContainer:EditText=itemView.findViewById(R.id.commentTextContainer)


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
