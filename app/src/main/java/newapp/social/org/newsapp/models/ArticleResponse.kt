package newapp.social.org.newsapp.models

data class ArticleResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)