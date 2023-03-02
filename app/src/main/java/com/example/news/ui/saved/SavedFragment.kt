package com.example.news.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.news.MainActivity
import com.example.news.databinding.FragmentSavedBinding
import com.example.news.ui.NewsViewModel

class SavedFragment : Fragment() {
    private var _binding: FragmentSavedBinding? = null

    private lateinit var newsViewModel:NewsViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[SavedViewModel::class.java]

        _binding = FragmentSavedBinding.inflate(inflater, container, false)
//        newsViewModel=(activity as MainActivity).newsViewModel
        val root: View = _binding!!.root

        return root
    }

}