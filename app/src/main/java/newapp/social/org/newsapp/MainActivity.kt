package newapp.social.org.newsapp

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appViewModel = AppViewModel.create(application)
        appViewModel.retrieveFromDb()
        appViewModel.fetchNews()

        appViewModel.articleResponse.observe(this, Observer {
            it?.articles?.let {
                if (!it.isEmpty()) {
                    appViewModel.updateDb(it)
                }
            }
        })

        appViewModel.articles.observe(this, Observer {
            it?.let {
                it.forEach { if (it.author!=null) Log.d("akshat",it.title) }
            }
        })
    }
}
