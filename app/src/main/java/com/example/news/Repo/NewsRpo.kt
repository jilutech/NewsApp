package com.example.news.Repo

import com.example.news.api.RetrofitInstance
import com.example.news.db.ArticleDB

class NewsRpo(
    val db: ArticleDB
) {


    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

}