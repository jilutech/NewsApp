package com.example.news.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.Repo.NewsRpo
import com.example.news.adapters.NewsAdapters
import com.example.news.databinding.FragmentHomeBinding
import com.example.news.db.ArticleDB
import com.example.news.ui.NewsViewModel
import com.example.news.ui.NewsViewModelProviderFactory
import com.example.news.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.example.news.utils.Resource
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: NewsViewModel
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var newsAdapters: NewsAdapters
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val repository = NewsRpo(ArticleDB(requireActivity()))
        val viewModelProviderFactory= NewsViewModelProviderFactory(repository)
        homeViewModel= ViewModelProvider(this,viewModelProviderFactory)[NewsViewModel::class.java]
        setUpRecyclerView()

        newsAdapters.setOnItemClickListener {

            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_navigation_home_to_articleFragment,
                bundle,
            )
        }
        homeViewModel.breakingNews.observe(viewLifecycleOwner, Observer {response ->

            when(response){
                is Resource.Success ->{
                 hideProgbar()
                    response.data.let { newsRespo ->
                        newsAdapters.differ.submitList(newsRespo?.articles?.toList())
                        val totalPage = newsRespo?.totalResults!! / QUERY_PAGE_SIZE + 2
                        isLastPage = homeViewModel.breakingNewsPage == totalPage
                    }
                }
                is Resource.Error ->{
                       hideProgbar()
                    response.message?.let { message ->
                      Log.e("error",message)
                    }
                }
                is Resource.Loading ->{
                    unHideProgbar()
                }
                else -> {}
            }
        })
        val root: View = binding.root

        return root
    }
    private fun hideProgbar(){
        paginationProgressBar.visibility=View.INVISIBLE
        isLoading =false
    }
    private fun unHideProgbar(){
        paginationProgressBar.visibility=View.VISIBLE
        isLoading = true
    }

    fun setUpRecyclerView(){
        newsAdapters= NewsAdapters()
        _binding?.rvBreakingNews?.apply {
            adapter=newsAdapters
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HomeFragment.scrollListener)
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var isScroling =false
    var isLoading = false
    var isLastPage =false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isLoading=true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount =layoutManager.itemCount


            val isNotLoadingAndNotLastPage=!isLoading && !isLastPage
            val isAtLastItem=firstVisiblePosition + visibleItemCount >=totalItemCount
            val isNotAtBegining = firstVisiblePosition >= 0
            val isTotalMoreThanVisible=totalItemCount >=QUERY_PAGE_SIZE
            val shouldPaginate=isNotLoadingAndNotLastPage && isAtLastItem  && isNotAtBegining
                    &&isTotalMoreThanVisible && isScroling
            if (shouldPaginate){
                homeViewModel.getBreakingNews("us")
                isScroling= false
            }





        }
    }
}