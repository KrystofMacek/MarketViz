package com.krystofmacek.marketviz.ui.fragments.portfolio

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.databinding.FragmentPortfolioBinding
import com.krystofmacek.marketviz.ui.adapters.OnItemSelectedListener
import com.krystofmacek.marketviz.ui.adapters.PositionAdapter
import com.krystofmacek.marketviz.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PortfolioFragment : Fragment(R.layout.fragment_portfolio), OnItemSelectedListener {

    private val listener = this

    private val portfolioViewModel: PortfolioViewModel by viewModels()

    @Inject
    lateinit var positionAdapter: PositionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = DataBindingUtil.inflate<FragmentPortfolioBinding>(
            inflater,
            R.layout.fragment_portfolio,
            container,
            false
        ).apply {
            this.lifecycleOwner = viewLifecycleOwner
            this.viewModel = portfolioViewModel
            fragmentPortfolioRecycler.adapter = positionAdapter
            positionAdapter.onItemSelectedListener = listener
        }

        subscribeObservers()

        return binding.root
    }

    private fun subscribeObservers() {
        portfolioViewModel.portfolioList.observe(viewLifecycleOwner, {
            positionAdapter.submitList(it)
            portfolioViewModel.currentPL.postValue(positionAdapter.getCurrentPL())
            portfolioViewModel.numOfPositions.postValue(positionAdapter.itemCount)
        })

        portfolioViewModel.updateTotalPL.observe(viewLifecycleOwner, {
            if(it) {
                saveToSP()
                portfolioViewModel.totalPlUpdateFinished()
            }
        })

        portfolioViewModel.loadTotalPL.observe(viewLifecycleOwner, {
            if (it) {
                val totalPl = loadFromSP()

                portfolioViewModel.totalPL.postValue(Utils.round(totalPl))

                portfolioViewModel.totalPlLoaded()
            }
        })

    }

    private fun saveToSP() {
        val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)

        sharedPreferences?.let {
            with(sharedPreferences.edit()) {
                this?.run {
                    val value = portfolioViewModel.totalPL.value ?: 0.0
                    putFloat(getString(R.string.total_pl), value.toFloat())
                    apply()
                }
            }
        }
    }
    private fun loadFromSP(): Double {
        val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)

        return sharedPreferences?.getFloat(getString(R.string.total_pl), 0.0F)?.toDouble() ?: 0.0

    }

    override fun onItemSelected(position: Int) {
        portfolioViewModel.selectedItem.postValue(position)
        displayDialog()
    }

    private fun displayDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.pd_title))
            .setMessage(resources.getString(R.string.pd_message))
            .setNegativeButton(resources.getString(R.string.pd_cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(resources.getString(R.string.pd_accept)) { dialog, _ ->
                portfolioViewModel.closePosition()
                dialog.cancel()
            }
            .show()
    }

}