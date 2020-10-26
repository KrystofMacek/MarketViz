package com.krystofmacek.marketviz.ui.fragments.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil
            .inflate<FragmentSearchBinding>(inflater, R.layout.fragment_search, container, false)
            .apply {
                this.lifecycleOwner = viewLifecycleOwner
                this.viewModel = searchViewModel
            }


        subscribeObservers()

        return binding.root
    }

    private fun subscribeObservers() {
        searchViewModel.symbolKeyWord.observe(viewLifecycleOwner, Observer {

        })
    }


}