package newapp.social.org.newsapp.ViewModels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import newapp.social.org.newsapp.Activities.MainActivity
import newapp.social.org.newsapp.models.Article
import newapp.social.org.newsapp.models.ArticleResponse
import newapp.social.org.newsapp.models.Location
import newapp.social.org.newsapp.utils.*
import org.jetbrains.anko.toast
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.*

class AppViewModel (application: Application) : AndroidViewModel(application) {

    var dataFetched: MutableLiveData<Boolean> = MutableLiveData()
    val articleResponse: MutableLiveData<ArticleResponse> = MutableLiveData()
    val articles: MutableLiveData<List<Article>> = MutableLiveData()
    val location: MutableLiveData<Location> = MutableLiveData()
    val sharedPreferences = application.getSharedPreferences(MainActivity.PREF,Context.MODE_PRIVATE)

    private lateinit var appDb: AppDB
    private lateinit var newsDao: NewsDao

    init {
        dataFetched.value = false
        articleResponse.value = ArticleResponse("", 0, emptyList())
        articles.value = emptyList()
        location.value = Location(
            sharedPreferences.getString(MainActivity.COUNTRY_NAME, "India"),
            sharedPreferences.getString(MainActivity.COUNTRY_CODE, "in")
        )
    }

    fun initAppDB(context: Context) {
        appDb = AppDB.getAppDatabase(context)
        newsDao = appDb.newsDao()
    }

    fun retrieveFromDb() {
        GlobalScope.launch {
            articles.postValue(newsDao.getAll())
        }
    }

    fun updateDb(updates: List<Article>) {

        GlobalScope.launch {
            articles.postValue(updates)
            //Log.d("akshatsize", updates.size.toString())
            val getAll = newsDao.getAll()
            getAll.forEach { newsDao.delete(it) }
            updates.forEach { newsDao.insert(it) }
        }
    }

    fun fetchNews(context: Context,country: String) {
        val reference = "https://newsapi.org/"

        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(reference)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val newsService = retrofit.create(NewsApi::class.java)

        val fetchedNews = newsService.getHeadlines("general",country)
        GlobalScope.launch (Dispatchers.Main) {
            try {
                articleResponse.value = (fetchedNews.await())
            } catch (e: Exception) {
                println(e)
                e.message?.let { context.toast(it) }
            }
        }
    }

    fun fetchLocation(context: Context) {
        val reference = "https://api.ipdata.co/"
        val gson = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(reference)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val locationService = retrofit.create(LocationApi::class.java)
        val fetchedLocation = locationService.getLocation()
        GlobalScope.launch (Dispatchers.Main) {
            try {
                if (location.value != (fetchedLocation.await()))location.value = (fetchedLocation.await())
                context.toast(location.value!!.country_name)
            } catch (e: Exception) {
                println(e)
                e.message?.let { context.toast(it) }
            }
        }
    }

    companion object {
        fun create(application: Application): AppViewModel {
            return ViewModelProvider.AndroidViewModelFactory
                .getInstance(application).create(AppViewModel::class.java)
                .apply { initAppDB(application) }
        }
    }
}