package newapp.social.org.newsapp

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProvider
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.Exception
import java.util.*

class AppViewModel (application: Application) : AndroidViewModel(application) {

    var dataFetched: MutableLiveData<Boolean> = MutableLiveData()
    val articles: MutableLiveData<ArticleResponse> = MutableLiveData()

    init {
        dataFetched.value = false
        articles.value = ArticleResponse("",0,null)
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

        val fetchedSponsors = newsService.getHeadlines("general","in")
        GlobalScope.launch {
            try {

                articles.postValue(fetchedSponsors.await())
                dataFetched.postValue(true)
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    companion object {
        fun create(application: Application): AppViewModel {
            return ViewModelProvider.AndroidViewModelFactory
                .getInstance(application).create(AppViewModel::class.java)
                //.apply { initAppDB(application) }
        }
    }
}