package com.krystofmacek.marketviz.ui.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.databinding.FragmentSearchBinding
import com.krystofmacek.marketviz.model.networkmodels.autocomplete.Symbols
import com.krystofmacek.marketviz.ui.adapters.AutoCompleteAdapter
import com.krystofmacek.marketviz.ui.adapters.OnItemSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search),
    OnItemSelectedListener {

    private val listener = this

    private val searchViewModel: SearchViewModel by viewModels()

    @Inject
    lateinit var autoCompleteAdapter: AutoCompleteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = DataBindingUtil
            .inflate<FragmentSearchBinding>(inflater, R.layout.fragment_search, container, false)
            .apply {
                this.lifecycleOwner = viewLifecycleOwner
                this.viewModel = searchViewModel

                this.fragmentSearchAutocompleteRecycler.adapter = autoCompleteAdapter
                autoCompleteAdapter.onItemSelectedListener = listener

                this.fragmentSearchSymbolInputT.setOnEditorActionListener { v, actionId, _ ->
                    return@setOnEditorActionListener when (actionId) {
                        EditorInfo.IME_ACTION_SEARCH -> {
                            searchQuote(v.text.toString())
                            true
                        }
                        else -> false
                    }
                }
            }

        subscribeObservers()

        return binding.root
    }

    private fun subscribeObservers() {
        searchViewModel.symbolKeyWord.observe(viewLifecycleOwner, {
            val keyW = it ?: ""
            searchViewModel.getAutoCompleteSymbols(keyW)
        })

        searchViewModel.autoCompletedList.observe(viewLifecycleOwner, {
            val symbols = it ?: Symbols()
            autoCompleteAdapter.submitList(symbols)
        })

        searchViewModel.navigateToDetails.observe(viewLifecycleOwner, {
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