package com.example.news.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.Repo.NewsRpo
import com.example.news.model.Article
import com.example.news.model.NewsResponse
import com.example.news.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Query

class NewsViewModel(
    val newsRepo: NewsRpo
):ViewModel() {
        val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        val breakingNewsPage=1

        val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        val searchNewsPage=1
    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = newsRepo.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response:retrofit2.Response<NewsResponse>):Resource<NewsResponse>{
        if (response != null){
            response?.body().let { resultResponse ->
                return Resource.Success(resultResponse)
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
            response?.body().let { resultResponse ->
                return Resource.Success(resultResponse)
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