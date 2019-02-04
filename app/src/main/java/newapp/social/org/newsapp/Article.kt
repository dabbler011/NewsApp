package newapp.social.org.newsapp

import java.sql.Timestamp

data class Article (
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val publishedAt: Timestamp?,
    val urlToImage: String?,
    val source: ArticleSource?,
    val content: String?
)