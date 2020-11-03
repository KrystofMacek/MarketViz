package com.krystofmacek.marketviz.ui.fragments.portfolio

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.ui.adapters.PositionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_portfolio.*
import javax.inject.Inject

@AndroidEntryPoint
class PortfolioFragment : Fragment(R.layout.fragment_portfolio) {

    private val portfolioViewModel: PortfolioViewModel by viewModels()

    @Inject
    lateinit var positionAdapter: PositionAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_portfolio_recycler.apply {
            this.adapter = positionAdapter
        }

        subscribeObservers()
    }

    private fun subscribeObservers() {
        portfolioViewModel.portfolioList.observe(viewLifecycleOwner, Observer {
            positionAdapter.submitList(it)
        })
    }

}