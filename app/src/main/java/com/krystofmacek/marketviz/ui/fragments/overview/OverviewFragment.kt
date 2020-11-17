package com.krystofmacek.marketviz.ui.fragments.overview

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.ui.adapters.MarketIndexAdapter
import com.krystofmacek.marketviz.ui.adapters.OnItemSelectedListener
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

        /**
         * Setup chart
         * */
        fragment_overview_chart?.apply {
            this.isHighlightPerDragEnabled = true
            this.setDrawBorders(true)
            this.setBorderColor(Color.LTGRAY)
            this.axisLeft?.apply {
                setDrawGridLines(false)
                setDrawLabels(false)
            }
            this.axisRight?.apply {
                setDrawGridLines(false)
                textColor = Color.WHITE
            }
            this.xAxis?.apply {
                setDrawGridLines(false)
                setDrawLabels(false)
                granularity = 1f
                isGranularityEnabled = true
                setAvoidFirstLastClipping(false)
            }

        }

        subscribeObservers()
    }


    private fun subscribeObservers() {
        overviewViewModel.marketIndices.observe(viewLifecycleOwner, Observer {
            marketIndexAdapter.submitList(it)
        })

        overviewViewModel.selectedItem.observe(viewLifecycleOwner, Observer {
            it?.let {
                overviewViewModel.loadChart(it)
            }
        })

        overviewViewModel.dataList.observe(viewLifecycleOwner, Observer {
            it?.let {
                overviewViewModel.createCandleData(it)
            }
        })

        overviewViewModel.candleData.observe(viewLifecycleOwner, Observer {
            it?.let { candleData ->

                Log.i("loadChart", "candle data = ${candleData.toString()}")
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