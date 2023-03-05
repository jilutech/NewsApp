package com.example.news.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.Repo.NewsRpo
import com.example.news.adapters.NewsAdapters
import com.example.news.databinding.FragmentSavedBinding
import com.example.news.db.ArticleDB
import com.example.news.ui.NewsViewModel
import com.example.news.ui.NewsViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar

class SavedFragment : Fragment() {
    private var _binding: FragmentSavedBinding? = null

    private lateinit var newsViewModel:NewsViewModel

    private lateinit var newsAdapters: NewsAdapters


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)

        val newsRepo = NewsRpo(ArticleDB(requireActivity()))
        val viewModelProviderFactory=NewsViewModelProviderFactory(newsRepo)
        newsViewModel=ViewModelProvider(this,viewModelProviderFactory)[NewsViewModel::class.java]

        setRecycle()
        newsAdapters.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_savedFragment_to_articleFragment,
                bundle
            )
        }

        newsViewModel.getSavedNews().observe(viewLifecycleOwner, Observer {  article ->
            newsAdapters.differ.submitList(article)
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
             ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position =viewHolder.adapterPosition
                val article= newsAdapters.differ.currentList[position]
                newsViewModel.deleteArticle(article)
                view?.let { Snackbar.make(it,"Deleted",Snackbar.LENGTH_SHORT).apply {
                    setAction("undo"){
                        newsViewModel.saveArticle(article)
                    }
                    show()
                } }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(_binding!!.rvSavedNews)
        }

        val root: View = _binding!!.root


        return root
    }

    fun setRecycle(){
        newsAdapters= NewsAdapters()
        _binding?.rvSavedNews?.apply {
            adapter=newsAdapters
            layoutManager=LinearLayoutManager(requireActivity())
        }
    }
}