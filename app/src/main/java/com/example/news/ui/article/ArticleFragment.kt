package com.example.news.ui.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.news.R
import com.example.news.Repo.NewsRpo
import com.example.news.adapters.NewsAdapters
import com.example.news.databinding.FragmentArticleBinding
import com.example.news.db.ArticleDB
import com.example.news.ui.NewsViewModel
import com.example.news.ui.NewsViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : Fragment() {

    private var _binding:FragmentArticleBinding?=null
    private val binding get() = _binding!!
    val args: ArticleFragmentArgs  by  navArgs()
    
    lateinit var newsViewModel: NewsViewModel

     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentArticleBinding.inflate(inflater,container,false)
        val root: View = binding.root

         val newsRpo=NewsRpo(ArticleDB(requireActivity()))
         val viewModelProviderFactory=NewsViewModelProviderFactory(newsRpo)
         newsViewModel=ViewModelProvider(this,viewModelProviderFactory)[NewsViewModel::class.java]


         val article=args.article
         _binding!!.webView.apply {
             webViewClient = WebViewClient()
             loadUrl(article.url)

         }
         _binding!!.fab.setOnClickListener {
             newsViewModel.saveArticle(article)
             view?.let { it1 -> Snackbar.make(it1,"saved",Snackbar.LENGTH_SHORT) }
         }

        return root


    }
}