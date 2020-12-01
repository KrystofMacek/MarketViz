package com.krystofmacek.marketviz.ui.fragments.overview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.ui.adapters.MarketIndexAdapter
import com.krystofmacek.marketviz.ui.adapters.OnItemSelectedListener
import com.krystofmacek.marketviz.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_overview.*
import javax.inject.Inject

@AndroidEntryPoint
class OverviewFragment : Fragment(R.layout.fragment_overview), OnItemSelectedListener {

    private val listener = this

    private val overviewViewModel: OverviewViewModel by viewModels()

    @Inject
    lateinit var marketIndexAdapter: MarketIndexAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_overview_markets_recyclerView?.apply {
            adapter = marketIndexAdapter
            marketIndexAdapter.onItemSelectedListener = listener
        }

        fragment_overview_chart?.apply {
            Utils.setupCandleStickChart(this)
        }

        subscribeObservers()
    }


    private fun subscribeObservers() {
        overviewViewModel.marketIndices.observe(viewLifecycleOwner, {
            marketIndexAdapter.submitList(it)
        })

        overviewViewModel.selectedItem.observe(viewLifecycleOwner, {
            it?.let {
                overviewViewModel.loadChart(it)
            }
        })

        overviewViewModel.dataList.observe(viewLifecycleOwner, {
            it?.let {
                overviewViewModel.createCandleData(it)
            }
        })

        overviewViewModel.candleData.observe(viewLifecycleOwner, {
            it?.let { candleData ->
                fragment_overview_chart?.let { chart ->
                    chart.data = candleData
                    chart.invalidate()
                }
            }
        })
    }

    override fun onItemSelected(position: Int) {
        overviewViewModel.selectedItem.postValue(position)
    }
}