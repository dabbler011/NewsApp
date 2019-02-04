package newapp.social.org.newsapp

import android.arch.persistence.room.*

@Dao
interface NewsDao {
    @Query("SELECT * FROM articles")
    fun getAll(): List<Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg users: Article)

    @Delete
    fun delete(user: Article)
}