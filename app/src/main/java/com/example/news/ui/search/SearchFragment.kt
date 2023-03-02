package com.example.news.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.news.MainActivity
import com.example.news.databinding.FragmentSearchedBinding
import com.example.news.ui.NewsViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchedBinding? = null

    private lateinit var newsModel:NewsViewModel
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchedBinding.inflate(inflater, container, false)

//        newsModel=(activity as MainActivity).newsViewModel
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}