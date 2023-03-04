package com.example.news.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.news.MainActivity
import com.example.news.Repo.NewsRpo
import com.example.news.databinding.FragmentSavedBinding
import com.example.news.db.ArticleDB
import com.example.news.ui.NewsViewModel
import com.example.news.ui.NewsViewModelProviderFactory

class SavedFragment : Fragment() {
    private var _binding: FragmentSavedBinding? = null

    private lateinit var newsViewModel:NewsViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)

        val newsRepo=NewsRpo(ArticleDB(requireActivity()))
        val viewModelProviderFactory=NewsViewModelProviderFactory(newsRepo)
        newsViewModel=ViewModelProvider(this,viewModelProviderFactory)[NewsViewModel::class.java]



        val root: View = _binding!!.root

        return root
    }

}