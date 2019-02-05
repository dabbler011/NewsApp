package newapp.social.org.newsapp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.news_container.view.*

class NewsAdapter(val context: Context): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var newsList: List<Article> = emptyList()

    fun updateList(articles: List<Article>) {
        newsList = articles
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NewsAdapter.NewsViewHolder =
        NewsViewHolder(
            LayoutInflater.from(p0.context)
            .inflate(R.layout.news_container, p0, false))

    override fun getItemCount(): Int = newsList.size

    override fun onBindViewHolder(p0: NewsAdapter.NewsViewHolder, p1: Int) {
        p0.bindItem(context,newsList.get(p1))
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(context: Context,news: Article) {
            news.title?.let {
                itemView.tv_news_title.text = it
            }
            news.source?.name?.let {
                itemView.tv_news_source.text = it
            }
            news.urlToImage?.let {
                GlideApp.with(context)
                    .load(it)
                    .into(itemView.tv_news_image)
            }

        }
    }

}