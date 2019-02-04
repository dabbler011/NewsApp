package newapp.social.org.newsapp

import android.arch.persistence.room.ColumnInfo

data class ArticleSource (
    @ColumnInfo(name = "id")
    val id: String?,
    @ColumnInfo(name = "name")
    val name: String?
)