package newapp.social.org.newsapp

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appViewModel = AppViewModel.create(application)

        appViewModel.fetchNews()

        appViewModel.dataFetched.observe(this, Observer {
            it?.let {
                if (it) {
                    toast("done "+ appViewModel.articles.value!!.status+" "+appViewModel.articles.value!!.totalResults)
                }
            }
        })
    }
}
