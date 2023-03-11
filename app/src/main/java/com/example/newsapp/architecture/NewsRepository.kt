package com.example.newsapp.architecture

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.BuildConfig
import com.example.newsapp.MainActivity
import com.example.newsapp.NewsModel
import com.example.newsapp.api.Coutrylist
import com.example.newsapp.api.NewsApi
import com.example.newsapp.api.NewsDataFromJson
import com.example.newsapp.api.RetrofitHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRepository {

    //add the list of countries from the country list
    private lateinit var coutryList: ArrayList<Coutrylist>
     private var newc=""

    //get the name of the country passed from the selection from the drop down  menu
    //check whether the selected country is in the list of  the supported countries


    companion object {

        private var newsDatabase: NewsDatabase? = null

        private fun initializeDB(context: Context): NewsDatabase {
            return NewsDatabase.getDatabaseClient(context)
        }

        fun insertNews(context: Context, news: NewsModel) {

            newsDatabase = initializeDB(context)
            CoroutineScope(IO).launch {
                newsDatabase!!.newsDao().insertNews(news)
            }
        }

        fun deleteNews(context: Context, news: NewsModel) {

            newsDatabase = initializeDB(context)
            CoroutineScope(IO).launch {
                newsDatabase!!.newsDao().deleteNews(news)
            }
        }

        fun getAllNews(context: Context): LiveData<List<NewsModel>> {

            newsDatabase = initializeDB(context)
            return newsDatabase!!.newsDao().getNewsFromDatabase()
        }

    }

    // get news from API
    fun getNewsApiCall(category: String?): MutableLiveData<List<NewsModel>> {
        coutryList= ArrayList()
        for (datas in coutryList){
             newc=datas.countryCode.toString()

        }


        val newsList = MutableLiveData<List<NewsModel>>()

        val call = RetrofitHelper.getInstance().create(NewsApi::class.java)

                //add a custom entry to fetch all the countries in the api
            //add the api key registered in newsapi.org
                //add a method to enter user custo country

            .getNews("us", category, BuildConfig.API_KEY) //put your api key here
        //cast  the country names in a string and place them using a spinner
        //allow the user to modify country data

        call.enqueue(object : Callback<NewsDataFromJson> {
            override fun onResponse(
                call: Call<NewsDataFromJson>,
                response: Response<NewsDataFromJson>
            ) {

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body != null) {
                        val tempNewsList = mutableListOf<NewsModel>()

                        body.articles.forEach {
                            tempNewsList.add(
                                NewsModel(
                                    it.title,
                                    it.urlToImage,
                                    it.description,
                                    it.url,
                                    it.source.name,
                                    it.publishedAt,
                                    it.content
                                )
                            )
                        }
                        newsList.value = tempNewsList
                    }

                } else {

                    val jsonObj: JSONObject?

                    try {
                        jsonObj = response.errorBody()?.string()?.let { JSONObject(it) }
                        if (jsonObj != null) {
                            MainActivity.apiRequestError = true
                            MainActivity.errorMessage = jsonObj.getString("message")
                            val tempNewsList = mutableListOf<NewsModel>()
                            newsList.value = tempNewsList
                        }
                    } catch (e: JSONException) {
                        Log.d("JSONException", "" + e.message)
                    }

                }
            }

            override fun onFailure(call: Call<NewsDataFromJson>, t: Throwable) {

                MainActivity.apiRequestError = true
                MainActivity.errorMessage = t.localizedMessage as String
                Log.d("err_msg", "msg" + t.localizedMessage)
            }
        })

        return newsList
    }

}

