package com.example.news.Repo

import com.example.news.api.RetrofitInstance
import com.example.news.db.ArticleDB
import com.example.news.model.Article
import retrofit2.http.Query

class NewsRpo(
    val db: ArticleDB
) {


    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String,pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun insert(articleDB: Article)=db.getArticleDao().insert(articleDB)

    fun getSavedNews()=db.getArticleDao().getAllArticles()

    suspend fun delete(articleDB: Article)=db.getArticleDao().delete(articleDB)

}