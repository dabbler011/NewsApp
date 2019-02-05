package newapp.social.org.newsapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_news_details.*

class NewsDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)

        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val id = intent.getLongExtra(NewsAdapter.ID,0)
        val content = intent.getStringExtra(NewsAdapter.CONTENT)
        val description = intent.getStringExtra(NewsAdapter.DESC)
        val imageUrl = intent.getStringExtra(NewsAdapter.IMAGE_URL)
        val title = intent.getStringExtra(NewsAdapter.TITLE)
        val source = intent.getStringExtra(NewsAdapter.SOURCE)
        val publishedAt = intent.getStringExtra(NewsAdapter.PUBLISHED_AT)
        val url = intent.getStringExtra(NewsAdapter.URL)

        val context = this

        content?.let {
            tv_news_content.text = it
        }
        description?.let {
            tv_news_desc.text = it
        }
        imageUrl?.let {
            GlideApp.with(context)
                .load(it)
                .into(tv_news_image)
        }
        title?.let {
            tv_news_title.text = it
        }
        source?.let {
            tv_news_source.text = it
        }
        publishedAt?.let {
            tv_time.text = it
        }
        btn_read_full.setOnClickListener {
            url?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
    }
}
