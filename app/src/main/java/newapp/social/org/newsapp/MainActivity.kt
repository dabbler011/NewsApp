package newapp.social.org.newsapp

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {


    companion object {
        @JvmStatic
        val PREF = "SHARED_PREFERENCE"
        @JvmStatic
        val COUNTRY_CODE = "country_code"
        @JvmStatic
        val COUNTRY_NAME = "country_name"
    }

    private lateinit var appViewModel: AppViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        sharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE)

        appViewModel = AppViewModel.create(application)

        val adapter = NewsAdapter(this)

        news_view.layoutManager = LinearLayoutManager(this)
        news_view.adapter = adapter

        progressBar.visibility = View.VISIBLE

        appViewModel.retrieveFromDb()
        appViewModel.fetchLocation(this)
       // appViewModel.fetchNews("in")

        appViewModel.articleResponse.observe(this, Observer {
            it?.articles?.let {
                if (!it.isEmpty()) {
                    appViewModel.updateDb(it)
                }
            }
        })

        appViewModel.location.observe(this, Observer {
            it?.country_name?.let {
                sharedPreferences.edit().putString(COUNTRY_NAME,it).apply()
            }
            it?.country_code?.let {
                sharedPreferences.edit().putString(COUNTRY_CODE,it).apply()
                appViewModel.fetchNews(this,it)
            }
        })

        appViewModel.articles.observe(this, Observer {
            it?.let {
                if (!it.isEmpty()) {
                    if (progressBar.visibility == View.VISIBLE)
                        progressBar.visibility = View.GONE
                    adapter.updateList(it)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (it.itemId == R.id.action_location) {
                toast("Updating Location...")
                appViewModel.fetchLocation(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
