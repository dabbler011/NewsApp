package newapp.social.org.newsapp

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.room.Room
import android.content.Context
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.Exception
import java.util.*

class AppViewModel (application: Application) : AndroidViewModel(application) {

    var dataFetched: MutableLiveData<Boolean> = MutableLiveData()
    val articleResponse: MutableLiveData<ArticleResponse> = MutableLiveData()
    val articles: MutableLiveData<List<Article>> = MutableLiveData()


    private lateinit var appDb: AppDB
    private lateinit var newsDao: NewsDao

    init {
        dataFetched.value = false
        articleResponse.value = ArticleResponse("",0, emptyList())
        articles.value = emptyList()
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
            updates.forEach { newsDao.insert(it) }
        }

    }

    fun fetchNews() {
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

        val fetchedNews = newsService.getHeadlines("general","in")
        GlobalScope.launch {
            try {
                articleResponse.postValue(fetchedNews.await())
            } catch (e: Exception) {
                println(e)
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