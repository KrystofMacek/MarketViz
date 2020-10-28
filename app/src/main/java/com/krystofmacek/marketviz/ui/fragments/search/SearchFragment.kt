package com.krystofmacek.marketviz.ui.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.databinding.FragmentSearchBinding
import com.krystofmacek.marketviz.model.networkmodels.autocomplete.Symbols
import com.krystofmacek.marketviz.ui.adapters.AutoCompleteAdapter
import com.krystofmacek.marketviz.utils.Constants.SEARCH_FRAGMENT
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

        fragment_search_symbol_inputT.setOnEditorActionListener { v, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    searchQuote(v.text.toString())
                    true
                }
                else -> false
            }
        }

        subscribeObservers()
    }

    private fun subscribeObservers() {
        searchViewModel.symbolKeyWord.observe(viewLifecycleOwner, Observer {
            val keyW = it ?: ""
            searchViewModel.getAutoCompleteSymbols(keyW)
        })

        searchViewModel.autoCompletedList.observe(viewLifecycleOwner, Observer {
            val symbols = it ?: Symbols()
            autoCompleteAdapter.submitList(symbols)
        })

        searchViewModel.navigateToDetails.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                this.findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToDetailsFragment()
                )
                searchViewModel.doneNavigating()
            }
        })
    }


    override fun onItemSelected(position: Int) {
        val selectedItem = searchViewModel.autoCompletedList.value?.get(position)
        searchViewModel.symbolKeyWord.postValue(
           selectedItem?.symbol
        )
    }

    private fun searchQuote(quote: String) {
        searchViewModel.searchQuote(quote)
    }


}