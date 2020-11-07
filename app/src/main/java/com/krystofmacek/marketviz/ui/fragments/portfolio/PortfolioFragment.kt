package com.krystofmacek.marketviz.ui.fragments.portfolio

import android.content.Context
import android.os.Bundle
import android.system.Os.accept
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.databinding.FragmentPortfolioBinding
import com.krystofmacek.marketviz.ui.adapters.OnItemSelectedListener
import com.krystofmacek.marketviz.ui.adapters.PositionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_portfolio.*
import javax.inject.Inject

@AndroidEntryPoint
class PortfolioFragment : Fragment(R.layout.fragment_portfolio), OnItemSelectedListener {

    private val listener = this

    private val portfolioViewModel: PortfolioViewModel by viewModels()

    private val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)

    @Inject
    lateinit var positionAdapter: PositionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
        portfolioViewModel.portfolioList.observe(viewLifecycleOwner, Observer {
            positionAdapter.submitList(it)
            portfolioViewModel.currentPL.postValue(positionAdapter.getCurrentPL())
            portfolioViewModel.numOfPositions.postValue(positionAdapter.itemCount)
        })

        portfolioViewModel.updateTotalPL.observe(viewLifecycleOwner, Observer {

            with(sharedPreferences?.edit()) {
                portfolioViewModel.totalPL.value?.let { value ->
                    this?.let {
                        putFloat(getString(R.string.total_pl), value)
                        apply()
                    }
                }
            }

            portfolioViewModel.totalPlUpdateFinished()
        })

        portfolioViewModel.loadTotalPL.observe(viewLifecycleOwner, Observer {
            val totalPl = sharedPreferences?.getFloat(getString(R.string.total_pl), 0.0F)
            portfolioViewModel.totalPL.postValue(totalPl)
            portfolioViewModel.totalPlLoaded()
        })

    }

    override fun onItemSelected(position: Int) {
        portfolioViewModel.selectedItem.postValue(position)
        displayDialog()
    }

    private fun displayDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.pd_title))
            .setMessage(resources.getString(R.string.pd_message))
            .setNegativeButton(resources.getString(R.string.pd_accept)) { dialog, _ ->
                portfolioViewModel.closePosition()
                dialog.cancel()
            }
            .setPositiveButton(resources.getString(R.string.pd_cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }


}