package com.example.news.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.NewsApplication
import com.example.news.Repo.NewsRpo
import com.example.news.model.Article
import com.example.news.model.NewsResponse
import com.example.news.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Query

class NewsViewModel(
    app:Application,
    val newsRepo: NewsRpo
):AndroidViewModel(app) {
        val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        var breakingNewsPage=1


        val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        var searchNewsPage=1


        var breakingNewsResponse:NewsResponse?=null
        var searchNewsResponse:NewsResponse?=null
    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
//        getApplication<NewsApplication>()
        val response = newsRepo.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response:retrofit2.Response<NewsResponse>):Resource<NewsResponse>{
        if (response != null){
            response?.body().let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null){
                    breakingNewsResponse=resultResponse
                }else{
                    val oldArticle = breakingNewsResponse?.articles
                    val newArticle = resultResponse?.articles
                    if (newArticle != null) {
                        oldArticle?.addAll(newArticle)
                    }

                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.body(),response.message())
    }

    fun searchNews(searchQuery : String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newsRepo.searchNews(searchQuery,searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleSearchNewsResponse(response: retrofit2.Response<NewsResponse>):Resource<NewsResponse>{
        if (response!=null){
            response.body().let { resultResponse ->
                searchNewsPage++

                if (searchNewsResponse == null){
                      searchNewsResponse=resultResponse

                }else{

                    val oldArti=searchNewsResponse?.articles
                    val newArti=resultResponse?.articles
                    if (newArti != null) {
                        oldArti?.addAll(newArti)
                    }

                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.body(),response.message())
    }

    fun saveArticle(article: Article)=viewModelScope.launch {
        newsRepo.insert(article)
    }

    fun getSavedNews()=newsRepo.getSavedNews()

    fun deleteArticle(article: Article)=viewModelScope.launch {
        newsRepo.delete(article)
    }











}