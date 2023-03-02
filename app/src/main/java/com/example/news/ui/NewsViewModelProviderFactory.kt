package com.example.news.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.news.Repo.NewsRpo

class NewsViewModelProviderFactory(
    val newsRpo: NewsRpo
):  ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRpo) as T
    }
}