package newapp.social.org.newsapp

data class ArticleResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)