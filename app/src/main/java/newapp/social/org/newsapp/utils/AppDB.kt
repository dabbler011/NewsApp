package newapp.social.org.newsapp.utils

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Room
import android.content.Context
import newapp.social.org.newsapp.models.Article


@Database(entities = arrayOf(Article::class), version = 1)
abstract class AppDB : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {
        @JvmStatic
        private var INSTANCE: AppDB? = null

        @JvmStatic
        fun getAppDatabase(context: Context): AppDB {

            if (INSTANCE == null) {
                INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDB::class.java, "db-news")
                        .allowMainThreadQueries().build()
            }
            return INSTANCE as AppDB
        }

    }
}