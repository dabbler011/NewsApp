package newapp.social.org.newsapp

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "articles")
data class Article (
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val publishedAt: String?,
    val urlToImage: String?,
    @Embedded(prefix = "source_")
    val source: ArticleSource?,
    val content: String?

)