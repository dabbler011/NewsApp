package newapp.social.org.newsapp.utils

import android.arch.persistence.room.*
import newapp.social.org.newsapp.models.Article

@Dao
interface NewsDao {
    @Query("SELECT * FROM articles")
    fun getAll(): List<Article>

    @Query("SELECT * FROM articles where id = :id")
    fun getNewsbyId(id: Long): Article

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg users: Article)

    @Delete
    fun delete(user: Article)
}