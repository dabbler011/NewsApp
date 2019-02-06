package newapp.social.org.newsapp

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import kotlinx.android.synthetic.main.news_container.view.*

class NewsAdapter(val context: Context): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(), Filterable {

    companion object {
        val ID = "id"
        val CONTENT = "content"
        val DESC = "description"
        val IMAGE_URL = "image_url"
        val TITLE = "title"
        val SOURCE = "source"
        val PUBLISHED_AT = "published_at"
        val URL = "url"
    }

    private var newsList: List<Article> = emptyList()
    private var newsFilteredList: List<Article> = emptyList()

    fun updateList(articles: List<Article>) {
        newsList = articles
        newsFilteredList = articles
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NewsAdapter.NewsViewHolder =
        NewsViewHolder(
            LayoutInflater.from(p0.context)
            .inflate(R.layout.news_container, p0, false))

    override fun getItemCount(): Int = newsFilteredList.size

    override fun onBindViewHolder(p0: NewsAdapter.NewsViewHolder, p1: Int) {
        p0.bindItem(context,newsFilteredList.get(p1))
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    newsFilteredList = newsList
                } else {
                    val filteredList = mutableListOf<Article>()
                    for (row in newsList) {
                        row.title?.let {

                            if (it.toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row)
                            }
                        }
                    }

                    newsFilteredList = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = newsFilteredList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                newsFilteredList = filterResults.values as List<Article>
                notifyDataSetChanged()
            }
        }
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
                    .placeholder(R.drawable.news_gradient)
                    .into(itemView.tv_news_image)
            }
            itemView.setOnClickListener {
                val intent = Intent(context,NewsDetailsActivity::class.java).apply {
                    putExtra(ID,news.id)
                    putExtra(CONTENT,news.content)
                    putExtra(DESC,news.description)
                    putExtra(IMAGE_URL,news.urlToImage)
                    putExtra(TITLE,news.title)
                    putExtra(SOURCE,news.source?.name)
                    putExtra(PUBLISHED_AT,news.publishedAt)
                    putExtra(URL,news.url)
                }
                context.startActivity(intent)
            }
        }
    }

}