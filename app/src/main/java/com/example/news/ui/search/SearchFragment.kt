package com.example.news.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.MainActivity
import com.example.news.R
import com.example.news.Repo.NewsRpo
import com.example.news.adapters.NewsAdapters
import com.example.news.databinding.FragmentSearchedBinding
import com.example.news.db.ArticleDB
import com.example.news.ui.NewsViewModel
import com.example.news.ui.NewsViewModelProviderFactory
import com.example.news.utils.Constants.Companion.TIME_DELAY
import com.example.news.utils.Resource
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchedBinding? = null
    private lateinit var newsModel:NewsViewModel
    private val binding get() = _binding!!
    private lateinit var newsAdapters: NewsAdapters

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setRecycle()
        newsAdapters.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_searchFragment_to_articleFragment,
                bundle
            )
        }
        var job : Job? = null
        _binding!!.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                     newsModel.searchNews(editable.toString())
                    }
                }
            }
        }
        newsAdapters.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_searchFragment_to_articleFragment,
                bundle
            )
        }
        val repository = NewsRpo(ArticleDB(requireActivity()))
        val viewModelProviderFactory=NewsViewModelProviderFactory(repository)
        newsModel=ViewModelProvider(this,viewModelProviderFactory)[NewsViewModel::class.java]
        newsModel.searchNews.observe(viewLifecycleOwner, Observer { response ->

            when(response) {
                is Resource.Success ->{
                     hideProgbar()
                     response.data.let { newsResponse ->
                         newsAdapters.differ.submitList(newsResponse?.articles)
                     }
                }
                is Resource.Loading ->{
                   unHideProgbar()
                }
                is Resource.Error ->{

                    unHideProgbar()
                    response?.message?.let { message ->
                        Log.e("Error",message)

                    }
                }
                else -> {}
            }
        })
        return root
    }

    fun setRecycle(){
        newsAdapters=NewsAdapters()
        _binding?.rvSearchNews?.apply {
            adapter=newsAdapters
            layoutManager=LinearLayoutManager(requireActivity())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun hideProgbar(){
        _binding?.paginationProgressBar?.visibility=View.INVISIBLE
    }
    private fun unHideProgbar(){
        _binding?.paginationProgressBar?.visibility=View.VISIBLE
    }
}