package com.krystofmacek.marketviz.ui.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.databinding.FragmentSearchBinding
import com.krystofmacek.marketviz.ui.adapters.AutoCompleteAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search),
    AutoCompleteAdapter.OnItemSelectedListener {

    private val listener = this

    private val searchViewModel: SearchViewModel by viewModels()

    @Inject
    lateinit var autoCompleteAdapter: AutoCompleteAdapter

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_search_autocomplete_recycler.apply {
            autoCompleteAdapter.onItemSelectedListener = listener
            this.adapter = autoCompleteAdapter
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        searchViewModel.symbolKeyWord.observe(viewLifecycleOwner, Observer {
            searchViewModel.getAutoCompleteSymbols(it)
        })

        searchViewModel.autoCompletedList.observe(viewLifecycleOwner, Observer {
            autoCompleteAdapter.submitList(it)
        })
    }


    override fun onItemSelected(position: Int) {
        val selectedItem = searchViewModel.autoCompletedList.value?.get(position)
        searchViewModel.symbolKeyWord.postValue(
           selectedItem?.symbol
        )
    }


}