package com.example.newsapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.newsapp.adapters.ContactsAdapter
import com.example.newsapp.adapters.FragmentAdapter
import com.example.newsapp.architecture.NewsViewModel
import com.example.newsapp.firebase.data.Message
import com.example.newsapp.firebase.data.User
import com.example.newsapp.utils.Constants.BS
//import com.example.newsapp.utils.Constants.BUSINESS
import com.example.newsapp.utils.Constants.ENTERTAINMENT
import com.example.newsapp.utils.Constants.GENERAL
import com.example.newsapp.utils.Constants.HEALTH
import com.example.newsapp.utils.Constants.HOME
//import com.example.newsapp.utils.Constants.HOME2
import com.example.newsapp.utils.Constants.SCIENCE
import com.example.newsapp.utils.Constants.SPORTS
import com.example.newsapp.utils.Constants.TECHNOLOGY
import com.example.newsapp.utils.Constants.TOTAL_NEWS_TAB
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ru.nikartm.support.ImageBadgeView


class MainActivity : AppCompatActivity() {

    // Tabs Title
    //initially declared n the constants and later imported in the activity
    //Modify the layout  of the nes views to emulate Twitter layout
    //Modify the nes container

    //Todo --adding  the appliacation crash analytics that will  collect  crash data  in firebase
    //add  a notification badge to show whwm new items  arrrive-- ie news feed or new messages
    //show notification badges that populate notifications
    //Notify the user when a new version of the  application  has been released
    //write  unit tests to test the ui and  the vital operations
    //show a notification  when the user is online- and when they are not --
    //when the user is logged in the application and the application is currently in running state
    //show a notification when  the user is typing the message

    //when  user is typing message module--add  a listener to the text container
    //when  the user makes  an input - the recipient receives a  typing  notification
    //allow user to modify the profile image
    //adding the comments module
    //restructuring the comment section --add a comment module and pass the relevant comment modules to
    //populate the comments
    //while populating the likes, on double click the like is dismissed
    //each iud is allowed to like  an item once
    //updating the message badge --listen to firebase and get the number of messages
    //update the count depending on the number of messages
    //Todo-- adding an notifier  to alert user when a new message arrives


    private val newsCategories = arrayOf(
        HOME, BS,
        ENTERTAINMENT, SCIENCE,
        SPORTS, TECHNOLOGY, HEALTH
    )
//late initialise variables
    private lateinit var viewModel: NewsViewModel
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var fragmentAdapter: FragmentAdapter
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private var totalRequestCount = 0
    private  lateinit var  txtNet: TextView
    private lateinit var showError: TextView
    private lateinit var PullRefresher: SwipeRefreshLayout
    private lateinit var imageBadgeView: ImageBadgeView
    private lateinit var imageBadgeViewNotify: ImageBadgeView

    private lateinit var mDbRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    private lateinit var messageList: ArrayList<Message>
    private lateinit var userList: ArrayList<User>





    val dataMessage = ArrayList<Message>()

    //user data
    var userData=ArrayList<User>()

    var senderRoom: String?=null
    var receiverRoom: String?=null


    var receiverUid=""
    val senderUid= FirebaseAuth.getInstance().currentUser?.uid








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set Action Bar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        shimmerLayout=findViewById(R.id.shimmer_layout)

       txtNet=findViewById(R.id.txtNet)

        showError=findViewById(R.id.display_error)

        //prevent swipe between tabs
        viewPager.isUserInputEnabled=false



        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        //pull to refresh the layout
        PullRefresher=findViewById(R.id.PullRefresher)


        //call the function every second
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {

                //Call your function here
                isNetworkAvailable(this@MainActivity)
                if (!isNetworkAvailable(applicationContext)){
                    val offline="offline"
                    txtNet.text=offline
                    txtNet.setTextColor(Color.parseColor("#ed4f3f"))


                }else{
                    val online="online"
                    txtNet.text=online
                    txtNet.setTextColor(Color.parseColor("#298932"))

                }


                handler.postDelayed(this, 1000)//1 sec delay
            }
        }, 0)


        ///log error to the user  when internet connection is  not available


        if (!isNetworkAvailable(applicationContext)){

            shimmerLayout.visibility = View.GONE
            showError.text = getString(R.string.internet_warming)
            showError.visibility = View.VISIBLE

        }

        //pull to refresh

        PullRefresher.setOnRefreshListener {
            PullRefresher.isRefreshing = false

            //pull  to refresh the layout and clear the view to avoid duplication



        }


        requestNews(GENERAL, generalNews)
        requestNews(BS, businessNews)
        requestNews(ENTERTAINMENT, entertainmentNews)
        requestNews(HEALTH, healthNews)
        requestNews(SCIENCE, scienceNews)
        requestNews(SPORTS, sportsNews)
        requestNews(TECHNOLOGY, techNews)

        fragmentAdapter = FragmentAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = fragmentAdapter
        viewPager.visibility = View.GONE




