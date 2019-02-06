package newapp.social.org.newsapp

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import android.support.v7.widget.DividerItemDecoration




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
    private lateinit var searchView: SearchView
    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        sharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE)

        appViewModel = AppViewModel.create(application)

        adapter = NewsAdapter(this)

        news_view.layoutManager = LinearLayoutManager(this)
        news_view.adapter = adapter
        news_view.addItemDecoration(DividerItemDecoration(news_view.getContext(), DividerItemDecoration.VERTICAL))

        progressBar.visibility = View.VISIBLE

        appViewModel.retrieveFromDb()
        appViewModel.fetchLocation(this)

        pullToRefresh.setOnRefreshListener {
            appViewModel.fetchNews(this,sharedPreferences.getString(COUNTRY_CODE,"in"))
            pullToRefresh.isRefreshing = false
        }

        appViewModel.articleResponse.observe(this, Observer {

            it?.articles?.let {
                if (!it.isEmpty()) {
                    var check = false
                    if (!appViewModel.articles.value.isNullOrEmpty()) {
                        if (it.get(0).title.equals(appViewModel.articles.value!!.get(0).title)
                            and (it.get(0).title != null)) {
                            check= true
                        }
                    }
                    if (!check) appViewModel.updateDb(it)
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
                    toast("feed updated")
                    adapter.updateList(it)

                }
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Integer.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.getFilter().filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                adapter.getFilter().filter(query)
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (it.itemId == R.id.action_location) {
                toast("Updating Location...")
                appViewModel.fetchLocation(this)
                return true
            } else if (it.itemId == R.id.action_search) {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true)
            return
        }
        super.onBackPressed()
    }
}
