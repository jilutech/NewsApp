package com.example.news.db

import android.content.Context
import androidx.room.*
import com.example.news.model.Article
import kotlin.coroutines.CoroutineContext

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDB : RoomDatabase() {
    abstract fun getArticle():ArticleDao

    companion object{
        @Volatile
        private var privateInstance:ArticleDB?=null
        private val LOCK =Any()

        operator fun invoke(context: Context) = privateInstance ?: synchronized(LOCK){
            privateInstance ?: createDB(context).also{
                privateInstance = it
            }
        }


        private fun createDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDB::class.java,
                "article_db.db"
            ).build()
    }

}