package com.krystofmacek.marketviz.ui.fragments.overview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.ui.adapters.MarketIndexAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_overview.*
import javax.inject.Inject

@AndroidEntryPoint
class OverviewFragment : Fragment(R.layout.fragment_overview) {

    private val viewModel: OverviewViewModel by viewModels()

    @Inject
    lateinit var marketIndexAdapter: MarketIndexAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_overview_markets_recyclerView.apply {
            adapter = marketIndexAdapter
        }

        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.marketIndices.observe(viewLifecycleOwner, Observer {
            Log.i("OBS","observing ${it.size}")
            marketIndexAdapter.submitList(it)
        })
    }

}