//        //incerement or decrement badge count
//        val count=30;
//        val menuItem:MenuItem
//         menuItem = menu.(R.id.messageic) as MenuItem
//        val actionView = menuItem.actionView
//
//        actionView?.findViewById<ImageBadgeView>(R.id.cart_menu_icon)?.badgeValue = count
        val data = ArrayList<Message>()
        //when a new item is added on the message list increment badge count





        //badge messages
        val count=2
        imageBadgeView=findViewById(R.id.cart_menu_icon)


        //update the message badge show  the number of the new messages on the badge


        mDbRef= FirebaseDatabase.getInstance().getReference()
        mAuth= FirebaseAuth.getInstance()

//listen to firebase and get the number of the messages
        //increment  the count  of the message badge
       // userList=ArrayList()

      updateMessageBadge()



        imageBadgeView.setOnClickListener {

            intent = Intent(applicationContext, MessagesActivity::class.java)
            startActivity(intent)
            imageBadgeView.badgeValue=0

        }

        //Todo ---Incememt or decrement badge count when  new items arrive
        //when the user clicks on the item-- it is assumed that all notifications have been read

        //badge notify
        val notificationCount=1
        imageBadgeViewNotify=findViewById(R.id.cart_menu_icon2)
        imageBadgeViewNotify.badgeValue=notificationCount

        imageBadgeViewNotify.setOnClickListener {
            imageBadgeViewNotify.badgeValue=0

        }

        //get  the receiver uid from firebase
        userList= ArrayList()


        //gett the messages



        //oncreate method ends here

    }

    private fun updateMessageBadge() {

       // val currentuser=userData[position]

       // val  receiverUid=currentuser.uid

        mDbRef.child("credentials").addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                userData.clear()

                //traverse through the items in the database to sort out individual items
                for (postSnapshot in snapshot.children){
                    //clear the list before adding values to avoid duplication when
                    //data set is changed
                    val currentUser=postSnapshot.getValue(User::class.java)

                    if (mAuth.currentUser?.uid  != currentUser?.uid){

                        userData.add(currentUser!!)

                       // imageBadgeView.badgeValue=userData.size

                       // receiverUid=userData.get(0).toString()

//                        for (datas in userData){
//
//                            receiverUid=datas.uid.toString()
//                        }



                        //  receiverRoom=senderUid+receiverUid


                        imageBadgeView.badgeValue=userData.size


                    }


                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })





        senderRoom=receiverUid+senderUid

     //   senderRoom=receiverUid+senderUid

        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //traverse through all  the children in snapshot using the  for loop
                    //when  the data has changed in the firebase
                    //only those users who  have a ne message will have a  new message notification

                    //clear the data list to avoid duplication
                    dataMessage.clear()

                    for (postSnapshot in snapshot.children){
                        val message=postSnapshot.getValue(Message::class.java)
                        dataMessage.add(message!!)
                       //imageBadgeView.badgeValue=dataMessage.size


                    }


                }

                override fun onCancelled(error: DatabaseError) {

                }

            })




    }


    private fun requestNews(newsCategory: String, newsData: MutableList<NewsModel>) {
        viewModel.getNews(category = newsCategory)?.observe(this) {
            newsData.addAll(it)
            totalRequestCount += 1

            // If main fragment loaded then attach the fragment to viewPager
            if (newsCategory == GENERAL) {
                shimmerLayout.stopShimmer()
                shimmerLayout.hideShimmer()
                shimmerLayout.visibility = View.GONE
                setViewPager()
            }

            if (totalRequestCount == TOTAL_NEWS_TAB) {
                viewPager.offscreenPageLimit = 7
            }
        }
    }

    private fun setViewPager() {
        if (!apiRequestError) {
            viewPager.visibility = View.VISIBLE

            //Edit tab items title
            //fetches the titles in the array -initially declared in the constants
            //when the titles are casted in tablayout the show in all caps
            //Tab titles are fetched from the declared strings in ths case
            //else -can be manually added based on the index on the adapter like in this case
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = newsCategories[position]

            }.attach()
        } else {
            val showError: TextView = findViewById(R.id.display_error)
            showError.text = errorMessage
            showError.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item_mainactivity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.savednews_menu -> {
                intent = Intent(applicationContext, SavedNewsActivity::class.java)
                startActivity(intent)
                return super.onOptionsItemSelected(item)

            }
//            R.id.messageic->{
//                val count=70
//                val actionView = item.actionView
//                actionView?.findViewById<ImageBadgeView>(R.id.cart_menu_icon)?.badgeValue = count
//
//                intent = Intent(applicationContext, MessagesActivity::class.java)
//                startActivity(intent)
//
//                return super.onOptionsItemSelected(item)
//
//            }

            R.id.menuBar->{
                intent = Intent(applicationContext, UserProfile::class.java)
                startActivity(intent)
                return super.onOptionsItemSelected(item)

            }
        }

        return true
    }

    // Check internet connection
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // For 29 api or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            // For below 29 api
            if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
                return true
            }
        }
        return false
    }

    companion object {
        var generalNews: ArrayList<NewsModel> = ArrayList()
        var entertainmentNews: MutableList<NewsModel> = mutableListOf()
        var businessNews: MutableList<NewsModel> = mutableListOf()
        var healthNews: MutableList<NewsModel> = mutableListOf()
        var scienceNews: MutableList<NewsModel> = mutableListOf()
        var sportsNews: MutableList<NewsModel> = mutableListOf()
        var techNews: MutableList<NewsModel> = mutableListOf()
        var apiRequestError = false
        var errorMessage = "error"
    }
}
